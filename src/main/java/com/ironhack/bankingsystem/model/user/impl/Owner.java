package com.ironhack.bankingsystem.model.user.impl;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.interfaces.IOwner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
//@Table(name = "owner")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.)
public abstract class Owner implements IOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER/*, orphanRemoval = true*/)
    private Collection<Account> primaryAccountList;
    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    private Collection<Account> secondaryAccountList;


    public Owner() {
        primaryAccountList = new ArrayList<>();
        secondaryAccountList = new ArrayList<>();
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

    public Collection<Account> getPrimaryAccountList() {
        return new ArrayList<Account>(primaryAccountList);
    }

    public void setPrimaryAccountList(Collection<Account> primaryAccountList) {
        this.primaryAccountList = primaryAccountList;
    }

    public Collection<Account> getSecondaryAccountList() {
        return new ArrayList<Account>(secondaryAccountList);
    }

    public void setSecondaryAccountList(Collection<Account> secondaryAccountList) {
        this.secondaryAccountList = secondaryAccountList;
    }

    public void addPrimaryAccount(Account account) {
        if (primaryAccountList.contains(account))
            return;
        primaryAccountList.add(account);
        account.setPrimaryOwner(this);
    }

    public void addSecondaryAccount(Account account) {
        if (secondaryAccountList.contains(account))
            return;
        secondaryAccountList.add(account);
        account.setSecondaryOwner(this);
    }


}
