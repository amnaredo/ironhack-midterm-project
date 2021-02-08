package com.ironhack.bankingsystem.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class MoneyTransferDTO {

    //private Integer fromAccountId;
    @NotBlank(message = "Account ID is required")
    @Min(1)
    private Integer toAccount;

    @Digits(integer = 6, fraction = 2, message = "Wrong amount format")
    @DecimalMin(value = "0", message = "Amount must be positive")
    private BigDecimal amount;
    private String currency;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;

    public Integer getToAccount() {
        return toAccount;
    }

    public void setToAccount(Integer toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
}
