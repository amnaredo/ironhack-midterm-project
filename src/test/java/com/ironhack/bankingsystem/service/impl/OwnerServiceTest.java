package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.user.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.user.ThirdPartyUserRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OwnerServiceTest {

    @Autowired
    private OwnerService service;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;


    @BeforeEach
    void setUp() {
        AccountHolder accountHolder = new AccountHolder("Alejandro Martínez", LocalDate.of(1984, 4, 14), new Address("Calle Corrida", "Gijón", "33201"));
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("Google", "Hola");

        accountHolderRepository.save(accountHolder);
        thirdPartyUserRepository.save(thirdPartyUser);
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
        thirdPartyUserRepository.deleteAll();
        ownerRepository.deleteAll();
    }


    @Test
    void getOwners() {
        List<Owner> owners = service.getOwners();
        assertEquals(2, owners.size());
    }

    @Test
    void existsOwner() {

        Optional<Owner> owner = ownerRepository.findByName("Alejandro Martínez");
        Long ownerId = owner.get().getId();

        assertTrue(service.existsOwner(ownerId));
        assertTrue(service.existsOwner(ownerId + 1));
        assertFalse(service.existsOwner(ownerId - 1));
    }

    @Test
    void getOwnerById() {

        Optional<Owner> owner = ownerRepository.findByName("Google");
        Long ownerId = owner.get().getId();

        Optional<Owner> ownerFound = service.getOwnerById(ownerId);
        assertTrue(ownerFound.isPresent());
        assertTrue(ownerFound.get().getName().equalsIgnoreCase("Google"));
    }

    @Test
    void addOwner() {
        Owner newOwner = new AccountHolder("Paco Pérez", LocalDate.of(1972, 1, 15), new Address("Calle Constitución", "Oviedo", "33300"));
        service.addOwner(newOwner);

        List<Owner> owners = ownerRepository.findAll();
        assertEquals(3, owners.size());
    }

    @Test
    void addAccountHolder() {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setType("account_holder");
        accountHolderDTO.setName("Ana Fernández");
        accountHolderDTO.setDateOfBirth("2000-02-09");
        accountHolderDTO.setStreet("Calle Mayor");
        accountHolderDTO.setCity("Madrid");
        accountHolderDTO.setPostalCode("28080");
        accountHolderDTO.setUsername("username");
        accountHolderDTO.setPassword("password");

        AccountHolder accountHolder = service.addAccountHolder(accountHolderDTO);

        List<Owner> owners = ownerRepository.findAll();
        assertEquals(3, owners.size());
        assertEquals("Ana Fernández", owners.get(owners.size()-1).getName());
    }

    @Test
    void addThirdPartyUser() {

        ThirdPartyUserDTO thirdPartyUserDTO = new ThirdPartyUserDTO();
        thirdPartyUserDTO.setType("third_party_user");
        thirdPartyUserDTO.setName("Hello World");
        thirdPartyUserDTO.setHashedKey("W0RLDH3LL0");
        thirdPartyUserDTO.setUsername("username");
        thirdPartyUserDTO.setPassword("password");

        ThirdPartyUser thirdPartyUser = service.addThirdPartyUser(thirdPartyUserDTO);

        List<Owner> owners = ownerRepository.findAll();
        assertEquals(3, owners.size());
        assertEquals("Hello World", owners.get(owners.size()-1).getName());
    }
}