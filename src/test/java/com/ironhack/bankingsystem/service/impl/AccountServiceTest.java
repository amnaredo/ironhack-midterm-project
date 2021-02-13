package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService service;

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
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder =
                new AccountHolder(
                        "Alejandro Martínez",
                        LocalDate.of(1984, 4, 14),
                        new Address("Calle Velázquez 1", "Gijón", "33201"));
        accountHolder.setUsername("username1");
        accountHolder.setPassword("password");

        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "elgooG");
        thirdPartyUser.setUsername("username2");
        thirdPartyUser.setPassword("password");

        ownerRepository.save(accountHolder);
        ownerRepository.save(thirdPartyUser);

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

        ownerRepository.deleteAll();
    }

    @Test
    void getAccounts() {
        List<Account> accountList = service.getAccounts();
        assertEquals(4, accountList.size());
    }

    @Test
    void existsAccount() {
        Account account = service.getAccounts().get(0).getPrimaryOwner().getPrimaryAccounts().get(0);
        Long accountId = account.getId();

        assertTrue(service.existsAccount(accountId));
        assertTrue(service.existsAccount(accountId + 1));
        assertFalse(service.existsAccount(accountId - 1));
    }

    @Test
    void getAccountById() {

        Account account = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(0);
        Long accountId = account.getId();

        Account accountFound = service.getAccountById(accountId);
        assertTrue(accountFound.getPrimaryOwner().getName().equalsIgnoreCase("Alejandro Martínez"));
    }

    @Test
    void addAccount() {
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        ownerRepository.save(owner);
        CheckingAccount account = new CheckingAccount(owner, new Money(BigDecimal.ZERO), "1234");

        service.addAccount(account);

        assertEquals(5, service.getAccounts().size());
    }

    @Test
    void addChecking() {
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        Owner primaryOwner = ownerRepository.save(owner);

        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setBalance(BigDecimal.ZERO);
        checkingAccountDTO.setSecretKey("secretkey");
        service.addChecking(checkingAccountDTO, owner.getId(), Optional.empty());

        List<Account> accounts = accountRepository.findByPrimaryOwner(primaryOwner);
        assertEquals(1, accounts.size());
        assertEquals("secretkey", ((CheckingAccount)accounts.get(accounts.size()-1)).getSecretKey());
    }

    @Test
    void addChecking_ownerUnder24_studentsChecking() {
        AccountHolder owner = new AccountHolder(
                "Jaimito",
                LocalDate.of(2010, 1, 1),
                new Address("Calle Mayor", "Barcelona", "12345"));
        Owner primaryOwner = ownerRepository.save(owner);

        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setBalance(BigDecimal.ZERO);
        checkingAccountDTO.setSecretKey("secretkey");
        service.addChecking(checkingAccountDTO, owner.getId(), Optional.empty());

        List<Account> accounts = accountRepository.findByPrimaryOwner(primaryOwner);
        assertEquals(1, accounts.size());
        assertEquals("secretkey", ((CheckingAccount)accounts.get(accounts.size()-1)).getSecretKey());
        assertEquals(Type.STUDENT_CHECKING, accounts.get(accounts.size()-1).getType());
    }

    @Test
    void addSavings() {
        Owner primaryOwner = ownerRepository.findAll().get(0);
        AccountHolder owner = new AccountHolder(
                "Pedro Perez",
                LocalDate.of(1959, 5, 6),
                new Address("Calle Dos", "Madrid", "28080"));
        Owner secondaryOwner = ownerRepository.save(owner);

        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO();
        savingsAccountDTO.setBalance(BigDecimal.valueOf(1000L));
        savingsAccountDTO.setSecretKey("keysecret");
        savingsAccountDTO.setInterestRate(BigDecimal.valueOf(0.0025));
        savingsAccountDTO.setMinBalance(BigDecimal.valueOf(500));
        service.addSavings(savingsAccountDTO, primaryOwner.getId(), Optional.of(secondaryOwner.getId()));

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
        Owner primaryOwner = ownerRepository.findAll().get(1);
        Owner secondaryOwner = ownerRepository.findAll().get(0);

        CreditCardAccountDTO creditCardAccountDTO = new CreditCardAccountDTO();
        creditCardAccountDTO.setBalance(BigDecimal.valueOf(1234L));
        creditCardAccountDTO.setInterestRate(BigDecimal.valueOf(0.125));
        creditCardAccountDTO.setCreditLimit(BigDecimal.valueOf(2121));
        service.addCreditCard(creditCardAccountDTO, primaryOwner.getId(), Optional.of(secondaryOwner.getId()));

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

        Account origin = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(0);
        Long originId = origin.getId();
        Account destination = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(1);
        Long destinationId = destination.getId();

        BigDecimal amount = BigDecimal.valueOf(90L);
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(amount);
        moneyTransferDTO.setName("Alejandro Martínez");
        moneyTransferDTO.setDescription("money transfer test");
        moneyTransferDTO.setToAccountId(destinationId);
        CustomUserDetails customUserDetails = new CustomUserDetails(ownerRepository.findAll().get(0));
        service.startMoneyTransfer(customUserDetails, moneyTransferDTO, originId);

        origin = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(0);
        destination = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(1);

        BigDecimal diff = destination.getBalance().getAmount().subtract(origin.getBalance().getAmount()).abs();
        assertEquals(amount.multiply(BigDecimal.valueOf(2L)).setScale(2, RoundingMode.HALF_EVEN), diff);
   }

    @Test
    void updateBalance() {
        NewBalanceDTO newBalanceDTO = new NewBalanceDTO();
        newBalanceDTO.setBalance(BigDecimal.valueOf(12345.67));

        service.updateBalance(newBalanceDTO, service.getAccounts().get(0).getId());
        assertEquals(BigDecimal.valueOf(12345.67), service.getAccounts().get(0).getBalance().getAmount());

    }
}