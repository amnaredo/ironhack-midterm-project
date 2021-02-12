package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;

import java.util.List;

public interface IOwnerController {

    // GET /owners
    List<Owner> getOwners();

    // POST /owners/ah
    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);

    // POST /owners/tpu
    ThirdPartyUser addThirdPartyUser(ThirdPartyUserDTO thirdPartyUserDTO);

    // GET /owners/{id}
    Owner getOwnerById(Long id);
}
