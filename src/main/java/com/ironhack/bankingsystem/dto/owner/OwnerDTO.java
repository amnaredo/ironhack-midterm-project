package com.ironhack.bankingsystem.dto.owner;

import com.ironhack.bankingsystem.dto.user.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class OwnerDTO extends UserDTO {

//    @NotBlank(message = "Name is required")
//    @Size(min = 2, max = 40)
//    private String name;
    @NotBlank(message = "Type is required")
    private String type;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
