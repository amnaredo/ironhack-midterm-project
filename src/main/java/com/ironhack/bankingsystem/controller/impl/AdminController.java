package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IAdminController;
import com.ironhack.bankingsystem.dto.user.UserDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.service.impl.AdminService;
import com.ironhack.bankingsystem.service.impl.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AdminController implements IAdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/bank/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return adminService.getUsers();
    }

//    @GetMapping("/bank/users/owners")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Owner> getOwners() {
//        return ownerService.getOwners();
//    }

    @GetMapping("/bank/users/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAdmins() {
        return adminService.getAdmins();
    }

    @PostMapping("/bank/users/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin addAdmin(@RequestBody @Valid UserDTO userDto) {
        return adminService.addAdmin(userDto);
    }
}
