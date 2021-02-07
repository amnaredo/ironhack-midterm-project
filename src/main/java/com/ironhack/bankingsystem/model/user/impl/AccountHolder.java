package com.ironhack.bankingsystem.model.user.impl;

import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.enums.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends Owner {
//    AccountHolders should be able to access their own accounts and only their accounts when passing
//    the correct credentials using Basic Auth.
//    AccountHolders have:
//
//    A name
//    A date of birth
//    A primaryAddress (which should be a separate address class)
//    An optional mailingAddress


    private LocalDate dateOfBirth;
    @Embedded
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "mail_street")),
            @AttributeOverride(name = "city", column = @Column(name = "mail_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mail_postal_code"))
    })
    private Address mailingAddress;


    public AccountHolder() {
        this.setType(Type.ACCOUNT_HOLDER);
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.setType(Type.ACCOUNT_HOLDER);
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
