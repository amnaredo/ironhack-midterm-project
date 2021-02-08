package com.ironhack.bankingsystem.dto.account;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class CheckingAccountDTO extends AccountDTO {

    @Pattern(regexp = "^[0-9]{4,8}$")
    private String secretKey;


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
