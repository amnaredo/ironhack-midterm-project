package com.ironhack.bankingsystem.model.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.transaction.enums.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

@Entity
public class Transaction {

    //public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Account fromAccount;
    @ManyToOne
    @JsonBackReference
    private Account toAccount;

    //@CreationTimestamp // had to quit this for testing fraud detection
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;
    //private String date; // to ease searching and querying by date


    private String authorName;
    private String description;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    private Type type;


    public Transaction() {
        setTimestamp(LocalDateTime.now());
    }

    public Transaction(Money amount) {
        this();
        this.amount = amount;
    }

    public Transaction(Account fromAccount, Account toAccount, Money amount, String authorName, String description) {
        this(amount);
        setFromAccount(fromAccount);
        setToAccount(toAccount);
        setAuthorName(authorName);
        setDescription(description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        if (fromAccount == null || fromAccount == this.fromAccount)
            return;

        this.fromAccount = fromAccount;
        fromAccount.addWithdrawalTransaction(this);
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        if (toAccount == null || toAccount == this.toAccount)
            return;

        this.toAccount = toAccount;
        toAccount.addDepositTransaction(this);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        //this.date = timestamp.format(DATE_FORMATTER);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
