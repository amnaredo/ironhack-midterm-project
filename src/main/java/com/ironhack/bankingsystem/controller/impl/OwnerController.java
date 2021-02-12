package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IOwnerController;
import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class OwnerController implements IOwnerController {

    @Autowired
    private IOwnerService ownerService;

    @GetMapping("/bank/users/owners")
    @ResponseStatus(HttpStatus.OK)
    public List<Owner> getOwners() {
        return ownerService.getOwners();
    }

    @GetMapping("/bank/users/owners/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Owner getOwnerById(@PathVariable("id") @NumberFormat @Min(1) Long id) {
        return ownerService.getOwnerById(id).get();
    }

    @PostMapping("/bank/users/owners/ah")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        return ownerService.addAccountHolder(accountHolderDTO);
    }

    @PostMapping("/bank/users/owners/tpu")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyUser addThirdPartyUser(@RequestBody @Valid ThirdPartyUserDTO thirdPartyUserDTO) {
        return ownerService.addThirdPartyUser(thirdPartyUserDTO);
    }
}
