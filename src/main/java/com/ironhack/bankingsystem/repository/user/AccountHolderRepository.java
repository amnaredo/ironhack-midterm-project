package com.ironhack.bankingsystem.repository.user;

import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
}
