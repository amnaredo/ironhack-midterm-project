package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;

public class SavingsAccount extends CheckingAccount {
//    Checking Accounts should have:
//
//    A balance
//    A secretKey
//    A PrimaryOwner
//    An optional SecondaryOwner
//    A minimumBalance
//    A penaltyFee
//    A monthlyMaintenanceFee
//    A status (FROZEN, ACTIVE)
//
//    Savings are identical to Checking accounts except that they
//
//    Do NOT have a monthlyMaintenanceFee
//    Do have an interestRate

    private BigDecimal interestRate;

    public SavingsAccount() {
    }

    public SavingsAccount(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, Status status, Integer secretKey, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee, status, secretKey, minimumBalance, BigDecimal.ZERO);
        this.interestRate = interestRate;
    }


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
