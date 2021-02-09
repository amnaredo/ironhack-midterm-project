package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyInterest;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
//@Table(name = "credit_card_account")
@PrimaryKeyJoinColumn(name = "id")
public class CreditCardAccount extends Account implements WithMonthlyInterest {

//    CreditCard Accounts have:
//
//    A balance
//    A PrimaryOwner
//    An optional SecondaryOwner
//    A creditLimit
//    An interestRate
//    A penaltyFee

    private static final Double VALID_MIN_CREDIT_LIMIT = 100.0;
    private static final Double VALID_MAX_CREDIT_LIMIT = 100000.0;
    private static final Double VALID_MIN_INTEREST_RATE = 0.1;
    private static final Double VALID_MAX_INTEREST_RATE = 0.2;

    private static final Money DEFAULT_CREDIT_LIMIT = new Money(BigDecimal.valueOf(VALID_MIN_CREDIT_LIMIT));
    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(VALID_MAX_INTEREST_RATE);

    private Money creditLimit;
    @Column(precision = 19, scale = 4, columnDefinition="DECIMAL(19,4)")
    private BigDecimal interestRate;

    private LocalDateTime interestAddedDateTime;


    public CreditCardAccount() {
        this.setType(Type.CREDIT_CARD);
        setCreditLimit(DEFAULT_CREDIT_LIMIT);  // CreditCard accounts have a default creditLimit of 100
        setInterestRate(DEFAULT_INTEREST_RATE);// CreditCards have a default interestRate of 0.2
        setInterestAddedDateTime(getCreationDateTime());
    }

    public CreditCardAccount(Owner owner, Money balance) {
        super(owner, balance);
        setCreditLimit(DEFAULT_CREDIT_LIMIT);  // CreditCard accounts have a default creditLimit of 100
        setInterestRate(DEFAULT_INTEREST_RATE);// CreditCards have a default interestRate of 0.2
        setType(Type.CREDIT_CARD);
        setInterestAddedDateTime(getCreationDateTime());
    }

    // CreditCards may be instantiated with a creditLimit higher than 100 but not higher than 100000
    // CreditCards may be instantiated with an interestRate less than 0.2 but not lower than 0.1
    public CreditCardAccount(Owner owner, Money balance,
                             @DecimalMin(value = "100.0", message = "Credit limit must be greater than 100")
                             @DecimalMax(value = "100000.0", message = "Credit limit must be lesser than 100000")
                             Double creditLimit,
                             @DecimalMin(value = "0.1", message = "Interest rate must be greater than 0.1")
                             @DecimalMax(value = "0.2", message = "Interest rate must be lesser than 0.2")
                             Double interestRate) {
        super(owner, balance);
        setCreditLimit(new Money(BigDecimal.valueOf(creditLimit)));
        setInterestRate(BigDecimal.valueOf(interestRate));
        setType(Type.CREDIT_CARD);
        setInterestAddedDateTime(getCreationDateTime());
    }


    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Money getMinimumBalance() {
        return new Money(BigDecimal.ZERO);
    }

    public LocalDateTime getInterestAddedDateTime() {
        return interestAddedDateTime;
    }

    public Money getLastInterestGenerated() {
        BigDecimal earnedInterests = BigDecimal.ZERO;
        BigDecimal interest = getMonthlyInterestRate();
        for(int i=0; i<getMonthsSinceLastInterestAdded(); i++) {
            earnedInterests = earnedInterests.add((getBalance().getAmount()).multiply(interest));
        }
        return new Money(earnedInterests);
    }

    public BigDecimal getMonthlyInterestRate() {
        return getInterestRate().divide(BigDecimal.valueOf(12L), RoundingMode.HALF_EVEN);
    }

    public void updateInterestAddedDateTime() {
        setInterestAddedDateTime(LocalDateTime.now());
    }

    public void setInterestAddedDateTime(LocalDateTime interestAddedDateTime) {
        this.interestAddedDateTime = interestAddedDateTime;
    }

    public Integer getMonthsSinceLastInterestAdded() {
        long months = ChronoUnit.MONTHS.between(getInterestAddedDateTime(), LocalDateTime.now());
        return (int) months;
    }

    //    Interest on credit cards is added to the balance monthly.
    //    If you have a 12% interest rate (0.12) then 1% interest will be added to the account monthly.
    //    When the balance of a credit card is accessed, check to determine if it has been 1 month or more since
    //    the account was created or since interested was added, and if so, add the appropriate interest to the balance.

}
