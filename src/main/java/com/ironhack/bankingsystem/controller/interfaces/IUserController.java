package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.model.user.User;

import java.util.List;

public interface IUserController {

    List<User> getUsers();
}
