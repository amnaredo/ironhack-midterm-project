package com.ironhack.bankingsystem.model.user.impl;

import com.ironhack.bankingsystem.model.user.enums.Type;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class ThirdPartyUser extends Owner {

    private String hashedKey;

    public ThirdPartyUser() {
        this.setType(Type.THIRD_PARTY_USER);
    }

    public ThirdPartyUser(String name, String hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
        this.setType(Type.THIRD_PARTY_USER);
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
