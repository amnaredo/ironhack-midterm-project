package com.ironhack.bankingsystem.controller.interfaces;

import com.ironhack.bankingsystem.dto.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;

import java.util.List;
import java.util.Optional;

public interface IOwnerController {

    List<Owner> getOwners();

    AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO);
    ThirdPartyUser addThirdPartyUser(ThirdPartyUserDTO thirdPartyUserDTO);
}
