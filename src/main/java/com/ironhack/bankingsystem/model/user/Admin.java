package com.ironhack.bankingsystem.model.user;

import com.ironhack.bankingsystem.model.user.enums.Type;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private String name;


    public Admin() {
        this.setType(Type.ADMIN);
        this.setRoles(new HashSet<Role>(Collections.singletonList(new Role("ADMIN", this))));
    }

    public Admin(String name) {
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
}
