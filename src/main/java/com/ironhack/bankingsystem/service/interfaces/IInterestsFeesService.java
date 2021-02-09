package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyFee;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyInterest;

public interface IInterestsFeesService {
    Account applyInterestsFeesService(Account account);

    void applyMonthlyInterest(WithMonthlyInterest account);
    void applyMonthlyFee(WithMonthlyFee account);
}
