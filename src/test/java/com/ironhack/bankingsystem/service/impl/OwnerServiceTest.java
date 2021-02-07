package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.user.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.user.ThirdPartyUserRepository;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OwnerServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        userRepository.deleteAll();
    }

    @Test
    void getUsers() {
        List<Owner> owners = userService.getUsers();
        assertEquals(2, owners.size());
    }

    @Test
    void addUser() {
//        User newUser = new AccountHolder("Paco Pérez", LocalDate.of(1972, 1, 15), new Address("Calle Constitución", "Oviedo", "33300"));
//        userService.addUser(newUser);
//
//
//        List<User> users = userRepository.findAll();
//        assertEquals(3, users.size());
    }
}