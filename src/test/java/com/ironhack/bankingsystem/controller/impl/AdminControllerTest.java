package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IAdminService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IAdminService service;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TransactionRepository repository;
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

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("ironhack");
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        ownerRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void getUsers() throws Exception {
        MvcResult result =
                mockMvc.perform(
                        get("/bank/users"))
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("mister"));
        assertTrue(result.getResponse().getContentAsString().contains("mistress"));
        assertTrue(result.getResponse().getContentAsString().contains("admin"));
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void getAdmins() throws Exception {
        MvcResult result =
                mockMvc.perform(
                        get("/bank/users/admins"))
                        .andExpect(status().isOk())
                        .andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("mister"));
        assertFalse(result.getResponse().getContentAsString().contains("mistress"));
        assertTrue(result.getResponse().getContentAsString().contains("admin"));
    }

    @Test
    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    void addAdmin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Another Admin");
        userDTO.setUsername("admin_");
        userDTO.setPassword("helloworld");
        String body = objectMapper.writeValueAsString(userDTO);

        MvcResult result =
                mockMvc.perform(
                        post("/bank/users/admins")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isCreated())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("admin_"));
        assertTrue(result.getResponse().getContentAsString().contains("Another Admin"));
        assertEquals(2, adminRepository.findAll().size());
    }
}