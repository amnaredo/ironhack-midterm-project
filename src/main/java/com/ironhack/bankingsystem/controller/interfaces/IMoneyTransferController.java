package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;

public interface IMoneyTransferController {
    Account getAccount(Long id);
    Transaction transferMoney(MoneyTransferDTO moneyTransferDTO, Long id);
}
