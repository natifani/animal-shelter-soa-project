package com.soa.demo.adoption.service;

import com.soa.demo.adoption.kafka.message.AdoptionMessage;
import com.soa.demo.adoption.kafka.producer.AdoptionProducer;
import com.soa.demo.adoption.model.AdoptionRequest;
import com.soa.demo.adoption.model.User;
import com.soa.demo.adoption.repository.AdoptionRepository;
import com.soa.demo.adoption.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService implements IAdoptionService {

    private final AdoptionRepository adoptionRepository;

    private final AdoptionProducer adoptionProducer;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository, AdoptionProducer adoptionProducer) {
        this.adoptionRepository = adoptionRepository;
        this.adoptionProducer = adoptionProducer;
    }

    @Override
    public AdoptionRequest create(AdoptionRequest adoptionRequest) throws DataIntegrityViolationException {
        AdoptionRequest newAdoptionRequest = adoptionRepository.save(adoptionRequest);
        AdoptionMessage adoptionMessage = new AdoptionMessage(adoptionRequest.getAnimal().getId(), Status.PENDING);
        adoptionProducer.send(adoptionMessage);
        return newAdoptionRequest;
    }

    public void reactToAdoptionRequest(AdoptionRequest adoptionRequest, String reaction) {
        Status status = Status.PENDING;
        if ("accept".equals(reaction)) {
            status = Status.ADOPTED;
        }
        if ("reject".equals(reaction)) {
            status = Status.NOT_ADOPTED;
        }
        AdoptionMessage adoptionMessage = new AdoptionMessage(adoptionRequest.getAnimal().getId(), status);
        adoptionProducer.send(adoptionMessage);
    }

    @Override
    public List<AdoptionRequest> findAll(Integer pageNumber, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return adoptionRepository.findAll(paging).getContent();
    }

    @Override
    public Long countAll() {
        return adoptionRepository.count();
    }

    @Override
    public Long countByUser(User user) {
        return adoptionRepository.countByUser(user);
    }

    @Override
    public List<AdoptionRequest> findByUserId(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return adoptionRepository.findByUserId(paging, userId).getContent();
    }

    @Override
    public AdoptionRequest findById(Long id) {
        return adoptionRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!adoptionRepository.existsById(id)) {
            return false;
        }
        adoptionRepository.deleteById(id);
        return true;
    }
}
