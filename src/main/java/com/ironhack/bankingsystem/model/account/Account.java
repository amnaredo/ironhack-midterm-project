package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Account {

    // The penaltyFee for all accounts should be 40.
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(40L));


    private Money balance;
    private AccountHolder primaryOwner;
    private AccountHolder secondaryOwner;

    private LocalDateTime creationDateTime;
    private LocalDateTime lastAccessDateTime;


    public Account() {
        this.creationDateTime = LocalDateTime.now();
        this.balance = new Money(BigDecimal.ZERO);
    }

    public Account(AccountHolder primaryOwner) {
        this();
        this.primaryOwner = primaryOwner;
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this(primaryOwner);
        this.secondaryOwner = secondaryOwner;
    }

    public Account(AccountHolder primaryOwner, Money balance) {
        this(primaryOwner);
        this.balance = balance;
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance) {
        this(primaryOwner, secondaryOwner);
        this.balance = balance;
    }


    public Money getBalance() {
        setLastAccessDateTime(LocalDateTime.now());
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public final AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public final void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public final AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public final void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public final Money getPenaltyFee() {
        return PENALTY_FEE;
    }

    public final LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public final void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getLastAccessDateTime() {
        return lastAccessDateTime;
    }

    public void setLastAccessDateTime(LocalDateTime lastAccessDateTime) {
        this.lastAccessDateTime = lastAccessDateTime;
    }
}
