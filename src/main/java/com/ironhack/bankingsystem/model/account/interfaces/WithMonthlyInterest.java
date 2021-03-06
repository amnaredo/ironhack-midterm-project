package com.ironhack.bankingsystem.model.account.interfaces;

import com.ironhack.bankingsystem.model.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WithMonthlyInterest {
    Integer getMonthsSinceLastInterestAdded();
    LocalDateTime getInterestAddedDateTime();
    Money getLastInterestGenerated();
    BigDecimal getMonthlyInterestRate();
    void updateInterestAddedDateTime();
}
