package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;

public interface IFraudDetectionService {
    //void checkMoneyTransfer(Account account, MoneyTransferDTO moneyTransferDTO);
    void checkMoneyTransferV2(Account account, MoneyTransferDTO moneyTransferDTO);
}
