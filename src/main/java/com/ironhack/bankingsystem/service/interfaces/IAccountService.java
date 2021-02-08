package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.account.CheckingAccountDTO;
import com.ironhack.bankingsystem.dto.account.CreditCardAccountDTO;
import com.ironhack.bankingsystem.dto.account.SavingsAccountDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;

import java.util.List;
import java.util.Optional;

public interface IAccountService {

    List<Account> getAccounts();
    Account addAccount(Account account);

    CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long id, Optional<Long> otherId);
    SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long id, Optional<Long> otherId);
    CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long id, Optional<Long> otherId);

    void deleteAll();
}
