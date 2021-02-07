package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.user.AccountHolderRepository;
import com.ironhack.bankingsystem.repository.user.ThirdPartyUserRepository;
import com.ironhack.bankingsystem.repository.user.UserRepository;
import com.ironhack.bankingsystem.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private ThirdPartyUserRepository thirdPartyUserRepository;

    public List<Owner> getUsers() {
        return userRepository.findAll();
    }

    public Owner addUser(Owner owner) {
        switch (owner.getType()) {
            case ACCOUNT_HOLDER:
                return addAccountHolder((AccountHolder) owner);
            case THIRD_PARTY:
                return addThirdPartyUser((ThirdPartyUser) owner);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user type does not exist");
        }
    }

    private AccountHolder addAccountHolder(AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    private ThirdPartyUser addThirdPartyUser(ThirdPartyUser thirdPartyUser) {
        return thirdPartyUserRepository.save(thirdPartyUser);
    }
}
