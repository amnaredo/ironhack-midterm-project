package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IUserController;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements IUserController {

    @Autowired
    private IOwnerService userService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<Owner> getUsers() {
        return userService.getOwners();
    }
}
