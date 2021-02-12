package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MoneyTransferServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AccountRepository accountRepository;


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

        accountService.addAccount(checkingAccount);
        accountService.addAccount(studentCheckingAccount);
        accountService.addAccount(savingsAccount);
        accountService.addAccount(creditCardAccount);
    }

    @AfterEach
    void tearDown() {
        transactionService.deleteAll();
        accountService.deleteAll();
        ownerService.deleteAll();
    }

    @Test
    void doMoneyTransfer() {
        Account origin = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(0);
        Long originId = origin.getId();
        Account destination = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(1);
        Long destinationId = destination.getId();

        BigDecimal amount = BigDecimal.valueOf(90L);
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(amount);
        moneyTransferDTO.setName("Alejandro Martínez");
        moneyTransferDTO.setDescription("money transfer test");
        moneyTransferDTO.setToAccountId(destinationId);
        // todo
        //accountService.startMoneyTransfer(moneyTransferDTO, originId);

        origin = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(0);
        destination = accountRepository.findByPrimaryOwner(ownerService.getOwners().get(0)).get(1);

        BigDecimal diff = destination.getBalance().getAmount().subtract(origin.getBalance().getAmount()).abs();
        assertEquals(amount.multiply(BigDecimal.valueOf(2L)).setScale(2, RoundingMode.HALF_EVEN), diff);
    }
}