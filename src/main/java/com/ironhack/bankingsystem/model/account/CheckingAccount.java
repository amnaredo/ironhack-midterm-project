package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyFee;
import com.ironhack.bankingsystem.model.account.interfaces.WithSecretKey;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
//@Table(name = "checking_account")
@PrimaryKeyJoinColumn(name = "id")
public class CheckingAccount extends Account implements WithMonthlyFee, WithSecretKey {
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


    private String secretKey;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "min_balance_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "min_balance_amount"))
    })
    private Money minimumBalance;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_fee_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_fee_amount"))
    })
    private Money monthlyMaintenanceFee;
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime monthlyFeeAppliedDateTime;


    public CheckingAccount() {
        setMinimumBalance(MINIMUM_BALANCE);
        setMonthlyMaintenanceFee(MONTHLY_MAINTENANCE_FEE);
        setStatus(Status.ACTIVE);
        setType(Type.CHECKING);
        setMonthlyFeeAppliedDateTime(getCreationDateTime());
    }

    // Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12
    public CheckingAccount(Owner owner, Money balance,
                           @Pattern(regexp = "^[0-9]{4,8}$")
                           String secretKey) {
        super(owner, balance);
        setSecretKey(secretKey);
        setMinimumBalance(MINIMUM_BALANCE);
        setMonthlyMaintenanceFee(MONTHLY_MAINTENANCE_FEE);
        setStatus(Status.ACTIVE);
        setType(Type.CHECKING);
        setMonthlyFeeAppliedDateTime(getCreationDateTime());
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
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

    public LocalDateTime getMonthlyFeeAppliedDateTime() {
        return monthlyFeeAppliedDateTime;
    }

    public void updateMonthlyFeeAppliedDateTime() {
        setMonthlyFeeAppliedDateTime(LocalDateTime.now());
    }

    public void setMonthlyFeeAppliedDateTime(LocalDateTime monthlyFeeAppliedDateTime) {
        this.monthlyFeeAppliedDateTime = monthlyFeeAppliedDateTime;
    }

    public Integer getMonthsSinceLastMonthlyFeeDeduction() {
        long months = ChronoUnit.MONTHS.between(getMonthlyFeeAppliedDateTime(), LocalDateTime.now());
        return (int) months;
    }
}
