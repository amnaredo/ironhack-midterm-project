package com.ironhack.bankingsystem.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AdminDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 40)
    private String name;

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 16)
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 16)
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
