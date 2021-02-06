package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Account {

    // The penaltyFee for all accounts should be 40.
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(40L));

    private Money balance;
    private AccountHolder primaryOwner;
    private AccountHolder secondaryOwner;

    private LocalDateTime creationDateTime;


    public Account() {
        this.creationDateTime = LocalDateTime.now();
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
}
