package com.ironhack.bankingsystem.repository.transaction;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    @Query("SELECT COUNT(*) FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) AND t.timestamp BETWEEN :initTime AND :endTime")
    Integer findCountBetweenPeriod(@Param("account") Account account, @Param("initTime")LocalDateTime initTime, @Param("endTime")LocalDateTime endTime);

    // Daily total amount of transactions from an account in desc order by total
    //@Query("SELECT t.date AS date, SUM(t.amount.amount) AS total FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) GROUP BY date ORDER BY total DESC")
    //List<Object[]> findDailyTotalByDateOrderedDesc(@Param("account") Account account);

    // Total amount of transactions in a determined date
    //@Query("SELECT t.date AS date, SUM(t.amount.amount) FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) GROUP BY date HAVING date = :date")
    //List<Object[]> findTotalInDate(@Param("account") Account account, @Param("date")String date);

    // Total amount of transactions within a determined period
    @Query("SELECT SUM(t.amount.amount) FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) AND t.timestamp BETWEEN :initTime AND :endTime")
    BigDecimal findTotalInPeriod(@Param("account") Account account, @Param("initTime")LocalDateTime initTime, @Param("endTime") LocalDateTime endTime);

    List<Transaction> findByFromAccountOrToAccountOrderByTimestampDesc(Account fromAccount, Account toAccount);
}
