package com.ironhack.bankingsystem.model.transaction;

import com.ironhack.bankingsystem.model.account.Account;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account fromAccount;
    @ManyToOne
    private Account toAccount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    private String authorName;
    private String description;

}
