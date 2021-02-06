package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.model.account.Account;

import java.util.List;

public interface IAccountController {

    List<Account> getAccounts();
}
