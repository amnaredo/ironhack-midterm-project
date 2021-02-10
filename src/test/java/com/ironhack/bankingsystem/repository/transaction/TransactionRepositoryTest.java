package com.ironhack.bankingsystem.repository.transaction;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.model.account.StudentCheckingAccount;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryTest {

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

        CheckingAccount checkingAccount = new CheckingAccount(accountHolder, new Money(BigDecimal.valueOf(1000)), "1234");
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
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void findTransactionsFromAccountBetweenPeriod() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(5);

        Integer result = transactionRepository.findTransactionsFromAccountBetweenPeriod(
                ownerRepository.findAll().get(0).getPrimaryAccounts().get(0),
                startTime,
                endTime);
        assertEquals(3, result);
    }

    @Test
    void findTransactionsFromAccountBetweenPeriod_lastSecond() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(1);

        Integer result = transactionRepository.findTransactionsFromAccountBetweenPeriod(
                ownerRepository.findAll().get(0).getPrimaryAccounts().get(0),
                startTime,
                endTime);
        assertEquals(1, result);
    }
}