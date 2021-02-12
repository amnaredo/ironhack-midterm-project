package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.repository.user.AdminRepository;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IAdminService;
import com.ironhack.bankingsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Admin addAdmin(UserDTO userDTO) {
        Admin admin = new Admin();
        admin.setType(Type.ADMIN);
        admin.setUsername(userDTO.getUsername());
        admin.setPassword(PasswordUtil.encryptPassword(userDTO.getPassword()));
        admin.setName(userDTO.getName());
        Set<Role> roleSet = new HashSet<Role>(Arrays.asList(new Role("ADMIN", admin)));
        admin.setRoles(roleSet);
        return adminRepository.save(admin);
    }
}
