package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;

public class CreditCardAccount extends Account {

//    CreditCard Accounts have:
//
//    A balance
//    A PrimaryOwner
//    An optional SecondaryOwner
//    A creditLimit
//    An interestRate
//    A penaltyFee

    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    public CreditCardAccount() {
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, BigDecimal creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, penaltyFee);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
