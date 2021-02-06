package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.user.AccountHolder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    // todo
    //    Interest on savings accounts is added to the account annually at the rate of specified interestRate per year.
    //    That means that if I have 1000000 in a savings account with a 0.01 interest rate,
    //    1% of 1 Million is added to my account after 1 year.
    //    When a savings Account balance is accessed, you must determine if it has been 1 year or more since the either
    //    the account was created or since interest was added to the account, and add the appropriate
    //    interest to the balance if necessary.


    private static final Double VALID_MIN_INTEREST_RATE = 0.;
    private static final Double VALID_MAX_INTEREST_RATE = 0.5;
    private static final Double VALID_MIN_MINIMUM_BALANCE = 100.;
    private static final Double VALID_MAX_MINIMUM_BALANCE = 1000.;

    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(0.0025);
    private static final Money DEFAULT_MINIMUM_BALANCE = new Money(BigDecimal.valueOf(VALID_MAX_MINIMUM_BALANCE));


    private BigDecimal interestRate;

    private LocalDateTime interestAddedDateTime;


    public SavingsAccount() {
        this.interestAddedDateTime = getCreationDateTime();
    }

    public SavingsAccount(AccountHolder owner, Money balance, Integer secretKey) {
        super(owner, balance, secretKey);
        setMonthlyMaintenanceFee(new Money(BigDecimal.ZERO));
        setMinimumBalance(DEFAULT_MINIMUM_BALANCE);
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.interestAddedDateTime = getCreationDateTime();
    }

    public SavingsAccount(AccountHolder owner, Money balance, Integer secretKey,
                          @DecimalMin(value = "0.0", message = "Interest rate must be non negative")
                          @DecimalMax(value = "0.5", message = "Interest rate must be lesser than 0.5")
                          Double interestRate,
                          @DecimalMin(value = "100", message = "Minimum balance must be greater than 100")
                          @DecimalMax(value = "1000", message = "Minimum balance must be lesser than 1000")
                          Double minimumBalance) {
        super(owner, balance, secretKey);
        setMonthlyMaintenanceFee(new Money(BigDecimal.ZERO));
        setMinimumBalance(new Money(BigDecimal.valueOf(minimumBalance)));
        this.interestRate = BigDecimal.valueOf(interestRate);
        this.interestAddedDateTime = getCreationDateTime();
    }


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
