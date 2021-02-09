package com.ironhack.bankingsystem.model.account.interfaces;

import com.ironhack.bankingsystem.model.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WithMonthlyFee {
    Integer getMonthsSinceLastMonthlyFeeDeduction();
    Money getMonthlyMaintenanceFee();
    LocalDateTime getMonthlyFeeAppliedDateTime();
    void updateMonthlyFeeAppliedDateTime();

}
