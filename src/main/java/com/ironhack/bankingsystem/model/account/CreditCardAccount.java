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

    private static final Money DEFAULT_CREDIT_LIMIT = new Money(BigDecimal.valueOf(100L));
    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(0.2);


    private Money creditLimit;
    private BigDecimal interestRate;



    public CreditCardAccount() {
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = DEFAULT_CREDIT_LIMIT;    //CreditCard accounts have a default creditLimit of 100
        this.interestRate = DEFAULT_INTEREST_RATE;// CreditCards have a default interestRate of 0.2
    }

    // CreditCards may be instantiated with a creditLimit higher than 100 but not higher than 100000
    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = creditLimit;
    }

    // CreditCards may be instantiated with an interestRate less than 0.2 but not lower than 0.1
    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.interestRate = interestRate;
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
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
}
