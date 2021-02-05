package com.ironhack.bankingsystem.model.user;

import java.time.LocalDate;

public class AccountHolder {
//    AccountHolders should be able to access their own accounts and only their accounts when passing
//    the correct credentials using Basic Auth.
//    AccountHolders have:
//
//    A name
//    A date of birth
//    A primaryAddress (which should be a separate address class)
//    An optional mailingAddress

    private String name;
    private LocalDate dateOfBirth;
    private Address primaryAddress;
    private Address mailingAddress;

    public AccountHolder() {
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
