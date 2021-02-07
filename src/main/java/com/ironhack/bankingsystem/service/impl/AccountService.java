package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.account.*;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.repository.account.*;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccountService implements IAccountService {

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

    public Account addAccount(Account account) {
        switch (account.getType()) {
            case CHECKING:
                return addCheckingAccount((CheckingAccount) account);
            case STUDENT_CHECKING:
                return addStudentCheckingAccount((StudentCheckingAccount) account);
            case SAVINGS:
                return addSavingsAccount((SavingsAccount) account);
            case CREDIT_CARD:
                return addCreditCardAccount((CreditCardAccount) account);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account type does not exist");
        }
    }

    public CheckingAccount addCheckingAccount(CheckingAccount account) {
        return checkingAccountRepository.save(account);
    }

    public StudentCheckingAccount addStudentCheckingAccount(StudentCheckingAccount account) {
        return studentCheckingAccountRepository.save(account);
    }

    public SavingsAccount addSavingsAccount(SavingsAccount account) {
        return savingsAccountRepository.save(account);
    }

    public CreditCardAccount addCreditCardAccount(CreditCardAccount account) {
        return creditCardAccountRepository.save(account);
    }

    public void deleteAll(){
        accountRepository.deleteAll();
    }
}
