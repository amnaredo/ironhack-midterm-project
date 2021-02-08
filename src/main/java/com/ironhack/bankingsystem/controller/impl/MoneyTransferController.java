package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IMoneyTransferController;
import com.ironhack.bankingsystem.dto.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoneyTransferController implements IMoneyTransferController {
    public Transaction transferMoney(MoneyTransferDTO moneyTransferDTO) {
        return null;
    }
}
