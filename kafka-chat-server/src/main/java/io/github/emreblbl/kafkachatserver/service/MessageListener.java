package io.github.emreblbl.kafkachatserver.service;

import io.github.emreblbl.kafkachatserver.constant.KafkaConstants;
import io.github.emreblbl.kafkachatserver.model.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {


    final SimpMessagingTemplate template;


    public MessageListener(SimpMessagingTemplate template) {
        this.template = template;
    }
    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )

    public void listen(Message message){
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/group",message);
    }
}
