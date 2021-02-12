package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.owner.OwnerDTO;
import com.ironhack.bankingsystem.dto.user.AdminDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;

import java.util.List;

public interface IAdminService {
    List<Admin> getAdmins();
    List<User> getUsers();
    Admin addAdmin(AdminDTO adminDTO);
    //Admin addUser(OwnerDTO ownerDTO);
}
