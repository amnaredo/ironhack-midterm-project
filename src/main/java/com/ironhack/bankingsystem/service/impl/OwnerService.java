package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.owner.AccountHolderDTO;
import com.ironhack.bankingsystem.dto.owner.ThirdPartyUserDTO;
import com.ironhack.bankingsystem.model.user.Address;
import com.ironhack.bankingsystem.model.user.Role;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.user.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.user.ThirdPartyUserRepository;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import com.ironhack.bankingsystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
public class OwnerService implements IOwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;

    public List<Owner> getOwners() {
        return ownerRepository.findAll();
    }

    public Boolean existsOwner(Long id) {
        return ownerRepository.existsById(id);
    }

    public Optional<Owner> getOwnerById(Long id) {
        if(!ownerRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");

        return ownerRepository.findById(id);
    }

    public Owner addOwner(Owner owner) {
        switch (owner.getType()) {
            case ACCOUNT_HOLDER:
                return saveAccountHolder((AccountHolder) owner);
            case THIRD_PARTY_USER:
                return saveThirdPartyUser((ThirdPartyUser) owner);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user type does not exist");
        }
    }

    public AccountHolder addAccountHolder(AccountHolderDTO accountHolderDTO) {
        AccountHolder accountHolder = new AccountHolder();

        // Type check
        try {
            accountHolder.setType(Type.valueOf(accountHolderDTO.getType().toUpperCase()));
            if (!accountHolder.getType().equals(Type.ACCOUNT_HOLDER))
                throw new IllegalArgumentException();
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is not valid");
        }

        // Date of birth check
        try {
            accountHolder.setDateOfBirth(LocalDate.parse(accountHolderDTO.getDateOfBirth()));
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth is not valid");
        }

        accountHolder.setName(accountHolderDTO.getName());
        accountHolder.setPrimaryAddress(new Address(accountHolderDTO.getStreet(), accountHolderDTO.getCity(), accountHolderDTO.getPostalCode()));
        accountHolder.setMailingAddress(new Address(accountHolderDTO.getMailingStreet(), accountHolderDTO.getMailingCity(), accountHolderDTO.getMailingPostalCode()));

        // set user credentials
        accountHolder.setUsername(accountHolderDTO.getUsername());
        accountHolder.setPassword(PasswordUtil.encryptPassword(accountHolderDTO.getPassword()));
        Set<Role> roleSet = new HashSet<Role>(Collections.singletonList(new Role("OWNER", accountHolder)));
        accountHolder.setRoles(roleSet);

        return saveAccountHolder(accountHolder);
    }

    public ThirdPartyUser addThirdPartyUser(ThirdPartyUserDTO thirdPartyUserDTO) {
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser();

        // Type
        try {
            thirdPartyUser.setType(Type.valueOf(thirdPartyUserDTO.getType().toUpperCase()));
            if (!thirdPartyUser.getType().equals(Type.THIRD_PARTY_USER))
                throw new IllegalArgumentException();
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type is not valid");
        }

        thirdPartyUser.setName(thirdPartyUserDTO.getName());
        thirdPartyUser.setHashedKey(thirdPartyUserDTO.getHashedKey());

        // set user credentials
        thirdPartyUser.setUsername(thirdPartyUserDTO.getUsername());
        thirdPartyUser.setPassword(PasswordUtil.encryptPassword(thirdPartyUserDTO.getPassword()));
        Set<Role> roleSet = new HashSet<Role>(Collections.singletonList(new Role("OWNER", thirdPartyUser)));
        thirdPartyUser.setRoles(roleSet);

        return saveThirdPartyUser(thirdPartyUser);
    }

    private AccountHolder saveAccountHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    private ThirdPartyUser saveThirdPartyUser(ThirdPartyUser thirdPartyUser) {
        return thirdPartyUserRepository.save(thirdPartyUser);
    }
}
