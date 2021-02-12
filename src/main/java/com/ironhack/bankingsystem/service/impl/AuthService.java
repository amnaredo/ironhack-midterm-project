package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithSecretKey;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.model.user.impl.ThirdPartyUser;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AuthService implements IAuthService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    public Boolean authAccountAccess(CustomUserDetails userDetails, Long accountId) {

        Account account = accountRepository.findById(accountId).get();
        if (userDetails.getUser().getId().equals(account.getPrimaryOwner().getId()))
            return true;

        return account.getSecondaryOwner() != null &&
                userDetails.getUser().getId().equals(account.getSecondaryOwner().getId());
    }

    public Boolean authMoneyTransfer(CustomUserDetails userDetails, String token, MoneyTransferDTO moneyTransferDTO, Long accountId) {

        if(!accountRepository.existsById(accountId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin account not found");

        // first there must be authorization for access the account
        if (!authAccountAccess(userDetails, accountId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");

        // access the account with authorization
        Account account = accountService.getAccountByIdWithAuth(userDetails, accountId);

        // check type of user
        Type userType = userDetails.getType();
        if(userType.equals(Type.THIRD_PARTY_USER)) {

            // check if the hashed key is present in the header
            if (token == null || token.isEmpty() || token.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No token or invalid token provided");
            }

            // check if it matches with the owner hashedKey
            ThirdPartyUser tpu = (ThirdPartyUser) userDetails.getUser();
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

//        if (userType.equals(Type.ACCOUNT_HOLDER)) {
//            // an account holder is accessing - no extra authorization needed
//            return true;
//        }

        // authorization ok
        return true;
    }
}
