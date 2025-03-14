package com.expese.service.controller;

import com.expese.service.dto.ExpenseDTO;
import com.expese.service.service.ExpenseService;
import jakarta.websocket.server.PathParam;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    ExpenseController (ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @GetMapping("/expense/v1/")
    public ResponseEntity<List<ExpenseDTO>> getExpenses (@PathParam("user_id") @NonNull String userId){

        try{

            List<ExpenseDTO> expenseDTOS = expenseService.getExpenses(userId);
            return new ResponseEntity<>(expenseDTOS, HttpStatus.OK);

        }catch (Exception ex){

            ex.printStackTrace();

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
