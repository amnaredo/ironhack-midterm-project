package com.ironhack.bankingsystem.repository.transaction;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
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
    void findCountBetweenPeriod() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(5);

        Integer result = transactionRepository.findCountBetweenPeriod(
                ownerRepository.findAll().get(0).getPrimaryAccounts().get(0),
                startTime,
                endTime);
        assertEquals(3, result);
    }

    @Test
    void findCountBetweenPeriod_lastSecond() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusSeconds(1);

        Integer result = transactionRepository.findCountBetweenPeriod(
                ownerRepository.findAll().get(0).getPrimaryAccounts().get(0),
                startTime,
                endTime);
        assertEquals(1, result);
    }

    @Test
    void findHighestDailyTotal() {
        List<Object[]> result =
                transactionRepository.findDailyTotalByDateOrderedDesc(ownerRepository.findAll().get(0).getPrimaryAccounts().get(0));
//        for (Object[] objArr : result)
//            System.out.println(objArr[0].toString() +  " " + objArr[1].toString());
//        2021-02-10 300.00
//        2021-02-08 200.00
//        2021-02-07 100.00
        assertEquals(LocalDateTime.now().format(Transaction.DATE_FORMATTER), result.get(0)[0]);
        assertEquals(new BigDecimal("300.00"), result.get(0)[1]);
        assertEquals(LocalDateTime.now().minusDays(2).format(Transaction.DATE_FORMATTER), result.get(1)[0]);
        assertEquals(new BigDecimal("200.00"), result.get(1)[1]);
        assertEquals(LocalDateTime.now().minusDays(3).format(Transaction.DATE_FORMATTER), result.get(2)[0]);
        assertEquals(new BigDecimal("100.00"), result.get(2)[1]);
    }

    @Test
    void findTotalInDate() {
        List<Object[]> result = transactionRepository.findTotalInDate(ownerRepository.findAll().get(0).getPrimaryAccounts().get(0),
                LocalDateTime.now().minusDays(3).format(Transaction.DATE_FORMATTER));
//        for (Object[] objArr : result)
//            System.out.println(objArr[0].toString() +  " " + objArr[1].toString());
        assertEquals(LocalDateTime.now().minusDays(3).format(Transaction.DATE_FORMATTER), result.get(0)[0]);
        assertEquals(new BigDecimal("100.00"), result.get(0)[1]);
    }
}