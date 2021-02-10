package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.repository.transaction.TransactionRepository;
import com.ironhack.bankingsystem.service.interfaces.IFraudDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionService implements IFraudDetectionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public void checkMoneyTransfer(Account account, MoneyTransferDTO moneyTransferDTO) {

        // first check if the fraud detection applies (i.e., implements WithStatus interface)
        if (!WithStatus.class.isAssignableFrom(account.getClass()))
            return;

        // More than 2 transactions occurring on a single account within a 1 second period.

        // get the number of transactions of this account in the last second
        Integer numberOfTransactionsInThisSecond =
                transactionRepository.findCountBetweenPeriod(
                        account,
                        LocalDateTime.now().minusSeconds(1), // a second ago
                        LocalDateTime.now()); // now

        // check the condition
        if (numberOfTransactionsInThisSecond >= 2){
            ((WithStatus)account).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account frozen: too many transactions");
        }

        // Transactions made in 24 hours that total to more than 150% of the customers highest daily total transactions
        // in any other 24 hour period.

        // get the highest daily total
        List<Object[]> dailyTotalsResult = transactionRepository.findDailyTotalByDateOrderedDesc(account);
        BigDecimal highestDailyTotal = (BigDecimal)dailyTotalsResult.get(0)[1];

        // get the total of transactions on this day
        List<Object[]> todayTotalResult =
                transactionRepository.findTotalInDate(account, LocalDateTime.now().format(Transaction.DATE_FORMATTER));
        BigDecimal todayTotal = (BigDecimal)dailyTotalsResult.get(0)[1];

        // sum the amount of the current transfer
        BigDecimal amount = moneyTransferDTO.getAmount();
        BigDecimal newTodayTotal = todayTotal.add(amount);

        // check the condition
        if (newTodayTotal.compareTo(highestDailyTotal.multiply(BigDecimal.valueOf(1.5))) > 0) {
            ((WithStatus)account).setStatus(Status.FROZEN);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account frozen: suspected fraud");
        }
    }
}
