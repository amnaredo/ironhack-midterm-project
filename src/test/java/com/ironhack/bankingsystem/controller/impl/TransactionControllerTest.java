package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.controller.interfaces.ITransactionController;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.transaction.enums.Type;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.impl.AdminService;
import com.ironhack.bankingsystem.service.impl.CustomUserDetailsService;
import com.ironhack.bankingsystem.service.impl.OwnerService;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    @Autowired
    private AdminRepository adminRepository;


    private MockMvc mockMvc;

    //private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        AccountHolder ah = new AccountHolder(
                "Mr. Account Holder",
                LocalDate.of(1984, 4, 14),
                new Address("Street", "City", "PostalCode"));
        ah.setUsername("mister");
        ah.setPassword("mister");
        ah.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", ah))));

        AccountHolder ah2 = new AccountHolder(
                "Mrs. Account Holder",
                LocalDate.of(1990, 5, 15),
                new Address("Street", "City", "PostalCode"));
        ah2.setUsername("mistress");
        ah2.setPassword("mistress");
        ah2.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", ah2))));
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

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("ironhack");
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    @Test
    void getTransactions() throws Exception {
        MvcResult result =
                mockMvc.perform(
                        get("/bank/transactions"))
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }

    @Test
    @WithMockUser(username = "mister", password = "mister", roles = {"OWNER"})
//    @WithUserDetails(value = "mister"/*, userDetailsServiceBeanName = "userDetailsService"*/)
    void getTransactionsByAccount() throws Exception {
        Account account = accountRepository.findAll().get(0);
//        UserDetails userDetails =
//                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(userDetails);
        CustomUserDetails customUserDetails = new CustomUserDetails(account.getPrimaryOwner());
//        System.out.println(customUserDetails);

        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId() + "/transactions")
                                .with(user(customUserDetails)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }

    @Test
    @WithMockUser(username = "mistress", password = "mistress", roles = {"OWNER"})
//    @WithUserDetails("mistress")
    void getTransactionsByAccount_recipient() throws Exception {
        Account account = accountRepository.findAll().get(1);
        CustomUserDetails customUserDetails = new CustomUserDetails(account.getPrimaryOwner());
        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId() + "/transactions")
                            .with(user(customUserDetails)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Another"));
        assertTrue(result.getResponse().getContentAsString().contains("Money"));
        assertTrue(result.getResponse().getContentAsString().contains("transfer :D"));
    }
}