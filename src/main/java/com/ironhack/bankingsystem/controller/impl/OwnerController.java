package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IOwnerController;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OwnerController implements IOwnerController {

    @Autowired
    private IOwnerService ownerService;

    @GetMapping("/owners")
    @ResponseStatus(HttpStatus.OK)
    public List<Owner> getOwners() {
        return ownerService.getOwners();
    }
}
