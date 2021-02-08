package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
//@Table(name = "credit_card_account")
@PrimaryKeyJoinColumn(name = "id")
public class CreditCardAccount extends Account {

//    CreditCard Accounts have:
//
//    A balance
//    A PrimaryOwner
//    An optional SecondaryOwner
//    A creditLimit
//    An interestRate
//    A penaltyFee

    // todo
    //    Interest on credit cards is added to the balance monthly.
    //    If you have a 12% interest rate (0.12) then 1% interest will be added to the account monthly.
    //    When the balance of a credit card is accessed, check to determine if it has been 1 month or more since
    //    the account was created or since interested was added, and if so, add the appropriate interest to the balance.

    private static final Double VALID_MIN_CREDIT_LIMIT = 100.0;
    private static final Double VALID_MAX_CREDIT_LIMIT = 100000.0;
    private static final Double VALID_MIN_INTEREST_RATE = 0.1;
    private static final Double VALID_MAX_INTEREST_RATE = 0.2;

    private static final Money DEFAULT_CREDIT_LIMIT = new Money(BigDecimal.valueOf(VALID_MIN_CREDIT_LIMIT));
    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(VALID_MAX_INTEREST_RATE);


    private Money creditLimit;
    private BigDecimal interestRate;

    private LocalDateTime interestAddedDateTime;


    public CreditCardAccount() {
        this.setType(Type.CREDIT_CARD);
        this.interestAddedDateTime = getCreationDateTime();
    }

    public CreditCardAccount(Owner owner, Money balance) {
        super(owner, balance);
        this.creditLimit = DEFAULT_CREDIT_LIMIT;  // CreditCard accounts have a default creditLimit of 100
        this.interestRate = DEFAULT_INTEREST_RATE;// CreditCards have a default interestRate of 0.2
        this.setType(Type.CREDIT_CARD);
        this.interestAddedDateTime = getCreationDateTime();
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
        this.creditLimit = new Money(BigDecimal.valueOf(creditLimit));
        this.interestRate = BigDecimal.valueOf(interestRate);
        this.setType(Type.CREDIT_CARD);
        this.interestAddedDateTime = getCreationDateTime();
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
}
