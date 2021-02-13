package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.IInterestsFeesService;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
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
class InterestsFeesServiceTest {

    @Autowired
    private IInterestsFeesService service;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private OwnerRepository ownerRepository;


    @BeforeEach
    void setUp() {
        AccountHolder accountHolder =
                new AccountHolder(
                        "Alejandro Martínez",
                        LocalDate.of(1984, 4, 14),
                        new Address("Calle Velázquez 1", "Gijón", "33201"));

        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "elgooG");

        ownerRepository.saveAll(List.of(accountHolder, thirdPartyUser));

        CheckingAccount checkingAccount = new CheckingAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)), "4321");
        SavingsAccount savingsAccount = new SavingsAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        CreditCardAccount creditCardAccount = new CreditCardAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)));

        LocalDateTime oneMonthAgo = LocalDateTime.of(2021, 1, 8, 12, 0);
        LocalDateTime oneYearAgo = LocalDateTime.of(2020, 2, 8, 12, 0);
        checkingAccount.setMonthlyFeeAppliedDateTime(oneMonthAgo);
        studentCheckingAccount.setLastAccessDateTime(oneMonthAgo);
        savingsAccount.setInterestAddedDateTime(oneYearAgo);
        creditCardAccount.setInterestAddedDateTime(oneMonthAgo);
        creditCardAccount.setInterestRate(BigDecimal.valueOf(0.12));

        accountRepository.saveAll(List.of(checkingAccount, studentCheckingAccount, savingsAccount, creditCardAccount));

    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void applyInterestsFeesService() {
        List<Account> accountList = accountRepository.findAll();
        for(Account account : accountList) {
            service.applyInterestsFeesService(account);
        }

        CheckingAccount checkingAccount = (CheckingAccount) accountList.get(0);
        StudentCheckingAccount studentCheckingAccount = (StudentCheckingAccount) accountList.get(1);
        SavingsAccount savingsAccount = (SavingsAccount) accountList.get(2);
        CreditCardAccount creditCardAccount = (CreditCardAccount) accountList.get(3);

        assertEquals(BigDecimal.valueOf(1000 - checkingAccount.getMonthlyMaintenanceFee().getAmount().intValue()).longValueExact(),
                checkingAccount.getBalance().getAmount().longValueExact());
        assertEquals(BigDecimal.valueOf(1000).longValueExact(), studentCheckingAccount.getBalance().getAmount().longValueExact());
        assertTrue(BigDecimal.valueOf(1000).compareTo(creditCardAccount.getBalance().getAmount()) < 0);
        assertEquals(BigDecimal.valueOf(1002.5).setScale(2), savingsAccount.getBalance().getAmount());
    }
}