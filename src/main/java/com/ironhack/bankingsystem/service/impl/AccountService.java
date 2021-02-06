package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
