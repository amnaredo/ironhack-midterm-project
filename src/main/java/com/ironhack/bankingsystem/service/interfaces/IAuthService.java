package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;

public interface IAuthService {
    Boolean authAccountAccess(/*UserDetails userDetails,*/ Long accountId);
    Boolean authMoneyTransfer(String headerToken, MoneyTransferDTO moneyTransferDTO, Long accountId);
}
