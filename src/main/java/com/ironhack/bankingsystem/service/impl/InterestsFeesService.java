package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.interfaces.WithAnnualInterest;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyFee;
import com.ironhack.bankingsystem.model.account.interfaces.WithMonthlyInterest;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.IInterestsFeesService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class InterestsFeesService implements IInterestsFeesService {

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IAccountService accountService;

    public Account applyInterestsFeesService(Account account) {

        if(WithMonthlyFee.class.isAssignableFrom(account.getClass()))
            applyMonthlyFee((WithMonthlyFee) account);

        if(WithMonthlyInterest.class.isAssignableFrom(account.getClass()))
            applyMonthlyInterest((WithMonthlyInterest) account);

        if(WithAnnualInterest.class.isAssignableFrom(account.getClass()))
            applyAnnualInterest((WithAnnualInterest) account);

        return accountService.addAccount(account);
    }

    public void applyMonthlyInterest(WithMonthlyInterest account) {
        LocalDateTime interestAddedDateTime = account.getInterestAddedDateTime();
        if (account.getMonthsSinceLastInterestAdded() > 0) {

            // new transaction to reflect the payment of interests.
            Transaction transaction = new Transaction(account.getLastInterestGenerated());
            //transaction.setFromAccount(null);
            transaction.setToAccount((Account) account);
            transaction.setAuthorName("SantanderBank");
            String dateString = interestAddedDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            transaction.setDescription("Monthly interests generated since " + dateString);
            transactionService.addTransaction(transaction);

            // update account info
            account.updateInterestAddedDateTime();
        }
    }

    public void applyAnnualInterest(WithAnnualInterest account) {
        LocalDateTime interestAddedDateTime = account.getInterestAddedDateTime();
        if (account.getYearsSinceLastInterestAdded() > 0) {

            // new transaction to reflect the payment of interests.
            Transaction transaction = new Transaction(account.getLastInterestGenerated());
            //transaction.setFromAccount(null);
            transaction.setToAccount((Account) account);
            transaction.setAuthorName("SantanderBank");
            String dateString = interestAddedDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            transaction.setDescription("Annual interests generated since " + dateString);
            transactionService.addTransaction(transaction);

            // update account info
            account.updateInterestAddedDateTime();
        }
    }

    public void applyMonthlyFee(WithMonthlyFee account) {
        LocalDateTime feeAppliedDateTime = account.getMonthlyFeeAppliedDateTime();
        if (account.getMonthsSinceLastMonthlyFeeDeduction() > 0) {

            // new transaction to reflect the deduction of fees
            Transaction transaction = new Transaction(account.getMonthlyMaintenanceFee());
            transaction.setFromAccount((Account) account);
            //transaction.setToAccount(null);
            transaction.setAuthorName("SantanderBank");
            String dateString = feeAppliedDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            transaction.setDescription("Maintenance fee deductions since " + dateString);

            transactionService.addTransaction(transaction);

            // update account info
            account.updateMonthlyFeeAppliedDateTime();
        }
    }
}
