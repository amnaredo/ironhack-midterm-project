package com.ironhack.bankingsystem.controller.impl;

import com.ironhack.bankingsystem.controller.interfaces.IAccountController;
import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.CheckingAccount;
import com.ironhack.bankingsystem.model.account.CreditCardAccount;
import com.ironhack.bankingsystem.model.account.SavingsAccount;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAuthService authService;

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(
            @PathVariable
            @NumberFormat
            @Min(1)
            Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/owners/{id}/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccountsByOwner(@PathVariable("id") @NumberFormat @Min(1) Long id) {
        return accountService.getAccountsByOwner(id);
    }

    @PostMapping("/accounts/checking/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccount addChecking(
            @RequestBody
            @Valid
            CheckingAccountDTO checkingAccountDTO,
            @PathVariable("id")
            @NumberFormat
            @Min(1)
            Long id) {
        return accountService.addChecking(checkingAccountDTO, id, Optional.empty());
    }

    @PostMapping("/accounts/checking/{id1}/{id2}")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccount addChecking(
            @RequestBody
            @Valid
            CheckingAccountDTO checkingAccountDTO,
            @PathVariable("id1")
            @NumberFormat
            @Min(1)
            Long id,
            @PathVariable("id2")
            @NumberFormat
            @Min(1)
            Long otherId) {
        return accountService.addChecking(checkingAccountDTO, id, Optional.of(otherId));
    }


    @PostMapping("/accounts/savings/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount addSavings(
            @RequestBody
            @Valid
            SavingsAccountDTO savingsAccountDTO,
            @PathVariable("id")
            @NumberFormat
            @Min(1)
            Long id) {
        return accountService.addSavings(savingsAccountDTO, id, Optional.empty());
    }

    @PostMapping("/accounts/savings/{id1}/{id2}")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount addSavings(
            @RequestBody
            @Valid
            SavingsAccountDTO savingsAccountDTO,
            @PathVariable("id1")
            @NumberFormat
            @Min(1)
            Long id,
            @PathVariable("id2")
            @NumberFormat
            @Min(1)
            Long otherId) {
        return accountService.addSavings(savingsAccountDTO, id, Optional.of(otherId));
    }


    @PostMapping("/accounts/creditcard/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardAccount addCreditCard(
            @RequestBody
            @Valid
            CreditCardAccountDTO creditCardAccountDTO,
            @PathVariable("id")
            @NumberFormat
            @Min(1)
            Long id) {
        return accountService.addCreditCard(creditCardAccountDTO, id, Optional.empty());
    }

    @PostMapping("/accounts/creditcard/{id1}/{id2}")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardAccount addCreditCard(
            @RequestBody
            @Valid
            CreditCardAccountDTO creditCardAccountDTO,
            @PathVariable("id1")
            @NumberFormat
            @Min(1)
            Long id,
            @PathVariable("id2")
            @NumberFormat
            @Min(1)
            Long otherId) {
        return accountService.addCreditCard(creditCardAccountDTO, id, Optional.of(otherId));
    }


    @PostMapping(value = "/accounts/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account transferMoney(
            @RequestHeader(value = "Hashed-Key", required = false)
            String token,
            @RequestBody
            @Valid
            MoneyTransferDTO moneyTransferDTO,
            @PathVariable
            @NumberFormat
            @Min(1)
            Long id) {

        authService.authMoneyTransfer(token, moneyTransferDTO, id);
        return accountService.startMoneyTransfer(moneyTransferDTO, id);
    }


    @PatchMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(
            @RequestBody
            @Valid
            NewBalanceDTO newBalanceDTO,
            @PathVariable
            @NumberFormat
            @Min(1)
            Long id) {
        accountService.updateBalance(newBalanceDTO, id);
    }
}
