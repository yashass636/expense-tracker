package com.expese.service.consumer;

import com.expese.service.dto.ExpenseDTO;
import com.expese.service.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseConsumer {

    private ExpenseService expenseService;

    @Autowired
    ExpenseConsumer (ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //TODO : in-complete topic
    @KafkaListener(topics= "${spring.kafka.topic-json.name}", groupId= "${spring.kafka.consumer.group-id}")
    public void listen (ExpenseDTO expenseDTO){

        try{

            expenseService.createExpense(expenseDTO);
            System.out.println(expenseDTO.getMerchant()+"-------------------------------------\n");

        } catch (Exception ex){

            ex.printStackTrace();
            System.out.println("exception in listening");
        }
    }

}
