package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(Long idAccount) {
        if (!accountService.existsAccount(idAccount))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");

        Account account = accountService.getAccountById(idAccount);
        return transactionRepository.findByFromAccountOrToAccountOrderByTimestampDesc(account, account);
    }

    public Transaction addTransaction(Transaction transaction) {

        Transaction newTransaction = transactionRepository.saveAndFlush(transaction);

        if (transaction.getFromAccount() != null)
            accountService.saveAccount(transaction.getFromAccount());
        if (transaction.getToAccount() != null)
            accountService.saveAccount(transaction.getToAccount());

        return newTransaction;
    }

    public void deleteAll() {
        transactionRepository.deleteAll();
    }
}
