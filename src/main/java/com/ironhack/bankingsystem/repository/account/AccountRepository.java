package com.ironhack.bankingsystem.repository.account;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByPrimaryOwner(Owner owner);
    List<Account> findBySecondaryOwner(Owner owner);
}
