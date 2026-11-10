package com.vcn.chat.consumer;

import com.vcn.chat.Service.ParticipantService;
import com.vcn.chat.model.Participant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private final ParticipantService participantService;

    public Consumer(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.users-create_participant}"})
    public void rendOrderConfirmation(@Payload String username) {
        participantService.save(new Participant(null, username, null, null, null));
    }

}
