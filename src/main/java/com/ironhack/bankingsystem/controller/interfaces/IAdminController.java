package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import java.util.List;

public interface IAdminController {

    // GET /users/admins
    List<Admin> getAdmins();

//    // GET /users/owners
//    List<Owner> getOwners();

    // GET /users
    List<User> getUsers();

    // POST /users/admins
    Admin addAdmin(UserDTO userDto);
}
