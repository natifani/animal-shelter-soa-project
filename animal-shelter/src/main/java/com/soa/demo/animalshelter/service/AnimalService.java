package com.soa.demo.animalshelter.service;

import com.soa.demo.animalshelter.kafka.message.AnimalMessage;
import com.soa.demo.animalshelter.kafka.producer.AnimalProducer;
import com.soa.demo.animalshelter.model.Animal;
import com.soa.demo.animalshelter.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class AnimalService implements IAnimalService {
    private AnimalRepository animalRepository;

    private AnimalProducer animalProducer;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, AnimalProducer animalProducer) {
        this.animalRepository = animalRepository;
        this.animalProducer = animalProducer;
    }

    public Animal create(Animal animal) throws DataIntegrityViolationException {
        Animal newAnimal = animalRepository.save(animal);
        AnimalMessage animalMessage = new AnimalMessage(newAnimal, "CREATE");
        animalProducer.send(animalMessage);
        return newAnimal;
    }

    public List<Animal> findAll(Integer pageNumber, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return animalRepository.findAll(paging).getContent();
    }

    public Long countAll() {
        return animalRepository.count();
    }

    public Animal findById(Long id) {
        return animalRepository.findById(id).orElse(null);
    }

    public void update(Animal animal) {
        Animal animalToUpdate = animalRepository.findById(animal.getId()).orElse(null);
        if (animalToUpdate != null) {
            animalToUpdate.setName(animal.getName());
            animalToUpdate.setBreed(animal.getBreed());
            if (animal.getAge() != null) {
                animalToUpdate.setAge(animal.getAge());
            }
            animalToUpdate.setAge(animal.getAge());
            animalToUpdate.setGender(animal.getGender());
            animalToUpdate.setWeight(animal.getWeight());
            animalToUpdate.setColor(animal.getColor());
            animalToUpdate.setArrivalDate(animal.getArrivalDate());
            if (animal.getMedicalInformation() != null) {
                animalToUpdate.setMedicalInformation(animal.getMedicalInformation());
            }
            if (animal.getSpecialNeeds() != null) {
                animalToUpdate.setSpecialNeeds(animal.getSpecialNeeds());
            }
            animalToUpdate.setKennelNumber(animal.getKennelNumber());
            if (animal.getImage() != null) {
                animalToUpdate.setImage(animal.getImage());
            }
            animalToUpdate.setAdoptionStatus(animal.getAdoptionStatus());

            animalRepository.save(animalToUpdate);
            AnimalMessage animalMessage = new AnimalMessage(animalToUpdate, "UPDATE");
            animalProducer.send(animalMessage);
        }
    }

    public boolean deleteById(Long id) {
        if (!animalRepository.existsById(id)) {
            return false;
        }
        Animal animalToDelete = animalRepository.findById(id).orElse(null);
        animalRepository.deleteById(id);
        AnimalMessage animalMessage = new AnimalMessage(animalToDelete, "DELETE");
        animalProducer.send(animalMessage);
        return true;
    }

    public void uploadImage(Long id, byte[] image) {
        Animal animal = animalRepository.findById(id).orElse(null);
        if (animal !=  null) {
            animal.setImage(image);
            animalRepository.save(animal);
        }
    }
}
