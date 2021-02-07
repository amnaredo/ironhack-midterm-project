package com.ironhack.bankingsystem.model.user.interfaces;

import com.ironhack.bankingsystem.model.user.enums.Type;

public interface IOwner {
    Type getType();
    String getName();
}
