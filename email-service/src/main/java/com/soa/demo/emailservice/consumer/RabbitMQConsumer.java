package com.soa.demo.emailservice.consumer;

import com.soa.demo.emailservice.model.RabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "q.adoption-email")
    public void recievedMessage(RabbitMessage rabbitMessage) throws InterruptedException {
       log.info("Recieved Message From RabbitMQ: " + rabbitMessage);

       Thread.sleep(10000);
       log.info("Sending email to {} with content: {}", rabbitMessage.getEmailAddress(), rabbitMessage.getMessage());
    }

}
