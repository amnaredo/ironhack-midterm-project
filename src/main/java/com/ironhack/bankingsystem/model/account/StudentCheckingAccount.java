package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;

public class StudentCheckingAccount extends CheckingAccount {

//    Student Checking Accounts are identical to Checking Accounts except that they do NOT have:
//
//    A monthlyMaintenanceFee
//    A minimumBalance

    public StudentCheckingAccount() {
    }

    public StudentCheckingAccount(AccountHolder owner, Money balance, Integer secretKey) {
        super(owner, balance, secretKey);
        setMonthlyMaintenanceFee(new Money(BigDecimal.ZERO));
        setMinimumBalance(new Money(BigDecimal.ZERO));
    }
}
