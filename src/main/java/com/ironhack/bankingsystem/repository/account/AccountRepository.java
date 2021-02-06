package com.ironhack.bankingsystem.repository.account;

import com.ironhack.bankingsystem.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
