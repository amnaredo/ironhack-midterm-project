package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.security.CustomUserDetails;

public interface IAuthService {
    Boolean authAccountAccess(CustomUserDetails userDetails, Long accountId);
    Boolean authMoneyTransfer(CustomUserDetails userDetails, String headerToken, MoneyTransferDTO moneyTransferDTO, Long accountId);
}
