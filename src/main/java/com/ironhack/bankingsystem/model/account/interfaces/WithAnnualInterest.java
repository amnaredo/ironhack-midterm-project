package com.ironhack.bankingsystem.model.account.interfaces;

import com.ironhack.bankingsystem.model.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WithAnnualInterest {
    Integer getYearsSinceLastInterestAdded();
    LocalDateTime getInterestAddedDateTime();
    Money getLastInterestGenerated();
    BigDecimal getAnnualInterestRate();
    void updateInterestAddedDateTime();
}
