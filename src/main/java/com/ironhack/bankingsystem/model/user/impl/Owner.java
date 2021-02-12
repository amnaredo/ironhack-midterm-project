package com.ironhack.bankingsystem.model.user.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.User;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.interfaces.IOwner;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "owner")
//@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.)
public abstract class Owner extends User implements IOwner, Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonManagedReference
//    private Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonBackReference
    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER/*, orphanRemoval = true*/)
    @Fetch(FetchMode.SUBSELECT)
    private List<Account> primaryAccounts;
    @JsonBackReference
    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Account> secondaryAccounts;


    public Owner() {
        primaryAccounts = new ArrayList<>();
        secondaryAccounts = new ArrayList<>();
    }

    public Owner(String name) {
        this();
        this.name = name;
    }


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

    @Override
    public Boolean hasAccountAccess(Long idAccount) {
        // look for the account id in the primary accounts
        // this would be better with a HashTable.containsKey...
        for(Account account : primaryAccounts)
            if (account.getId() == idAccount)
                return true;

        // look for the account id in the primary accounts
        for(Account account : secondaryAccounts)
            if (account.getId() == idAccount)
                return true;

        // no luck
        return false;
    }
}
