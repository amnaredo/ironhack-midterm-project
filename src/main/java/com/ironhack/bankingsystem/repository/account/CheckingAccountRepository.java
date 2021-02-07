package com.ironhack.bankingsystem.repository.account;

import com.ironhack.bankingsystem.model.account.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {
}
