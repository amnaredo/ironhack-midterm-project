package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.transaction.enums.Type;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ITransactionService service;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TransactionRepository repository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        AccountHolder ah = new AccountHolder(
                "Mr. Account Holder",
                LocalDate.of(1984, 4, 14),
                new Address("Street", "City", "PostalCode"));

        AccountHolder ah2 = new AccountHolder(
                "Mrs. Account Holder",
                LocalDate.of(1990, 5, 15),
                new Address("Street", "City", "PostalCode"));
        ownerRepository.saveAll(List.of(ah, ah2));

        SavingsAccount sav = new SavingsAccount(
                ah,
                new Money(BigDecimal.valueOf(10000L)),
                "98765432");
        sav.setSecondaryOwner(ah2);
        CreditCardAccount cc = new CreditCardAccount(
                ah2,
                new Money(BigDecimal.valueOf(1000L)));
        accountRepository.saveAll(List.of(sav, cc));

        Transaction tx1 = new Transaction(new Money (BigDecimal.valueOf(200L)));
        tx1.setType(Type.MONEY_TRANSFER);
        tx1.setFromAccount(sav);
        tx1.setToAccount(cc);
        tx1.setAuthorName("Mister");
        tx1.setDescription("Money transfer");
        tx1.setTimestamp(LocalDateTime.now().minusDays(1));
        service.addTransaction(tx1);

        Transaction tx2 = new Transaction(new Money (BigDecimal.valueOf(200L)));
        tx2.setType(Type.MONEY_TRANSFER);
        tx2.setFromAccount(sav);
        tx2.setToAccount(cc);
        tx2.setAuthorName("Mister");
        tx2.setDescription("Another money transfer :D");
        tx2.setTimestamp(LocalDateTime.now());
        service.addTransaction(tx2);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void getTransactions() throws Exception {
        MvcResult result =
                mockMvc.perform(
                        get("/transactions"))
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }

    @Test
    void getTransactionsByAccount() throws Exception {
        Account account = accountRepository.findAll().get(0);
        System.out.println(account.getId());
        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId() + "/transactions"))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }

    @Test
    void getTransactionsByAccount_recipient() throws Exception {
        Account account = accountRepository.findAll().get(1);
        System.out.println(account.getId());
        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId() + "/transactions"))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }
}