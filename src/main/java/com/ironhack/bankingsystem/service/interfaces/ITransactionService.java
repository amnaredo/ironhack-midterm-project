package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.security.CustomUserDetails;

import java.util.List;

public interface ITransactionService {

    List<Transaction> getTransactions();
    List<Transaction> getTransactionsByAccount(CustomUserDetails userDetails, Long idAccount);
    Transaction addTransaction(Transaction transaction);
}
