package io.github.emreblbl.kafkachatserver.controller;


import io.github.emreblbl.kafkachatserver.constant.KafkaConstants;
import io.github.emreblbl.kafkachatserver.model.Message;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
public class ChatController {


    final KafkaTemplate<String, Message> kafkaTemplate;

    public ChatController(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(value = "/api/send",consumes = "application/json",produces = "application/json")
    public void sendMessage(@RequestBody Message message){
        message.setTimestamp(LocalDateTime.now().toString());
            //sending the message to kafka topic queue
        try {
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC,message).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
    }
    }



    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
