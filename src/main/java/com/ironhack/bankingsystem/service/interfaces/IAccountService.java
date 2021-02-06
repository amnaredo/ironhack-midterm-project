package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.account.Account;

import java.util.List;

public interface IAccountService {

    List<Account> getAccounts();
}
