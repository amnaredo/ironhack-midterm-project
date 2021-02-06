package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
