package com.ironhack.bankingsystem.repository.user;

import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyUserRepository extends JpaRepository<ThirdPartyUser, Long> {
}
