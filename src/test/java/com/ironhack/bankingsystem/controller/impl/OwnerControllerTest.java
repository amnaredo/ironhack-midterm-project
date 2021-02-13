package com.ironhack.bankingsystem.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class OwnerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private IOwnerService service;
    @Autowired
    private OwnerRepository repository;
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
        ThirdPartyUser tpu = new ThirdPartyUser(
                "Third Party User",
                "hashedKey");
        repository.saveAll(List.of(ah, tpu));

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("ironhack");
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        adminRepository.deleteAll();
    }

    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    @Test
    void getOwners() throws Exception {

        MvcResult result = mockMvc.perform(
                get("/bank/users/owners"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Mr. Account Holder"));
        assertTrue(result.getResponse().getContentAsString().contains("Third Party User"));
    }

    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    @Test
    void getOwnerById() throws Exception {

        Owner owner = repository.findByName("Mr. Account Holder").get();

        MvcResult result =
                mockMvc.perform(
                        get("/bank/users/owners/" + owner.getId()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Mr. Account Holder"));
    }

    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    @Test
    void addAccountHolder() throws Exception {

        AccountHolderDTO ah = new AccountHolderDTO();
        ah.setType("ACCOUNT_HOLDER");
        ah.setName("Mrs. Account Holder");
        ah.setDateOfBirth("1985-04-14");
        ah.setStreet("Street");
        ah.setCity("City");
        ah.setPostalCode("PostalCode");
        ah.setUsername("username");
        ah.setPassword("password");
        String body = objectMapper.writeValueAsString(ah);

        MvcResult result =
                mockMvc.perform(
                    post("/bank/users/owners/ah")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andReturn();

        List<Owner> owners = service.getOwners();
        assertEquals(3, owners.size());
        assertTrue(result.getResponse().getContentAsString().contains("Mrs. Account Holder"));
    }

    @WithMockUser(username = "admin", password = "ironhack", roles = {"ADMIN"})
    @Test
    void addThirdPartyUser() throws Exception {

        ThirdPartyUserDTO tpu = new ThirdPartyUserDTO();
        tpu.setType(Type.THIRD_PARTY_USER.toString());
        tpu.setName("Another TPU");
        tpu.setHashedKey("anotherHashedKey");
        tpu.setUsername("username");
        tpu.setPassword("password");
        String body = objectMapper.writeValueAsString(tpu);

        MvcResult result =
                mockMvc.perform(
                        post("/bank/users/owners/tpu")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isCreated())
                        .andReturn();

        List<Owner> owners = service.getOwners();
        assertEquals(3, owners.size());
        assertEquals("Another TPU", owners.get(owners.size()-1).getName());
    }
}