package com.soa.demo.animalshelter.kafka.producer;

import com.soa.demo.animalshelter.kafka.message.AnimalMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor
@Component
public class AnimalProducer {
    private final String animalTopic = "animal-topic";

    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    public AnimalProducer(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(AnimalMessage message) {
        CompletableFuture<SendResult<String, Serializable>> future = kafkaTemplate.send(animalTopic, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message = {} due to: {}", message.toString(), ex.getMessage());
            } else {
                log.info("Message sent successfully with offset = {}", result.getRecordMetadata().offset());
            }
        });
    }

}
