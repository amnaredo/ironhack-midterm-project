package com.ironhack.bankingsystem.model.user;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.enums.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Enumerated
    private Type type;

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER)
    private List<Account> primaryAccountList;
    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    private List<Account> secondaryAccountList;


    public User() {
    }

    public User(String name) {
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
