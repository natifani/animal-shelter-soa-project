package com.soa.demo.animalshelter.kafka.listener;

import com.soa.demo.animalshelter.kafka.message.AdoptionMessage;
import com.soa.demo.animalshelter.model.Animal;
import com.soa.demo.animalshelter.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AdoptionListener {
    private AnimalService animalService;

    @Autowired
    public AdoptionListener(AnimalService animalService) {
        this.animalService = animalService;
    }

    @KafkaListener(topics = "adoption-topic", containerFactory = "kafkaListenerContainerFactory")
    public void newAdoptionListener(AdoptionMessage adoptionMessage) {
        log.info("Get request from adoption-topic " + adoptionMessage.toString());
        Animal animal = animalService.findById(adoptionMessage.getAnimalId());
        log.info(animal.getId().toString());
        log.info(adoptionMessage.getAdoptionStatus().toString());
        if(Objects.nonNull(animal)) {
            animal.setAdoptionStatus(adoptionMessage.getAdoptionStatus());
            animalService.update(animal);
        }
    }
}
