package com.ironhack.bankingsystem.model.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@Table(name = "account")
public abstract class Account implements Serializable {

    // The penaltyFee for all accounts should be 40.
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(40.));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonManagedReference
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;
    @ManyToOne(/*fetch = FetchType.EAGER, */optional = false/*, cascade = CascadeType.ALL*/)
    private Owner primaryOwner;
    @ManyToOne(/*fetch = FetchType.EAGER, */optional = true)
    private Owner secondaryOwner;

    @CreationTimestamp
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDateTime;
    private LocalDateTime lastAccessDateTime;

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonBackReference
    private List<Transaction> depositTxs;
    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonBackReference
    private List<Transaction> withdrawalTxs;

    @Enumerated(EnumType.STRING)
    private Type type;


    public Account() {
        setBalance(new Money(BigDecimal.ZERO));

        setCreationDateTime(LocalDateTime.now());
        setLastAccessDateTime(getCreationDateTime());

        setDepositTxs(new ArrayList<>());
        setWithdrawalTxs(new ArrayList<>());
    }

    public Account(Owner primaryOwner) {
        this();
        setPrimaryOwner(primaryOwner);
    }

    public Account(Owner primaryOwner, Owner secondaryOwner) {
        this(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    public Account(Owner primaryOwner, Money balance) {
        this(primaryOwner);
        setBalance(balance);
    }

    public Account(Owner primaryOwner, Owner secondaryOwner, Money balance) {
        this(primaryOwner, secondaryOwner);
        setBalance(balance);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Owner getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(Owner primaryOwner) {
        if (primaryOwner == null)
            return;

        this.primaryOwner = primaryOwner;
        primaryOwner.addPrimaryAccount(this);
    }

    public Owner getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(Owner secondaryOwner) {
        if (secondaryOwner == null)
            return;

        this.secondaryOwner = secondaryOwner;
        secondaryOwner.addSecondaryAccount(this);
    }

    public Money getPenaltyFee() {
        return PENALTY_FEE;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getLastAccessDateTime() {
        return lastAccessDateTime;
    }

    public void setLastAccessDateTime(LocalDateTime lastAccessDateTime) {
        this.lastAccessDateTime = lastAccessDateTime;
    }

    public boolean hasSecondaryOwner() {
        return secondaryOwner != null;
    }

    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public List<Transaction> getDepositTxs() {
        return new ArrayList<Transaction>(depositTxs);
    }

    public void setDepositTxs(List<Transaction> depositTxs) {
        this.depositTxs = depositTxs;
    }

    public List<Transaction> getWithdrawalTxs() {
        return new ArrayList<Transaction>(withdrawalTxs);
    }

    public void setWithdrawalTxs(List<Transaction> withdrawalTxs) {
        this.withdrawalTxs = withdrawalTxs;
    }

    public void addDepositTransaction(Transaction transaction) {
        if (depositTxs.contains(transaction))
            return;
        depositTxs.add(transaction);
        transaction.setToAccount(this);

        // increase money
        getBalance().increaseAmount(transaction.getAmount());
    }

    public void addWithdrawalTransaction(Transaction transaction) {
        if (withdrawalTxs.contains(transaction))
            return;
        withdrawalTxs.add(transaction);
        transaction.setFromAccount(this);

        // decrease money
        getBalance().decreaseAmount(transaction.getAmount());
    }

    public abstract Money getMinimumBalance();

    public void updateLastAccessDateTime() {
        setLastAccessDateTime(LocalDateTime.now());
    }
}
