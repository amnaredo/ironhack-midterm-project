package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.security.CustomUserDetails;

import java.util.List;

public interface IAccountController {

    // GET /accounts
    List<Account> getAccounts();
    // GET /owners/{id}/accounts
    List<Account>getAccountsByOwner(Long ownerId);

    // POST /accounts/checking/{id}
    CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long ownerId);
    // POST /accounts/checking/{id1}/{id2}
    CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long ownerId, Long otherOwnerId);

    // POST /accounts/savings/{id}
    SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long ownerId);
    // POST /accounts/savings/{id1}/{id2}
    SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long ownerId, Long otherOwnerId);

    // POST /accounts/creditcard/{id}
    CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long ownerId);
    // POST /accounts/creditcard/{id1}/{id2}
    CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long ownerId, Long otherOwnerId);


    // GET /accounts/{id} - authenticated user
    Account getAccount(CustomUserDetails userDetails, Long id);
    // POST /accounts/{id} - authenticated user
    Account transferMoney(CustomUserDetails userDetails, String headerToken, MoneyTransferDTO moneyTransferDTO, Long id);

    // PATCH /accounts/{id} - admin only
    void updateBalance(NewBalanceDTO newBalanceDTO, Long id);
}
