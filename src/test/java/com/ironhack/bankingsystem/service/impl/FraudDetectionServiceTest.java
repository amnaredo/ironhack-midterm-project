package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IFraudDetectionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FraudDetectionServiceTest {

    @Autowired
    private IFraudDetectionService service;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OwnerRepository ownerRepository;


    @BeforeEach
    void setUp() {
        AccountHolder accountHolder = new AccountHolder("Alejandro Martínez", LocalDate.of(1984, 4, 14), new Address("Calle Corrida", "Gijón", "33201"));
        ownerRepository.save(accountHolder);

        CheckingAccount checkingAccount = new CheckingAccount(accountHolder, new Money(BigDecimal.valueOf(100000)), "1234");
        SavingsAccount savingsAccount = new SavingsAccount(accountHolder, new Money(BigDecimal.valueOf(1000)), "1234");
        accountRepository.saveAll(List.of(checkingAccount, savingsAccount));

        Transaction transaction2 = new Transaction(checkingAccount, savingsAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transaction2.setTimestamp(LocalDateTime.now().minusSeconds(2));
        transactionRepository.save(transaction2);

        Transaction transaction1 = new Transaction(checkingAccount, savingsAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transaction1.setTimestamp(LocalDateTime.now().minusSeconds(1));
        transactionRepository.save(transaction1);

        Transaction transaction = new Transaction(checkingAccount, savingsAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionRepository.save(transaction);


        Transaction transactionThreeDaysAgo = new Transaction(checkingAccount, savingsAccount, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionThreeDaysAgo.setTimestamp(LocalDateTime.now().minusDays(3));
        transactionRepository.save(transactionThreeDaysAgo);

        Transaction transactionTwoDaysAgo = new Transaction(checkingAccount, savingsAccount, new Money(BigDecimal.valueOf(200L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionTwoDaysAgo.setTimestamp(LocalDateTime.now().minusDays(2));
        transactionRepository.save(transactionTwoDaysAgo);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void checkMoneyTransferV2() {
        Account account = ownerRepository.findAll().get(0).getPrimaryAccounts().get(0);

        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(BigDecimal.valueOf(500));

        assertThrows(ResponseStatusException.class, () -> {
            service.checkMoneyTransferV2(account, moneyTransferDTO);
        });
        assertEquals(Status.FROZEN, ((WithStatus)account).getStatus());
    }

    @Test
    void checkMoneyTransferV2_tooManyTransactions() {
        Account account = ownerRepository.findAll().get(0).getPrimaryAccounts().get(0);

        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(BigDecimal.valueOf(100));

        Transaction transactionA = new Transaction(account, account, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        Transaction transactionB = new Transaction(account, account, new Money(BigDecimal.valueOf(100L)), "Alejandro Martínez Naredo", "Esto es una prueba");
        transactionA.setTimestamp(LocalDateTime.now());
        transactionB.setTimestamp(LocalDateTime.now());
        transactionRepository.saveAll(List.of(transactionA, transactionB));

        assertThrows(ResponseStatusException.class, () -> {
            service.checkMoneyTransferV2(account, moneyTransferDTO);
        });
        assertEquals(Status.FROZEN, ((WithStatus)account).getStatus());
    }
}