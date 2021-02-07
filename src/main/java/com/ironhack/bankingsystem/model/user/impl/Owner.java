package com.ironhack.bankingsystem.model.user.impl;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.interfaces.IOwner;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER)
    private List<Account> primaryAccountList;
    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    private List<Account> secondaryAccountList;


    public Owner() {
    }

    public Owner(String name) {
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

    public List<Account> getPrimaryAccountList() {
        return primaryAccountList;
    }

    public void setPrimaryAccountList(List<Account> primaryAccountList) {
        this.primaryAccountList = primaryAccountList;
    }

    public List<Account> getSecondaryAccountList() {
        return secondaryAccountList;
    }

    public void setSecondaryAccountList(List<Account> secondaryAccountList) {
        this.secondaryAccountList = secondaryAccountList;
    }
}
