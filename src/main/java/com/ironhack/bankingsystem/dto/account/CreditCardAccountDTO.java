package com.ironhack.bankingsystem.dto.account;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class CreditCardAccountDTO extends AccountDTO {

    @Digits(integer = 1, fraction = 4)
    @DecimalMin("0.1")
    @DecimalMax("0.2")
    private BigDecimal interestRate = BigDecimal.valueOf(0.2);
    @Digits(integer = 6, fraction = 2)
    @DecimalMin("100")
    @DecimalMax("100000")
    private BigDecimal creditLimit = BigDecimal.valueOf(100.0);


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}
