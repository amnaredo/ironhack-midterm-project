package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService service;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder = new AccountHolder("Alejandro Martínez", LocalDate.of(1984, 4, 14), new Address("Calle Corrida", "Gijón", "33201"));
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "Hola");

        ownerRepository.saveAll(List.of(accountHolder, thirdPartyUser));

        CheckingAccount checkingAccount = new CheckingAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000)), "1234");
        StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(accountHolder, new Money(BigDecimal.valueOf(1000)), "4321");
        SavingsAccount savingsAccount = new SavingsAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000)), "1234");
        CreditCardAccount creditCardAccount = new CreditCardAccount(accountHolder, new Money(BigDecimal.valueOf(1000)));

        accountRepository.saveAll(List.of(checkingAccount, studentCheckingAccount, savingsAccount, creditCardAccount));

        Transaction transaction = new Transaction(checkingAccount, studentCheckingAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionRepository.save(transaction);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void getTransactions() {
        Collection<Transaction> transactions = service.getTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    void addTransaction() {
        List<Owner> owners = ownerRepository.findAll();
        Account fromAccount = owners.get(0).getPrimaryAccounts().get(0);
        Account toAccount = owners.get(1).getPrimaryAccounts().get(0);

        BigDecimal fromBalance = fromAccount.getBalance().getAmount();
        BigDecimal toBalance = toAccount.getBalance().getAmount();

        Transaction transaction = new Transaction(fromAccount, toAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro", "Hola amigo");
        service.addTransaction(transaction);
        assertEquals(2, service.getTransactions().size());
        assertEquals(fromBalance.intValue() - 100, fromAccount.getBalance().getAmount().intValue());
        assertEquals(toBalance.intValue() + 100, toAccount.getBalance().getAmount().intValue());
    }
}