package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.*;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.user.enums.Type;
import com.ironhack.bankingsystem.model.user.impl.AccountHolder;
import com.ironhack.bankingsystem.model.user.impl.Owner;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.repository.user.OwnerRepository;
import com.ironhack.bankingsystem.security.CustomUserDetails;
import com.ironhack.bankingsystem.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private IMoneyTransferService moneyTransferService;
    @Autowired
    private IInterestsFeesService interestsFeesService;
    @Autowired
    private IAuthService authService;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Boolean existsAccount(Long id) {
        return accountRepository.existsById(id);
    }

    public Account getAccountById(Long id) {
        if (!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        return accountRepository.findById(id).get();
    }

    public Account getAccountByIdWithAuth(CustomUserDetails userDetails, Long id) {
        if(!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");

        // authorize access
        if (!authService.authAccountAccess(userDetails, id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");

        // get the account
        Account account = accountRepository.findById(id).get();

        // apply interests/fees
        interestsFeesService.applyInterestsFeesService(account);

        // update last access date time
        account.updateLastAccessDateTime();

        return saveAccount(account);
    }

    public List<Account> getAccountsByOwner(Long idOwner) {
        if (!ownerRepository.existsById(idOwner))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not valid");

        Owner owner = ownerRepository.findById(idOwner).get();
        return accountRepository.findByPrimaryOwnerOrSecondaryOwner(owner, owner);
    }

    public Account addAccount(Account account) {
        switch (account.getType()) {
            case CHECKING:
                return saveCheckingAccount((CheckingAccount) account);
            case STUDENT_CHECKING:
                return saveStudentCheckingAccount((StudentCheckingAccount) account);
            case SAVINGS:
                return saveSavingsAccount((SavingsAccount) account);
            case CREDIT_CARD:
                return saveCreditCardAccount((CreditCardAccount) account);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account type does not exist");
        }
    }

    public CheckingAccount addChecking(CheckingAccountDTO checkingAccountDTO, Long id, Optional<Long> otherId) {

        if (!ownerRepository.existsById(id) || (otherId.isPresent() && !ownerRepository.existsById(otherId.get())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not valid");

        Owner owner = ownerRepository.findById(id).get();
        Owner otherOwner =
                otherId.isPresent() ? ownerRepository.findById(otherId.get()).get() : null;

        if (owner.getType().equals(Type.ACCOUNT_HOLDER)) {

            // When creating a new Checking account, if the primaryOwner is less than 24,
            // a StudentChecking account should be created otherwise a regular Checking Account should be created.

            AccountHolder accountHolder = (AccountHolder) owner;
            LocalDate now = LocalDate.now();

            if (ChronoUnit.YEARS.between(accountHolder.getDateOfBirth(), now) < 24) {
                StudentCheckingAccount studentCheckingAccount = new StudentCheckingAccount();
                studentCheckingAccount.setPrimaryOwner(accountHolder);
                studentCheckingAccount.setSecondaryOwner(otherOwner);
                studentCheckingAccount.setBalance(new Money(checkingAccountDTO.getBalance()));
                studentCheckingAccount.setSecretKey(checkingAccountDTO.getSecretKey());

                return saveStudentCheckingAccount(studentCheckingAccount);
            }
        }

        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setPrimaryOwner(owner);
        checkingAccount.setSecondaryOwner(otherOwner);
        checkingAccount.setBalance(new Money(checkingAccountDTO.getBalance()));
        checkingAccount.setSecretKey(checkingAccountDTO.getSecretKey());

        return saveCheckingAccount(checkingAccount);
    }

    public SavingsAccount addSavings(SavingsAccountDTO savingsAccountDTO, Long id, Optional<Long> otherId) {

        if (!ownerRepository.existsById(id) || (otherId.isPresent() && !ownerRepository.existsById(otherId.get())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not valid");

        Owner owner = ownerRepository.findById(id).get();
        Owner otherOwner =
                otherId.isPresent() ? ownerRepository.findById(otherId.get()).get() : null;

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setPrimaryOwner(owner);
        savingsAccount.setSecondaryOwner(otherOwner);
        savingsAccount.setBalance(new Money(savingsAccountDTO.getBalance()));
        savingsAccount.setSecretKey(savingsAccountDTO.getSecretKey());
        savingsAccount.setInterestRate(savingsAccountDTO.getInterestRate());
        savingsAccount.setMinimumBalance(new Money(savingsAccountDTO.getMinBalance()));

        return saveSavingsAccount(savingsAccount);
    }

    public CreditCardAccount addCreditCard(CreditCardAccountDTO creditCardAccountDTO, Long id, Optional<Long> otherId) {

        if (!ownerRepository.existsById(id) || (otherId.isPresent() && !ownerRepository.existsById(otherId.get())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not valid");

        Owner owner = ownerRepository.findById(id).get();
        Owner otherOwner =
                otherId.isPresent() ? ownerRepository.findById(otherId.get()).get() : null;

        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setPrimaryOwner(owner);
        creditCardAccount.setSecondaryOwner(otherOwner);
        creditCardAccount.setBalance(new Money(creditCardAccountDTO.getBalance()));
        creditCardAccount.setCreditLimit(new Money(creditCardAccountDTO.getCreditLimit()));
        creditCardAccount.setInterestRate(creditCardAccountDTO.getInterestRate());

        return saveCreditCardAccount(creditCardAccount);
    }

    public Account startMoneyTransfer(CustomUserDetails userDetails, MoneyTransferDTO moneyTransferDTO, Long id) {

        // check the existence of the referenced accounts
        if(!existsAccount(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin account not found");
        if(!existsAccount(moneyTransferDTO.getToAccountId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination account not found");

        // check the accounts are not the same
        if(id.equals(moneyTransferDTO.getToAccountId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin and destination accounts can't match");

        Account account = accountRepository.findById(id).get();

        // check the name of the author (it's name is the primary or secondary owner's)
        String primaryName = account.getPrimaryOwner().getName();
        String secondaryName = account.hasSecondaryOwner() ? account.getSecondaryOwner().getName() : "";

        String transferName = moneyTransferDTO.getName();

        if (!transferName.equalsIgnoreCase(primaryName) && !transferName.equalsIgnoreCase(secondaryName))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name provided does not match with owner's name");


        // everything ok, next checks are MoneyTransferService responsibility
        account = moneyTransferService.doMoneyTransfer(moneyTransferDTO, id);

        return saveAccount(account);
    }

    public void updateBalance(NewBalanceDTO newBalanceDTO, Long id) {
        if(!existsAccount(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");

        Account account = accountRepository.findById(id).get();
        account.setBalance(new Money(newBalanceDTO.getBalance()));
        saveAccount(account);
    }

    private CheckingAccount saveCheckingAccount(CheckingAccount account) {
        return checkingAccountRepository.save(account);
    }

    private StudentCheckingAccount saveStudentCheckingAccount(StudentCheckingAccount account) {
        return studentCheckingAccountRepository.save(account);
    }

    private SavingsAccount saveSavingsAccount(SavingsAccount account) {
        return savingsAccountRepository.save(account);
    }

    private CreditCardAccount saveCreditCardAccount(CreditCardAccount account) {
        return creditCardAccountRepository.save(account);
    }

    public Account saveAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }
}
