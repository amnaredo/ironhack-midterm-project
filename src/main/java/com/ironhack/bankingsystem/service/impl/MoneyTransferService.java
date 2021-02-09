package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.Money;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.enums.Type;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.service.interfaces.IAccountService;
import com.ironhack.bankingsystem.service.interfaces.IMoneyTransferService;
import com.ironhack.bankingsystem.service.interfaces.IOwnerService;
import com.ironhack.bankingsystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MoneyTransferService implements IMoneyTransferService {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IOwnerService ownerService;
    @Autowired
    private ITransactionService transactionService;

    public Transaction doMoneyTransfer(MoneyTransferDTO moneyTransferDTO, Long id) {

        Account origin = accountService.getAccountById(id);
        Account destination = accountService.getAccountById(moneyTransferDTO.getToAccountId());

        // check enough funds in origin account
        BigDecimal currentBalance = origin.getBalance().getAmount();
        BigDecimal transferAmount = moneyTransferDTO.getAmount();
        if (transferAmount.compareTo(currentBalance) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount exceeds balance of the account");

        // check if penalty fee has to be deducted later
        BigDecimal result = currentBalance.subtract(transferAmount);
        Boolean applyPenaltyFee =
                currentBalance.compareTo(origin.getMinimumBalance().getAmount()) > 0 &&
                result.compareTo(origin.getMinimumBalance().getAmount()) < 0;

        // make the transaction
        Transaction transaction = new Transaction(new Money(transferAmount));
        transaction.setFromAccount(origin);
        transaction.setToAccount(destination);
        //transaction.setAmount(new Money(transferAmount));
        transaction.setAuthorName(moneyTransferDTO.getName());
        transaction.setDescription(moneyTransferDTO.getDescription());

        Transaction newTransaction = transactionService.addTransaction(transaction);

        // deduct penalty fee with another transaction if needed
        if (applyPenaltyFee) {
            try {
                Thread.sleep(2000L); // wait 2 seconds to avoid fraud detection
            } catch (Exception e) { }

            Transaction deductionTransaction = new Transaction(origin.getPenaltyFee());
            deductionTransaction.setFromAccount(origin);
            //deductionTransaction.setToAccount(null);
            //deductionTransaction.setAmount(origin.getPenaltyFee()); // set amount before accounts
            deductionTransaction.setAuthorName(moneyTransferDTO.getName());
            deductionTransaction.setDescription("Penalty fee deduction");

            transactionService.addTransaction(deductionTransaction);
        }

        return newTransaction;
    }
}
