package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.dto.account.CheckingAccountDTO;
import com.ironhack.bankingsystem.dto.account.CreditCardAccountDTO;
import com.ironhack.bankingsystem.dto.account.SavingsAccountDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository repository;
    @Autowired
    private IAccountService service;
    @Autowired
    private OwnerRepository ownerRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        AccountHolder ah = new AccountHolder(
                "Mr. Account Holder",
                LocalDate.of(1990, 4, 14),
                new Address("Street", "City", "PostalCode"));
        AccountHolder ah2 = new AccountHolder(
                "Mrs. Account Holder",
                LocalDate.of(2000, 4, 14),
                new Address("Street", "City", "PostalCode"));
        ThirdPartyUser tpu = new ThirdPartyUser(
                "Third Party User",
                "hashedKey");
        ownerRepository.saveAll(List.of(ah, ah2, tpu));

        CheckingAccount check = new CheckingAccount(
                tpu,
                new Money(BigDecimal.valueOf(10000L)),
                "secretKeyA");
        StudentCheckingAccount stCheck = new StudentCheckingAccount(
                ah,
                new Money(BigDecimal.valueOf(5000L)),
                "secretKeyB");
        SavingsAccount sav = new SavingsAccount(
                ah2,
                new Money(BigDecimal.valueOf(10000L)),
                "secretKeyC");
        sav.setSecondaryOwner(ah);
        CreditCardAccount cc = new CreditCardAccount(
                ah,
                new Money(BigDecimal.valueOf(1000L)));

        repository.saveAll(List.of(check, stCheck, sav, cc));
    }

    @AfterEach
    void tearDown() {

        repository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void getAccounts() throws Exception {

        MvcResult result = mockMvc.perform(
                get("/accounts"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Mr. Account Holder"));
        assertTrue(result.getResponse().getContentAsString().contains("Mrs. Account Holder"));
        assertTrue(result.getResponse().getContentAsString().contains("Third Party User"));
    }

    @Test
    void getAccount() throws Exception {

        List<Account> accounts = repository.findAll();
        MvcResult result = mockMvc.perform(
                get("/accounts/" + accounts.get(0).getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(accounts.get(0).getPrimaryOwner().getName()));
    }

    @Test
    void getAccountsByOwner() throws Exception {

        List<Account> accounts = repository.findAll();
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = accounts.get(2).getPrimaryOwner().getId();
        MvcResult result = mockMvc.perform(
                get("/owners/" + idOwner + "/accounts"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
    }

    @Test
    void addChecking() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();

        CheckingAccountDTO check = new CheckingAccountDTO();
        check.setBalance(BigDecimal.valueOf(1000L));
        check.setSecretKey("12345678");
        String body = objectMapper.writeValueAsString(check);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/checking/" + idOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(check.getSecretKey()));
        // student checking account because age < 24
        assertTrue(result.getResponse().getContentAsString().contains(Type.STUDENT_CHECKING.toString()));
    }

    @Test
    void addChecking_twoOwners() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idFirstOwner = owners.get(0).getId();
        Long idSecondOwner = owners.get(1).getId();

        CheckingAccountDTO check = new CheckingAccountDTO();
        check.setBalance(BigDecimal.valueOf(1000L));
        check.setSecretKey("12345678");
        String body = objectMapper.writeValueAsString(check);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/checking/" + idFirstOwner + "/" + idSecondOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(check.getSecretKey()));
        // checking account because age of primary owner >= 24
        assertTrue(result.getResponse().getContentAsString().contains(Type.CHECKING.toString()));
    }


    @Test
    void addSavings() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();

        SavingsAccountDTO sav = new SavingsAccountDTO();
        sav.setBalance(BigDecimal.valueOf(2000L));
        sav.setSecretKey("12345678");
        sav.setInterestRate(new BigDecimal("0.1234"));
        String body = objectMapper.writeValueAsString(sav);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/savings/" + idOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(sav.getSecretKey()));
        assertTrue(result.getResponse().getContentAsString().contains("0.1234")); // interestRate
        assertTrue(result.getResponse().getContentAsString().contains("1000.00")); // default minimumBalance
    }

    @Test
    void addSavings_twoOwners() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idFirstOwner = owners.get(0).getId();
        Long idSecondOwner = owners.get(1).getId();

        SavingsAccountDTO sav = new SavingsAccountDTO();
        sav.setBalance(BigDecimal.valueOf(2000L));
        sav.setSecretKey("12345678");
        sav.setMinBalance(new BigDecimal("750.00"));
        String body = objectMapper.writeValueAsString(sav);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/savings/" + idFirstOwner + "/" + idSecondOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(sav.getSecretKey()));
        assertTrue(result.getResponse().getContentAsString().contains("0.0025")); // default interestRate
        assertTrue(result.getResponse().getContentAsString().contains("750.00")); // minimumBalance
    }

    @Test
    void addCreditCard() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();

        CreditCardAccountDTO cc = new CreditCardAccountDTO();
        cc.setBalance(BigDecimal.valueOf(2000L));
        cc.setInterestRate(new BigDecimal("0.1234"));
        String body = objectMapper.writeValueAsString(cc);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/creditcard/" + idOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains("0.1234")); // interestRate
        assertTrue(result.getResponse().getContentAsString().contains("100.00")); // default creditLimit
    }

    @Test
    void addCreditCard_twoOwners() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idFirstOwner = owners.get(0).getId();
        Long idSecondOwner = owners.get(1).getId();

        CreditCardAccountDTO cc = new CreditCardAccountDTO();
        cc.setBalance(BigDecimal.valueOf(2000L));
        cc.setCreditLimit(new BigDecimal("100000.00"));
        String body = objectMapper.writeValueAsString(cc);

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/creditcard/" + idFirstOwner + "/" + idSecondOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains("0.2000")); // default interestRate
        assertTrue(result.getResponse().getContentAsString().contains("100000.00")); // creditLimit
    }

    @Test
    void transferMoney() {

    }

    @Test
    void updateBalance() {

    }
}