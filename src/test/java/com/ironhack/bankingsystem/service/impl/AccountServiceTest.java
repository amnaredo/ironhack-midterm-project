package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.CheckingAccountDTO;
import com.ironhack.bankingsystem.dto.account.CreditCardAccountDTO;
import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.dto.account.SavingsAccountDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder =
                new AccountHolder(
                        "Alejandro Martínez",
                        LocalDate.of(1984, 4, 14),
                        new Address("Calle Velázquez 1", "Gijón", "33201"));

        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "elgooG");

        ownerService.addOwner(accountHolder);
        ownerService.addOwner(thirdPartyUser);

        CheckingAccount checkingAccount = new CheckingAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)), "4321");
        SavingsAccount savingsAccount = new SavingsAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        CreditCardAccount creditCardAccount = new CreditCardAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)));

        checkingAccountRepository.save(checkingAccount);
        studentCheckingAccountRepository.save(studentCheckingAccount);
        savingsAccountRepository.save(savingsAccount);
        creditCardAccountRepository.save(creditCardAccount);
    }

    @AfterEach
    void tearDown() {

        transactionRepository.deleteAll();

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
    void existsAccount() {
        Account account = accountService.getAccounts().get(0).getPrimaryOwner().getPrimaryAccounts().get(0);
        Long accountId = account.getId();

        assertTrue(accountService.existsAccount(accountId));
        assertTrue(accountService.existsAccount(accountId + 1));
        assertFalse(accountService.existsAccount(accountId - 1));
    }

    @Test
    void getAccountById() {

        Account account = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(0);
        Long accountId = account.getId();

        Account accountFound = accountService.getAccountById(accountId);
        assertTrue(accountFound.getPrimaryOwner().getName().equalsIgnoreCase("Alejandro Martínez"));
    }

    @Test
    void addAccount() {
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        ownerService.addOwner(owner);
        CheckingAccount account = new CheckingAccount(owner, new Money(BigDecimal.ZERO), "1234");

        accountService.addAccount(account);

        assertEquals(5, accountService.getAccounts().size());
    }

    @Test
    void addChecking() {
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        Owner primaryOwner = ownerService.addOwner(owner);

        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setBalance(BigDecimal.ZERO);
        checkingAccountDTO.setSecretKey("secretkey");
        accountService.addChecking(checkingAccountDTO, owner.getId(), Optional.empty());

        List<Account> accounts = accountRepository.findByPrimaryOwner(primaryOwner);
        assertEquals(1, accounts.size());
        assertEquals("secretkey", ((CheckingAccount)accounts.get(accounts.size()-1)).getSecretKey());
    }

    @Test
    void addSavings() {
        Owner primaryOwner = ownerService.getOwners().get(0);
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        Owner secondaryOwner = ownerService.addOwner(owner);

        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO();
        savingsAccountDTO.setBalance(BigDecimal.valueOf(1000L));
        savingsAccountDTO.setSecretKey("keysecret");
        savingsAccountDTO.setInterestRate(BigDecimal.valueOf(0.0025));
        savingsAccountDTO.setMinBalance(BigDecimal.valueOf(500));
        accountService.addSavings(savingsAccountDTO, primaryOwner.getId(), Optional.of(secondaryOwner.getId()));

        List<Account> accounts = accountRepository.findByPrimaryOwner(primaryOwner);
        List<Account> moreAccounts = accountRepository.findBySecondaryOwner(secondaryOwner);

        assertEquals(3, accounts.size());
        assertEquals(1, moreAccounts.size());

        assertEquals("keysecret", ((SavingsAccount)accounts.get(accounts.size()-1)).getSecretKey());
        assertEquals("keysecret", ((SavingsAccount)moreAccounts.get(0)).getSecretKey());
        assertEquals(secondaryOwner.getName(), accounts.get(accounts.size()-1).getSecondaryOwner().getName());
        assertEquals(primaryOwner.getName(), moreAccounts.get(0).getPrimaryOwner().getName());
    }

    @Test
    void addCreditCard() {
        Owner primaryOwner = ownerService.getOwners().get(1);
        Owner secondaryOwner = ownerService.getOwners().get(0);

        CreditCardAccountDTO creditCardAccountDTO = new CreditCardAccountDTO();
        creditCardAccountDTO.setBalance(BigDecimal.valueOf(1234L));
        creditCardAccountDTO.setInterestRate(BigDecimal.valueOf(0.125));
        creditCardAccountDTO.setCreditLimit(BigDecimal.valueOf(2121));
        accountService.addCreditCard(creditCardAccountDTO, primaryOwner.getId(), Optional.of(secondaryOwner.getId()));

        List<Account> accounts = accountRepository.findByPrimaryOwner(primaryOwner);
        List<Account> moreAccounts = accountRepository.findBySecondaryOwner(secondaryOwner);

        assertEquals(3, accounts.size());
        assertEquals(1, moreAccounts.size());

        assertEquals(new BigDecimal("1234.00"), accounts.get(accounts.size()-1).getBalance().getAmount());
        assertEquals(new BigDecimal("2121.00"), ((CreditCardAccount)moreAccounts.get(0)).getCreditLimit().getAmount());
        assertEquals(secondaryOwner.getName(), accounts.get(accounts.size()-1).getSecondaryOwner().getName());
    }

    @Test
    void startMoneyTransfer() {

        Account origin = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(0);
        Long originId = origin.getId();
        Account destination = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(1);
        Long destinationId = destination.getId();

        BigDecimal amount = BigDecimal.valueOf(90L);
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(amount);
        moneyTransferDTO.setName("Alejandro Martínez");
        moneyTransferDTO.setDescription("money transfer test");
        moneyTransferDTO.setToAccount(destinationId);
        accountService.startMoneyTransfer(moneyTransferDTO, originId);

        origin = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(0);
        destination = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(1);

        BigDecimal diff = destination.getBalance().getAmount().subtract(origin.getBalance().getAmount()).abs();
        assertEquals(amount.multiply(BigDecimal.valueOf(2L)).setScale(2), diff);

   }

    @Test
    void updateBalance() {
    }
}