package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import org.springframework.http.HttpHeaders;

public interface IMoneyTransferController {
    Account getAccount(Long id);
    Account transferMoney(String token, MoneyTransferDTO moneyTransferDTO, Long id);
}
