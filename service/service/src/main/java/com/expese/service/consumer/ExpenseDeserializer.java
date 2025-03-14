package com.expese.service.consumer;

import com.expese.service.dto.ExpenseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ExpenseDeserializer implements Deserializer<ExpenseDTO> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public ExpenseDTO deserialize(String s, byte[] bytes) {

        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseDTO   expenseDTO = null;

        try{

            //data is fetched in bytes form, form kafka and the obtained data is mapped with "Expense" class by ObjectMapper
            expenseDTO = objectMapper.readValue(bytes, ExpenseDTO.class);
            System.out.println(expenseDTO.getMerchant());

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("deserialization failed.");
        }

        return expenseDTO;
    }

    @Override
    public void close() {}

}
