package com.ironhack.bankingsystem.repository.user;

import com.ironhack.bankingsystem.model.user.impl.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Owner, Long> {
}
