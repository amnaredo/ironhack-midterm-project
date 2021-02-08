package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IMoneyTransferController;
import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.IMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
public class MoneyTransferController implements IMoneyTransferController {

    @Autowired
    private IMoneyTransferService moneyTransferService;
    @Autowired
    private IAccountService accountService;

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable @NumberFormat @Min(1) Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction transferMoney(@RequestBody @Valid MoneyTransferDTO moneyTransferDTO, @PathVariable @NumberFormat @Min(1) Long id) {
        return moneyTransferService.doMoneyTransfer(moneyTransferDTO, id);
    }
}
