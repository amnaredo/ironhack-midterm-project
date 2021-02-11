package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.transaction.Transaction;

import java.util.List;

public interface ITransactionService {

    List<Transaction> getTransactions();
    List<Transaction> getTransactionsByAccount(Long idAccount);
    Transaction addTransaction(Transaction transaction);
    void deleteAll();
}
