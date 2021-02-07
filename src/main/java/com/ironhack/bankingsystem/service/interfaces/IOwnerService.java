package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.model.user.impl.Owner;

import java.util.List;

public interface IOwnerService {

    List<Owner> getOwners();
    Owner addOwner(Owner owner);
    void deleteAll();
}
