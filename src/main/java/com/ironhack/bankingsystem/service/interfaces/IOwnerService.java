package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;

import java.util.List;
import java.util.Optional;

public interface IOwnerService {

    List<Owner> getOwners();
    Boolean existsOwner(Long id);
    Optional<Owner> getOwnerById(Long id);

    Owner addOwner(Owner owner);

    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);
    ThirdPartyUser addThirdPartyUser(ThirdPartyUserDTO thirdPartyUserDTO);

    void deleteAll();
}
