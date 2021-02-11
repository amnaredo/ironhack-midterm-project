package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;

import java.util.List;

public interface IAccountController {

    List<Account> getAccounts();
    Account getAccount(Long id);
    List<Account>getAccountsByOwner(Long ownerId);

    CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long ownerId);
    CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long ownerId, Long otherOwnerId);

    SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long ownerId);
    SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long ownerId, Long otherOwnerId);

    CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long ownerId);
    CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long ownerId, Long otherOwnerId);

    Account transferMoney(String headerToken, MoneyTransferDTO moneyTransferDTO, Long id);

    void updateBalance(NewBalanceDTO newBalanceDTO, Long id);
}
