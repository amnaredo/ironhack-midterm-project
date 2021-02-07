package com.ironhack.bankingsystem.model.user.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.interfaces.IOwner;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
//@Table(name = "owner")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.)
public abstract class Owner implements IOwner, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonManagedReference
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER/*, orphanRemoval = true*/)
    private List<Account> primaryAccounts;
    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    private List<Account> secondaryAccounts;


    public Owner() {
        primaryAccounts = new ArrayList<>();
        secondaryAccounts = new ArrayList<>();
    }

    public Owner(String name) {
        this();
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Account> getPrimaryAccounts() {
        return new ArrayList<Account>(primaryAccounts);
    }

    public void setPrimaryAccounts(List<Account> primaryAccounts) {
        this.primaryAccounts = primaryAccounts;
    }

    public List<Account> getSecondaryAccounts() {
        return new ArrayList<Account>(secondaryAccounts);
    }

    public void setSecondaryAccounts(List<Account> secondaryAccounts) {
        this.secondaryAccounts = secondaryAccounts;
    }

    public void addPrimaryAccount(Account account) {
        if (primaryAccounts.contains(account))
            return;
        primaryAccounts.add(account);
        account.setPrimaryOwner(this);
    }

    public void addSecondaryAccount(Account account) {
        if (secondaryAccounts.contains(account))
            return;
        secondaryAccounts.add(account);
        account.setSecondaryOwner(this);
    }


}
