package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.model.user.impl.Owner;

import java.util.List;

public interface IAdminController {
    List<Admin> getAdmins();
    List<Owner> getOwners();
    List<User> getUsers();
    Admin addAdmin(UserDTO userDto);
}
