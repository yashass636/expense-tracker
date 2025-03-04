package com.singhdevhub.userService.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.singhdevhub.userService.entities.UserInfo;
import com.singhdevhub.userService.entities.UserInfoDto;
import com.singhdevhub.userService.repository.UserRepository;
//import com.singhdevhub.userService.service.UserService;
import com.singhdevhub.userService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceConsumer
{
    @Autowired
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(UserInfoDto eventData) {
        try{

            userService.createOrUpdateUser(eventData);

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
        }
    }

}