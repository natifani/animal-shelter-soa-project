package com.soa.demo.animalshelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.demo.animalshelter.controller.exception.BadRequestException;
import com.soa.demo.animalshelter.controller.exception.NotFoundException;
import com.soa.demo.animalshelter.dto.AnimalIncomingDTO;
import com.soa.demo.animalshelter.dto.AnimalOutgoingDTO;
import com.soa.demo.animalshelter.mapper.AnimalMapper;
import com.soa.demo.animalshelter.model.Animal;
import com.soa.demo.animalshelter.service.IAnimalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final IAnimalService animalService;
    private final AnimalMapper animalMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public AnimalController(IAnimalService animalService, AnimalMapper animalMapper, ObjectMapper objectMapper) {
        this.animalService = animalService;
        this.animalMapper = animalMapper;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalOutgoingDTO create(@RequestParam("model") String model, @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
        try {


            AnimalIncomingDTO animalIncomingDTO = objectMapper.readValue(model, AnimalIncomingDTO.class);
            Animal animal = animalMapper.dtoToAnimal(animalIncomingDTO);
            if (file != null && !file.isEmpty()) {
                animal.setImage(file.getBytes());
            }
            return animalMapper.animalToDto(animalService.create(animal));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Kennel number should be unique.");
        }
    }

    @GetMapping
    public ResponseEntity<List<AnimalOutgoingDTO>> findAll(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Animal> animals = animalService.findAll(pageNumber, pageSize);
        Long count = animalService.countAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Animal-Total-Count");
        headers.add("Animal-Total-Count", String.valueOf(count));

        return ResponseEntity.ok().headers(headers).body(animalMapper.animalsToDtos(animals));
    }

    @GetMapping("{id}")
    public AnimalOutgoingDTO findById(@PathVariable Long id) {
        Animal animal = animalService.findById(id);
        if (animal != null) {
            return animalMapper.animalToDto(animal);
        } else {
            throw new NotFoundException();
        }
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@PathVariable Long id, @RequestBody @Valid AnimalIncomingDTO animalIncomingDTO) {
        if (animalService.findById(id) == null) {
            throw new NotFoundException();
        } else {
            Animal animal = animalMapper.dtoToAnimal(animalIncomingDTO);
            animalService.update(animal);
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!animalService.deleteById(id)) {
            throw new NotFoundException();
        }
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void uploadPhoto(@PathVariable Long id, @RequestParam(value = "image") MultipartFile file) throws IOException {
        if (animalService.findById(id) == null) {
            throw new NotFoundException();
        } else {
            animalService.uploadImage(id, file.getBytes());
        }
    }
}
