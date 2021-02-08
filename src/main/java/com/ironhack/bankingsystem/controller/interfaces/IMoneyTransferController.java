package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.transaction.Transaction;

public interface IMoneyTransferController {
    Transaction transferMoney(MoneyTransferDTO moneyTransferDTO);
}
