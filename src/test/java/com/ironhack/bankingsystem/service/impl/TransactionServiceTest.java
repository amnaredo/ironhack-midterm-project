package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder = new AccountHolder("Alejandro Martínez", LocalDate.of(1984, 4, 14), new Address("Calle Corrida", "Gijón", "33201"));
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "Hola");

        ownerService.addOwner(accountHolder);
        ownerService.addOwner(thirdPartyUser);

        CheckingAccount checkingAccount = new CheckingAccount(thirdPartyUser, new Money(BigDecimal.TEN), 1234);
        StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(accountHolder, new Money(BigDecimal.ONE), 4321);
        SavingsAccount savingsAccount = new SavingsAccount(thirdPartyUser, new Money(BigDecimal.TEN), 1234);
        CreditCardAccount creditCardAccount = new CreditCardAccount(accountHolder, new Money(BigDecimal.ZERO));

        accountService.addAccount(checkingAccount);
        accountService.addAccount(studentCheckingAccount);
        accountService.addAccount(savingsAccount);
        accountService.addAccount(creditCardAccount);

        Transaction transaction = new Transaction(checkingAccount, studentCheckingAccount, new Money(BigDecimal.valueOf(5L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionRepository.save(transaction);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getTransactions() {
        Collection<Transaction> transactions = transactionService.getTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    void addTransaction() {
        List<Owner> owners = ownerService.getOwners();
        Account fromAccount = owners.get(0).getPrimaryAccounts().get(0);
        Account toAccount = owners.get(1).getPrimaryAccounts().get(0);

        Transaction transaction = new Transaction(fromAccount, toAccount, new Money(BigDecimal.valueOf(5L)), "Alejandro", "Hola amigo");
        transactionService.addTransaction(transaction);
        assertEquals(2, transactionService.getTransactions().size());
    }
}