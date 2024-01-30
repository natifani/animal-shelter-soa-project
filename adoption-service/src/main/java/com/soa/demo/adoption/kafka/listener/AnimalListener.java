package com.soa.demo.adoption.kafka.listener;

import com.soa.demo.adoption.kafka.message.AnimalMessage;
import com.soa.demo.adoption.model.Animal;
import com.soa.demo.adoption.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnimalListener {
    private AnimalService animalService;

    @Autowired
    public AnimalListener(AnimalService animalService) {
        this.animalService = animalService;
    }

    @KafkaListener(topics = "animal-topic", containerFactory = "kafkaListenerAnimalContainerFactory")
    public void newAnimalListener(AnimalMessage animalMessage) {
        Animal animal = new Animal();
        animal.setId(animalMessage.getId());
        animal.setName(animalMessage.getName());
        animal.setBreed(animalMessage.getBreed());
        animal.setAge(animalMessage.getAge());
        animal.setGender(animalMessage.getGender());
        animal.setWeight(animalMessage.getWeight());
        animal.setColor(animalMessage.getColor());
        animal.setArrivalDate(animalMessage.getArrivalDate());
        animal.setMedicalInformation(animalMessage.getMedicalInformation());
        animal.setSpecialNeeds(animalMessage.getSpecialNeeds());
        animal.setKennelNumber(animalMessage.getKennelNumber());
        animal.setAdoptionStatus(animalMessage.getAdoptionStatus());

        switch (animalMessage.getAction()) {
            case "CREATE":
                animalService.create(animal);
                break;
            case "UPDATE":
                animalService.update(animal);
                break;
            case "DELETE":
                animalService.deleteById(animal.getId());
                break;
        }
    }
}
