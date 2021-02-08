package com.ironhack.bankingsystem.dto.account;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class NewBalanceDTO {

    @Digits(integer = 6, fraction = 2/*, message = "Wrong amount format"*/)
    //@DecimalMin(value = "0"/*, message = "Amount must be non negative"*/)
    private BigDecimal balance;


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
