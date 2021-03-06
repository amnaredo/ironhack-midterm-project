package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
//@Table(name = "student_checking_account")
@PrimaryKeyJoinColumn(name = "id")
public class StudentCheckingAccount extends CheckingAccount {

//    Student Checking Accounts are identical to Checking Accounts except that they do NOT have:
//
//    A monthlyMaintenanceFee
//    A minimumBalance

    public StudentCheckingAccount() {
        setMonthlyMaintenanceFee(new Money(BigDecimal.ZERO));
        setMinimumBalance(new Money(BigDecimal.ZERO));
        this.setType(Type.STUDENT_CHECKING);
    }

    public StudentCheckingAccount(Owner owner, Money balance, String secretKey) {
        super(owner, balance, secretKey);
        setMonthlyMaintenanceFee(new Money(BigDecimal.ZERO));
        setMinimumBalance(new Money(BigDecimal.ZERO));
        this.setType(Type.STUDENT_CHECKING);
    }
}
