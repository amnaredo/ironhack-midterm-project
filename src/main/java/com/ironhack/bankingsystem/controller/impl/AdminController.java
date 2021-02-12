package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IAdminController;
import com.ironhack.bankingsystem.dto.user.AdminDTO;
import com.ironhack.bankingsystem.model.user.Admin;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AdminController implements IAdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    @GetMapping("/users/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAdmins() {
        return adminService.getAdmins();
    }

    @PostMapping("/users/admins")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin addAdmin(@RequestBody @Valid AdminDTO adminDto) {
        return adminService.addAdmin(adminDto);
    }
}
