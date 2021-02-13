package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.interfaces.IMoneyTransferService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MoneyTransferServiceTest {

    @Autowired
    private IMoneyTransferService service;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OwnerRepository ownerRepository;
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
        ownerRepository.saveAll(List.of(accountHolder, thirdPartyUser));

        CheckingAccount checkingAccount = new CheckingAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)), "4321");
        SavingsAccount savingsAccount = new SavingsAccount(thirdPartyUser, new Money(BigDecimal.valueOf(1000L)), "1234");
        CreditCardAccount creditCardAccount = new CreditCardAccount(accountHolder, new Money(BigDecimal.valueOf(1000L)));
        accountRepository.saveAll(List.of(checkingAccount, studentCheckingAccount, savingsAccount, creditCardAccount));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void doMoneyTransfer() {
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
        service.doMoneyTransfer(moneyTransferDTO, originId);

        origin = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(0);
        destination = accountRepository.findByPrimaryOwner(ownerRepository.findAll().get(0)).get(1);

        BigDecimal diff = destination.getBalance().getAmount().subtract(origin.getBalance().getAmount()).abs();
        assertEquals(amount.multiply(BigDecimal.valueOf(2L)).setScale(2, RoundingMode.HALF_EVEN), diff);
    }
}