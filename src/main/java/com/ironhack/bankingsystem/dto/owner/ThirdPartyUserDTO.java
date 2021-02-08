package com.ironhack.bankingsystem.dto.owner;

import javax.validation.constraints.NotBlank;

public class ThirdPartyUserDTO extends OwnerDTO {

//    @NotBlank(message = "Name is required")
//    @Size(min = 2, max = 40)
//    private String name;
//    @NotBlank(message = "Type is required")
//    private String type;

    @NotBlank(message = "Hashed Key is required")
    private String hashedKey;


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
