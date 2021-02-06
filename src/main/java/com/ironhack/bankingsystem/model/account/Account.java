package com.ironhack.bankingsystem.model.account;

import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.model.user.AccountHolder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "account")
public abstract class Account {

    // The penaltyFee for all accounts should be 40.
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(40.));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Money balance;
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private AccountHolder primaryOwner;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private AccountHolder secondaryOwner;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
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

    public Account(AccountHolder primaryOwner) {
        this();
        this.primaryOwner = primaryOwner;
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this(primaryOwner);
        this.secondaryOwner = secondaryOwner;
    }

    public Account(AccountHolder primaryOwner, Money balance) {
        this(primaryOwner);
        this.balance = balance;
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance) {
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

    public final AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public final void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public final AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public final void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public final Money getPenaltyFee() {
        return PENALTY_FEE;
    }

    public final LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public final void setCreationDateTime(LocalDateTime creationDateTime) {
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
