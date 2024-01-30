package com.soa.demo.adoption.service;

import com.soa.demo.adoption.model.Animal;
import com.soa.demo.adoption.repository.AnimalRepository;
import com.soa.demo.adoption.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal create(Animal animal) throws DataIntegrityViolationException {
        return animalRepository.save(animal);
    }

    public void deleteById(Long id) {
        animalRepository.deleteById(id);
    }

    public void update(Animal animal) {
         animalRepository.save(animal);
    }

    public void updateStatus(Long id, Status status) {
        Animal animal = animalRepository.findById(id).orElse(null);
        if (Objects.nonNull(animal)) {
            animal.setAdoptionStatus(status);
            animalRepository.save(animal);
        }
    }
    public boolean checkIfExists(Long id) {
       return animalRepository.existsById(id);
    }
    public Animal findById(Long id) { return animalRepository.findById(id).orElse(null); }
}
