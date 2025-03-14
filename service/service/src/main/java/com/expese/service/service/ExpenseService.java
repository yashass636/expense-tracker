package com.expese.service.service;

import com.expese.service.dto.ExpenseDTO;
import com.expese.service.entity.Expense;
import com.expese.service.repository.ExpenseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private ObjectMapper      objectMapper = new ObjectMapper();

    @Autowired
    ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    //if user wants to add his expense manually
    public boolean createExpense (ExpenseDTO expenseDTO){

        setCurrency(expenseDTO);

        try{

            expenseRepository.save(objectMapper.convertValue(expenseDTO, Expense.class));
            return true;

        }catch (Exception ex){

            System.out.println("Saving to DB failed.");
            return false;
        }
    }

    public boolean updateExpense (ExpenseDTO expenseDTO) {

        //TODO : locking needed here.

        Optional<Expense> expenseFoundOpt = expenseRepository.findByUserIdAndExternalId(expenseDTO.getUserId(), expenseDTO.getExternalId());

        if(expenseFoundOpt.isEmpty()){
            return false;
        }

        Expense expense = expenseFoundOpt.get();
        expense.setCurrency(Strings.isNotBlank(expenseDTO.getCurrency())? expenseDTO.getCurrency() : expense.getCurrency());
        expense.setMerchant(Strings.isNotBlank(expenseDTO.getMerchant())? expenseDTO.getMerchant() : expense.getMerchant());
        expense.setAmount(expenseDTO.getAmount());

        expenseRepository.save(expense);

        return true;
    }

    public List<ExpenseDTO> getExpenses(String userId) {

        List<Expense> expenseList = expenseRepository.findByUserId(userId);
        return objectMapper.convertValue(expenseList, new TypeReference<List<ExpenseDTO>>() {}); //TODO:Study about TypeReference
    }

    private void setCurrency (ExpenseDTO expenseDTO){

        if (Objects.isNull(expenseDTO.getCurrency())){
            expenseDTO.setCurrency("inr");
        }
    }
}
