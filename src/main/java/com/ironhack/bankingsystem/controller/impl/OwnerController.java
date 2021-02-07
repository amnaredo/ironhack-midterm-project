package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IOwnerController;
import com.ironhack.bankingsystem.dto.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OwnerController implements IOwnerController {

    @Autowired
    private IOwnerService ownerService;

    @GetMapping("/owners")
    @ResponseStatus(HttpStatus.OK)
    public List<Owner> getOwners() {
        return ownerService.getOwners();
    }

    @PostMapping("/owners/ah")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        return ownerService.addAccountHolder(accountHolderDTO);
    }

    @PostMapping("/owners/tpu")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyUser addThirdPartyUser(@RequestBody @Valid ThirdPartyUserDTO thirdPartyUserDTO) {
        return ownerService.addThirdPartyUser(thirdPartyUserDTO);
    }
}
