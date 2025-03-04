package com.SecApp.SpringSec.eventProducer;

import com.SecApp.SpringSec.models.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {

    /*
    *
    * KafkaTemplate<K, V> is a generic class provided by Spring Kafka for sending messages to Kafka.
    *   - String (K)      → Represents the key of the Kafka message (can be null if not used).
    *   - UserInfoDto (V) → Represents the value (payload) being sent.
    *
    * This means KafkaTemplate<String, UserInfoDto> is a Kafka producer that:
    *   - Sends messages where the key is a String.
    *   - Sends messages where the value is a UserInfoDto object.
    *
    * NOTE:
    *   - You do not always have to send a key.
    *   - If a key is not provided, Kafka distributes messages randomly.
    *   - If a key is provided, Kafka ensures messages with the same key go to the same partition.
    *
    *   -> With a key → Messages with the same key go to the same partition (useful for maintaining order per entity).
    *   -> Without a key → Messages are distributed randomly across partitions (better for load balancing).
    *   -> Kafka's default hash-based partitioning ensures that all messages with the same key go to the same partition.
    *
    * */
    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;

    @Autowired
    UserInfoProducer (KafkaTemplate<String, UserInfoDto> kafkaTemplate){

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent userInfoDto){
        Message<UserInfoEvent> message = MessageBuilder.withPayload(userInfoDto)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName).build();

        kafkaTemplate.send(message);
    }
}
