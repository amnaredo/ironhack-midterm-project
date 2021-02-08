package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.transaction.Transaction;

public interface IMoneyTransferService {

    Transaction doMoneyTransfer(MoneyTransferDTO moneyTransferDTO, Long id);
}
