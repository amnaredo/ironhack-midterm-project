package com.ironhack.bankingsystem.dto.account;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class AccountDTO {

//    @NotBlank(message = "Type is required")
//    private String type;

    @Digits(integer = 6, fraction = 2/*, message = "Wrong balance format"*/)
    @DecimalMin(value = "0"/*, message = "Balance must be non negative"*/)
    private BigDecimal balance;


//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
