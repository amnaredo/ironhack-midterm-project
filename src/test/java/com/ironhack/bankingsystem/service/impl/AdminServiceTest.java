package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IAdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private IAdminService service;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Admin admin = new Admin();
        admin.setName("Alejandro");
        admin.setUsername("admin");
        admin.setPassword("ironhack");
        admin.setType(Type.ADMIN);
        admin.setRoles(new HashSet<Role>(Collections.singletonList(new Role("ADMIN", admin))));
        adminRepository.save(admin);

        Owner owner = new AccountHolder();
        owner.setName("Prueba");
        owner.setUsername("username");
        owner.setPassword("password");
        owner.setRoles(new HashSet<Role>(Collections.singletonList(new Role("OWNER", admin))));
        ownerRepository.save(owner);
    }

    @AfterEach
    void tearDown() {
        ownerRepository.deleteAll();
        adminRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAdmins() {
        assertEquals(1, service.getAdmins().size());
    }

    @Test
    void getUsers() {
        assertEquals(2, service.getUsers().size());
    }

    @Test
    void addAdmin() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Admin 2");
        userDTO.setUsername("admin2");
        userDTO.setPassword("password");
        service.addAdmin(userDTO);

        assertEquals(2, adminRepository.findAll().size());
        assertEquals(3, userRepository.findAll().size());
    }
}