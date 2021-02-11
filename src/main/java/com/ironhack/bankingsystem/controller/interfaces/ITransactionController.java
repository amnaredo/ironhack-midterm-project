package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.model.transaction.Transaction;

import java.util.List;

public interface ITransactionController {

    List<Transaction> getTransactions();
    List<Transaction> getTransactionsByAccount(Long idAccount);
}
