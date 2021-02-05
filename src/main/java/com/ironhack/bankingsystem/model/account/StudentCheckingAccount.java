package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;

public class StudentCheckingAccount extends CheckingAccount {

//    Student Checking Accounts are identical to Checking Accounts except that they do NOT have:
//
//    A monthlyMaintenanceFee
//    A minimumBalance

    public StudentCheckingAccount() {
    }

    public StudentCheckingAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, Status status, Integer secretKey) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, status, secretKey, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
