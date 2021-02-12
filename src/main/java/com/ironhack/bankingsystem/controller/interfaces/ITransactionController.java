package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.security.CustomUserDetails;

import java.util.List;

public interface ITransactionController {

    // GET /transactions
    List<Transaction> getTransactions();

    // GET /accounts/{id}/transactions
    List<Transaction> getTransactionsByAccount(CustomUserDetails userDetails, Long idAccount);
}
