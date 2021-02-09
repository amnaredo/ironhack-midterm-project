package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;

public interface IMoneyTransferService {

    Account doMoneyTransfer(MoneyTransferDTO moneyTransferDTO, Long id);
}
