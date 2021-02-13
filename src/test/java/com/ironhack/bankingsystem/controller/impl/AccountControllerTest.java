package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository repository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AdminRepository adminRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        AccountHolder ah = new AccountHolder(
                "Mr. Account Holder",
                LocalDate.of(1990, 4, 14),
                new Address("Street", "City", "PostalCode"));
        ah.setUsername("mister");
        ah.setPassword("mister");
//        ah.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", ah))));
        AccountHolder ah2 = new AccountHolder(
                "Mrs. Account Holder",
                LocalDate.of(2000, 4, 14),
                new Address("Street", "City", "PostalCode"));
        ah2.setUsername("mistress");
        ah2.setPassword("mistress");
//        ah2.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", ah2))));
        ThirdPartyUser tpu = new ThirdPartyUser(
                "Third Party User",
                "hashedKey");
        tpu.setUsername("thirdpartyuser");
        tpu.setPassword("thirdpartyuser");
//        tpu.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", tpu))));

        ownerRepository.saveAll(List.of(ah, ah2, tpu));

        CheckingAccount check = new CheckingAccount(
                tpu,
                new Money(BigDecimal.valueOf(10000L)),
                "43211234");
        StudentCheckingAccount stCheck = new StudentCheckingAccount(
                ah2,
                new Money(BigDecimal.valueOf(5000L)),
                "12345678");
        SavingsAccount sav = new SavingsAccount(
                ah,
                new Money(BigDecimal.valueOf(10000L)),
                "98765432");
        sav.setSecondaryOwner(ah2);
        CreditCardAccount cc = new CreditCardAccount(
                ah,
                new Money(BigDecimal.valueOf(1000L)));

        repository.saveAll(List.of(check, stCheck, sav, cc));

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("ironhack");
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        repository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void getAccounts() throws Exception {

        MvcResult result = mockMvc.perform(
                get("/bank/accounts"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Mr. Account Holder"));
        assertTrue(result.getResponse().getContentAsString().contains("Mrs. Account Holder"));
        assertTrue(result.getResponse().getContentAsString().contains("Third Party User"));
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void getAccount() throws Exception {

        List<Account> accounts = repository.findAll();
        MvcResult result = mockMvc.perform(
                get("/bank/accounts/" + accounts.get(0).getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(accounts.get(0).getPrimaryOwner().getName()));
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void getAccountsByOwner() throws Exception {

        List<Account> accounts = repository.findAll();
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = accounts.get(2).getPrimaryOwner().getId();
        MvcResult result = mockMvc.perform(
                get("/bank/users/owners/" + idOwner + "/accounts"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void addChecking() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();

        CheckingAccountDTO check = new CheckingAccountDTO();
        check.setBalance(BigDecimal.valueOf(1000L));
        check.setSecretKey("12345678");
        String body = objectMapper.writeValueAsString(check);

        MvcResult result =
                mockMvc.perform(
                        post("/bank/accounts/checking/" + idOwner)
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
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
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
                        post("/bank/accounts/checking/" + idFirstOwner + "/" + idSecondOwner)
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
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
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
                        post("/bank/accounts/savings/" + idOwner)
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
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
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
                        post("/bank/accounts/savings/" + idFirstOwner + "/" + idSecondOwner)
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
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void addCreditCard() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();

        CreditCardAccountDTO cc = new CreditCardAccountDTO();
        cc.setBalance(BigDecimal.valueOf(2000L));
        cc.setInterestRate(new BigDecimal("0.1234"));
        String body = objectMapper.writeValueAsString(cc);

        MvcResult result =
                mockMvc.perform(
                        post("/bank/accounts/creditcard/" + idOwner)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(1).getName()));
        assertTrue(result.getResponse().getContentAsString().contains("0.1234")); // interestRate
        assertTrue(result.getResponse().getContentAsString().contains("100.00")); // default creditLimit
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
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
                        post("/bank/accounts/creditcard/" + idFirstOwner + "/" + idSecondOwner)
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
    @WithMockUser(username = "mister", password = "mister", roles = {"OWNER"})
    void transferMoney_accountHolder() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(0).getId();
        Account fromAccount = owners.get(0).getPrimaryAccounts().get(1);
        Account toAccount = owners.get(0).getPrimaryAccounts().get(0);

        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount(new BigDecimal("200.00"));
        transfer.setToAccountId(toAccount.getId());
        transfer.setName(owners.get(0).getName());
        transfer.setDescription("More savings!");
        String body = objectMapper.writeValueAsString(transfer);

        CustomUserDetails customUserDetails = new CustomUserDetails(fromAccount.getPrimaryOwner());

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/" + fromAccount.getId())
                                .with(user(customUserDetails))
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(0).getName()));
        assertTrue(result.getResponse().getContentAsString().contains("800.00")); // savings account
    }

    @Test
    @WithMockUser(username = "mistress", password = "mistress", roles = {"OWNER"})
    void transferMoney_accountHolder_secondOwner() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(0).getId();
        Account fromAccount = owners.get(0).getPrimaryAccounts().get(0);
        Account toAccount = owners.get(0).getPrimaryAccounts().get(1);

        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount(new BigDecimal("500.00"));
        transfer.setToAccountId(toAccount.getId());
        transfer.setName(fromAccount.getSecondaryOwner().getName());
        transfer.setDescription("Less savings... to spend with credit card");
        String body = objectMapper.writeValueAsString(transfer);

        CustomUserDetails customUserDetails = new CustomUserDetails(fromAccount.getSecondaryOwner());

        MvcResult result =
                mockMvc.perform(
                        post("/accounts/" + fromAccount.getId())
                                .with(user(customUserDetails))
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(fromAccount.getSecondaryOwner().getName()));
        assertTrue(result.getResponse().getContentAsString().contains("9500.00")); // credit card account
    }

    @Test
    @WithMockUser(username = "thirdpartyuser", password = "thirdpartyuser", roles = {"OWNER"})
    void transferMoney_thirdPartyUser() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(2).getId();
        Account fromAccount = owners.get(2).getPrimaryAccounts().get(0);
        Account toAccount = owners.get(0).getPrimaryAccounts().get(1);

        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount(new BigDecimal("200.00"));
        transfer.setToAccountId(toAccount.getId());
        transfer.setName(owners.get(2).getName());
        transfer.setDescription("Refund of money");
        transfer.setSecretKey("43211234");
        String body = objectMapper.writeValueAsString(transfer);

        CustomUserDetails customUserDetails = new CustomUserDetails(fromAccount.getPrimaryOwner());

        MvcResult result =
//        String message =
                mockMvc.perform(
                        post("/accounts/" + fromAccount.getId())
                                .with(user(customUserDetails))
                                .header("Hashed-Key", "hashedKey")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isCreated())
                        .andReturn();
//        .andReturn().getResolvedException().getMessage();
//        System.out.println(message);
        assertTrue(result.getResponse().getContentAsString().contains(owners.get(2).getName()));
        assertTrue(result.getResponse().getContentAsString().contains("9800.00")); // checking account
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void updateBalance() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(1).getId();
        Account account = owners.get(1).getPrimaryAccounts().get(0);

        NewBalanceDTO newBalance = new NewBalanceDTO();
        newBalance.setBalance(new BigDecimal("5500.00"));
        String body = objectMapper.writeValueAsString(newBalance);
        MvcResult result =
                mockMvc.perform(
                        patch("/bank/accounts/" + account.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
    }

    @Test
    @WithMockUser(username = "mister", password = "mister", roles = {"OWNER"})
    void getAccountWithAuth() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(0).getId();
        Account account = owners.get(0).getPrimaryAccounts().get(0);

        CustomUserDetails customUserDetails = new CustomUserDetails(owners.get(0));

        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId())
                                .with(user(customUserDetails)))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @WithMockUser(username = "mistress", password = "mistress", roles = {"OWNER"})
    void getAccountWithAuth_notOwner() throws Exception {
        List<Owner> owners = ownerRepository.findAll();
        Long idOwner = owners.get(0).getId();
        Account account = owners.get(0).getPrimaryAccounts().get(1);

        CustomUserDetails customUserDetails = new CustomUserDetails(owners.get(1));

        MvcResult result =
                mockMvc.perform(
                        get("/accounts/" + account.getId())
                                .with(user(customUserDetails)))
                        .andExpect(status().isUnauthorized())
                        .andReturn();
    }

}