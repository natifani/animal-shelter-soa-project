package com.soa.demo.adoption.controller;


import com.soa.demo.adoption.controller.exception.BadRequestException;
import com.soa.demo.adoption.controller.exception.NotFoundException;
import com.soa.demo.adoption.dto.AdoptionRequestIncomingDTO;
import com.soa.demo.adoption.dto.AdoptionRequestOutgoingDTO;
import com.soa.demo.adoption.jwt.JWTUtils;
import com.soa.demo.adoption.mapper.AdoptionMapper;
import com.soa.demo.adoption.model.AdoptionRequest;
import com.soa.demo.adoption.model.Animal;
import com.soa.demo.adoption.model.User;
import com.soa.demo.adoption.rabbitmq.RabbitMessage;
import com.soa.demo.adoption.service.AnimalService;
import com.soa.demo.adoption.service.IAdoptionService;
import com.soa.demo.adoption.service.UserService;
import com.soa.demo.adoption.util.Status;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("api/adoptions")
public class AdoptionController {
    private final IAdoptionService adoptionService;
    private final AnimalService animalService;
    private final UserService userService;
    private final AdoptionMapper adoptionMapper;
    private final JWTUtils jwtUtils;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AdoptionController(IAdoptionService adoptionService, AdoptionMapper adoptionMapper, AnimalService animalService, UserService userService, JWTUtils jwtUtils, RabbitTemplate rabbitTemplate) {
        this.adoptionService = adoptionService;
        this.adoptionMapper = adoptionMapper;
        this.animalService = animalService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionRequestOutgoingDTO create(@RequestBody @Valid AdoptionRequestIncomingDTO adoptionRequestIncomingDTO) {
        try {
            AdoptionRequest adoptionRequest = adoptionMapper.dtoToAdoption(adoptionRequestIncomingDTO);
            String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);
            String username = jwtUtils.getUserNameFromJwtToken(token);

            Animal animal = animalService.findById(adoptionRequestIncomingDTO.getAnimalId());
            User user = userService.findById(adoptionRequestIncomingDTO.getUserId());

            adoptionRequest.setUser(user);
            adoptionRequest.setAnimal(animal);
            adoptionRequest.setRequestDate(new Date(System.currentTimeMillis()));

            if (Objects.isNull(adoptionRequest.getUser())) {
                throw new NotFoundException("User does not exist");
            }

            if (!username.equals(adoptionRequest.getUser().getUsername())) {
                throw new BadRequestException("Incompatible users");
            }

            if (Objects.isNull(adoptionRequest.getAnimal())) {
                throw new NotFoundException("Animal does not exist");
            }

            if (adoptionRequest.getAnimal().getAdoptionStatus() != Status.NOT_ADOPTED) {
                throw new BadRequestException("Animal cannot be adopted");
            }

            AdoptionRequest adoptionRequestCreated = adoptionService.create(adoptionRequest);
            animalService.updateStatus(adoptionRequest.getAnimal().getId(), Status.PENDING);
            return adoptionMapper.adoptionToDto(adoptionRequestCreated);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("User and animal pair should be unique.");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("all")
    public ResponseEntity<List<AdoptionRequestOutgoingDTO>> findAll(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize) {

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        List<AdoptionRequest> adoptionRequests = adoptionService.findAll(pageNumber, pageSize);
        Long count = adoptionService.countAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Adoption-Total-Count");
        headers.add("Adoption-Total-Count", String.valueOf(count));

        return ResponseEntity.ok().headers(headers).body(adoptionMapper.adoptionsToDtos(adoptionRequests));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<AdoptionRequestOutgoingDTO>> findAllByUser(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize) {

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user = userService.findByUsername(username);

        if (Objects.isNull(user)) {
            throw new BadRequestException("User does not exist.");
        } else {
            List<AdoptionRequest> adoptionRequests = adoptionService.findByUserId(user.getId(), pageNumber, pageSize);
            Long count = adoptionService.countByUser(user);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Expose-Headers", "Adoption-Total-Count");
            headers.add("Adoption-Total-Count", String.valueOf(count));

            return ResponseEntity.ok().headers(headers).body(adoptionMapper.adoptionsToDtos(adoptionRequests));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void reactToAdoptionRequest(@PathVariable Long id, @RequestParam String reaction) {
        AdoptionRequest adoptionRequest = adoptionService.findById(id);
        if (Objects.isNull(adoptionRequest)) {
            throw new BadRequestException("The adoption request does not exist.");
        }

        if ("accept".equals(reaction)) {
            animalService.updateStatus(adoptionRequest.getAnimal().getId(), Status.ADOPTED);
            adoptionService.deleteById(id);
            adoptionService.reactToAdoptionRequest(adoptionRequest, reaction);
            RabbitMessage rabbitMessage = new RabbitMessage(adoptionRequest.getUser().getEmail(), "Your adoption request to adopt " + adoptionRequest.getAnimal().getName() + " has been accepted.");
            rabbitTemplate.convertAndSend("", "q.adoption-email", rabbitMessage);
        }

        if ("reject".equals(reaction)) {
            animalService.updateStatus(adoptionRequest.getAnimal().getId(), Status.NOT_ADOPTED);
            adoptionService.deleteById(id);
            adoptionService.reactToAdoptionRequest(adoptionRequest, reaction);
            RabbitMessage rabbitMessage = new RabbitMessage(adoptionRequest.getUser().getEmail(), "Your adoption request to adopt " + adoptionRequest.getAnimal().getName() + " has been rejected.");
            rabbitTemplate.convertAndSend("", "q.adoption-email", rabbitMessage);
        }
    }


}
