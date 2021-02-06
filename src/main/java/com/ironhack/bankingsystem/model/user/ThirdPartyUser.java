package com.ironhack.bankingsystem.model.user;

import com.ironhack.bankingsystem.model.user.enums.Type;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class ThirdPartyUser extends User {

    private String hashedKey;

    public ThirdPartyUser() {
        this.setType(Type.THIRD_PARTY);
    }

    public ThirdPartyUser(String name, String hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
        this.setType(Type.THIRD_PARTY);
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
