package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithSecretKey;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AuthService implements IAuthService {

    @Autowired
    private AccountService accountService;

    public Boolean authAccountAccess(/*UserDetails userDetails,*/Long accountId) {
        return true;
        // todo
        //  check ownership of the account
    }

    public Boolean authMoneyTransfer(String token, MoneyTransferDTO moneyTransferDTO, Long accountId) {

        if(!accountService.existsAccount(accountId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin account not found");

        // first there must be authorization for access the account
        if (!authAccountAccess(accountId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have access to this account");

        Account account = accountService.getAccountById(accountId);


        // todo
        //  get the type of the user who is transfering money...
        //  /*UserDetails userDetails,*/
        //  (for now we suppose is accessing the primary owner)


        Owner owner = account.getPrimaryOwner();
        Type ownerType = owner.getType();

        if (ownerType.equals(Type.ACCOUNT_HOLDER)) {
            // an account holder is accessing - no extra authorization needed
            return true;
        }

        if(ownerType.equals(Type.THIRD_PARTY_USER)) {

            // check if the hashed key is present in the header
            if (token == null || token.isEmpty() || token.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No token or invalid token provided");
            }

            // check if it matches with the owner hashedKey
            ThirdPartyUser tpu = (ThirdPartyUser) owner;
            if (!token.equals(tpu.getHashedKey())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed authorization token validation");
            }

            // check if the account has a secret key and if it matches the secret key provided for the transfer
            if (WithSecretKey.class.isAssignableFrom(account.getClass())) {
                WithSecretKey accountWithSecretKey = (WithSecretKey) account;
                if (!moneyTransferDTO.getSecretKey().equals(accountWithSecretKey.getSecretKey())) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed secret key validation");
                }
            }
        }

        // authorization ok
        return true;
    }
}
