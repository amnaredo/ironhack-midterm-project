package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.user.AdminDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;

import java.util.List;

public interface IAdminController {
    List<Admin> getAdmins();
    List<User> getUsers();
    Admin addAdmin(AdminDTO adminDto);
}
