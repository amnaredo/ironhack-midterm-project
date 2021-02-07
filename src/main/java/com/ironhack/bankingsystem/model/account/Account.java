package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "account")
public abstract class Account {

    // The penaltyFee for all accounts should be 40.
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(40.));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private Owner primaryOwner;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Owner secondaryOwner;

    @CreationTimestamp
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDateTime;
    private LocalDateTime lastAccessDateTime;

    @OneToMany(mappedBy = "toAccount")
    private List<Transaction> depositTxs;
    @OneToMany(mappedBy = "fromAccount")
    private List<Transaction> withdrawalTxs;

    @Enumerated(EnumType.STRING)
    private Type type;


    public Account() {
        this.creationDateTime = LocalDateTime.now();
        this.balance = new Money(BigDecimal.ZERO);
    }

    public Account(Owner primaryOwner) {
        this();
        this.primaryOwner = primaryOwner;
    }

    public Account(Owner primaryOwner, Owner secondaryOwner) {
        this(primaryOwner);
        this.secondaryOwner = secondaryOwner;
    }

    public Account(Owner primaryOwner, Money balance) {
        this(primaryOwner);
        this.balance = balance;
    }

    public Account(Owner primaryOwner, Owner secondaryOwner, Money balance) {
        this(primaryOwner, secondaryOwner);
        this.balance = balance;
    }


    public Money getBalance() {
        setLastAccessDateTime(LocalDateTime.now());
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public Owner getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(Owner primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public Owner getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(Owner secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
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

    protected Type getType() {
        return type;
    }

    protected void setType(Type type) {
        this.type = type;
    }
}
