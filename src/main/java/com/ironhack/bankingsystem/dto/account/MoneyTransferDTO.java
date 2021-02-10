package com.ironhack.bankingsystem.dto.account;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class MoneyTransferDTO {

    //private Integer fromAccountId;
    @NumberFormat/*(message = "Account ID is required")*/
    @Min(1)
    private Long toAccountId;

    @Digits(integer = 6, fraction = 2/*, message = "Wrong amount format"*/)
    @DecimalMin(value = "0"/*, message = "Amount must be non negative"*/)
    private BigDecimal amount;

    @NotBlank/*(message = "Name is required")*/
    private String name;
    private String description;

    private String secretKey; // third party users need this


    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
