package com.soa.demo.authservice.kafka.producer;

import com.soa.demo.authservice.kafka.message.AuthMessage;
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
public class AuthProducer {
    private final String authTopic = "auth-topic";

    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    public AuthProducer(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(AuthMessage message) {
        CompletableFuture<SendResult<String, Serializable>> future = kafkaTemplate.send(authTopic, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message = {} due to: {}", message.toString(), ex.getMessage());
            } else {
                log.info("Message sent successfully with offset = {}", result.getRecordMetadata().offset());
            }
        });
    }
}
