package com.SecApp.SpringSec.serializer;

import com.SecApp.SpringSec.eventProducer.UserInfoEvent;
import com.SecApp.SpringSec.models.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoEvent> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String s, UserInfoEvent userInfoDto) {

        //Initially convert to string, then to bytes.
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            retVal = objectMapper.writeValueAsString(userInfoDto).getBytes();

        }catch (Exception e){
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public void close() {
    }
}
