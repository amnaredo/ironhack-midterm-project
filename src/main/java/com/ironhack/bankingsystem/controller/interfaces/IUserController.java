package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.model.user.impl.Owner;

import java.util.List;

public interface IUserController {

    List<Owner> getUsers();
}
