package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.user.AccountHolder;

import java.math.BigDecimal;

public class CheckingAccount extends Account {
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

    // Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12
    private final static Money MINIMUM_BALANCE = new Money(BigDecimal.valueOf(250L));
    private final static Money MONTHLY_MAINTENANCE_FEE = new Money(BigDecimal.valueOf(12L));

    private Integer secretKey; // todo ?
    private Money minimumBalance;
    private Money monthlyMaintenanceFee;
    private Status status;


    public CheckingAccount() {
    }

    // Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12
    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Integer secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.minimumBalance = MINIMUM_BALANCE;
        this.monthlyMaintenanceFee = MONTHLY_MAINTENANCE_FEE;
        this.status = Status.ACTIVE;
    }

    public Integer getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(Integer secretKey) {
        this.secretKey = secretKey;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    protected void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    protected void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
