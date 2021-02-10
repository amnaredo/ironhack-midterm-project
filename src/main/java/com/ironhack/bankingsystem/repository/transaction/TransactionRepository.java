package com.ironhack.bankingsystem.repository.transaction;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    Fraud Detection
//
//    The application must recognise patterns that indicate fraud and Freeze the account status when potential fraud is detected.
//
//    Patterns that indicate fraud include:
//
//    - Transactions made in 24 hours that total to more than 150% of the customers highest daily total transactions in any other 24 hour period.
//    - More than 2 transactions occurring on a single account within a 1 second period.

    // Number of transactions occurred on a single account within a determined period
    @Query("SELECT COUNT(*) FROM Transaction t WHERE t.fromAccount = :account AND t.timestamp BETWEEN :initTime AND :endTime")
    Integer findTransactionsFromAccountBetweenPeriod(@Param("account") Account account, @Param("initTime")LocalDateTime initTime, @Param("endTime")LocalDateTime endTime);
}
