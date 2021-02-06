package com.ironhack.bankingsystem.model.transaction;

import com.ironhack.bankingsystem.model.account.Account;

import java.time.LocalDateTime;

public class Transaction {

    private Account fromAccount;
    private Account toAccount;

    private LocalDateTime timestamp;
    private String authorName;

}
