package com.ironhack.bankingsystem.model.account.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WithMonthlyInterest {
    Integer getMonthsSinceLastInterestAdded();
    LocalDateTime getInterestAddedDateTime();
    BigDecimal getLastInterestGenerated();
    BigDecimal getMonthlyInterest();
    void updateInterestAddedDateTime();
}
