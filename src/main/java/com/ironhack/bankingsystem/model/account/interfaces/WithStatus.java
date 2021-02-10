package com.ironhack.bankingsystem.model.account.interfaces;

import com.ironhack.bankingsystem.model.account.enums.Status;

public interface WithStatus {
    Status getStatus();
    void setStatus(Status status);
}
