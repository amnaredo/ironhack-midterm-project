package com.ironhack.bankingsystem.service.interfaces;

import com.ironhack.bankingsystem.dto.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;

import java.util.List;

public interface IOwnerService {

    List<Owner> getOwners();
    Owner addOwner(Owner owner);

    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);
    ThirdPartyUser addThirdPartyUser(ThirdPartyUserDTO thirdPartyUserDTO);

    void deleteAll();
}
