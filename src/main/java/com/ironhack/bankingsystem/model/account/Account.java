package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Account {

    private Money balance;
    private AccountHolder primaryOwner;
    private AccountHolder secondaryOwner;
    private BigDecimal penaltyFee;

    private LocalDate creationDate;

    public Account() {
        this.creationDate = LocalDate.now();
    }

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = penaltyFee;
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
