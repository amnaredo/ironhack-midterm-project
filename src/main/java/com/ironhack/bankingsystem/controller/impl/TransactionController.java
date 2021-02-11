package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.ITransactionController;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class TransactionController implements ITransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/accounts/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactionsByAccount(@PathVariable("id") @NumberFormat @Min(1) Long idAccount) {
        return transactionService.getTransactionsByAccount(idAccount);
    }
}
