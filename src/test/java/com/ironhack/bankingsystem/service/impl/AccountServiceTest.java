package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.CheckingAccountRepository;
import com.ironhack.bankingsystem.repository.account.CreditCardAccountRepository;
import com.ironhack.bankingsystem.repository.account.SavingsAccountRepository;
import com.ironhack.bankingsystem.repository.account.StudentCheckingAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;

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

        checkingAccountRepository.save(checkingAccount);
        studentCheckingAccountRepository.save(studentCheckingAccount);
        savingsAccountRepository.save(savingsAccount);
        creditCardAccountRepository.save(creditCardAccount);
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        studentCheckingAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        creditCardAccountRepository.deleteAll();

        ownerService.deleteAll();
    }

    @Test
    void getAccounts() {
        List<Account> accountList = accountService.getAccounts();
        assertEquals(4, accountList.size());
    }

    @Test
    void addAccount() {
        AccountHolder owner = new AccountHolder("Pedro Perez", LocalDate.of(1959, 5, 6), new Address("Calle Dos", "Madrid", "28080"));
        ownerService.addOwner(owner);
        CheckingAccount account = new CheckingAccount(owner, new Money(BigDecimal.ZERO), 1234);

        accountService.addAccount(account);

        assertEquals(5, accountService.getAccounts().size());
    }

    @Test
    void addCheckingAccount() {
    }

    @Test
    void addStudentCheckingAccount() {
    }

    @Test
    void addSavingsAccount() {
    }

    @Test
    void addCreditCardAccount() {
    }
}