package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.user.impl.Owner;

import java.util.List;

public interface IUserService {

    List<Owner> getUsers();
    Owner addUser(Owner owner);
}
