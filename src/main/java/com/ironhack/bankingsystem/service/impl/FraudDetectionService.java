package com.ironhack.bankingsystem.service.impl;

import com.ironhack.bankingsystem.dto.account.MoneyTransferDTO;
import com.ironhack.bankingsystem.model.account.Account;
import com.ironhack.bankingsystem.model.account.enums.Status;
import com.ironhack.bankingsystem.model.account.interfaces.WithStatus;
import com.ironhack.bankingsystem.model.transaction.Transaction;
import com.ironhack.bankingsystem.repository.account.AccountRepository;
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
    @Autowired
    private AccountRepository accountRepository;


//    public void checkMoneyTransfer(Account account, MoneyTransferDTO moneyTransferDTO) {
//
//        // first check if the fraud detection applies (i.e., implements WithStatus interface)
//        if (!WithStatus.class.isAssignableFrom(account.getClass()))
//            return;
//
//        // More than 2 transactions occurring on a single account within a 1 second period.
//
//        // get the number of transactions of this account in the last second
//        Integer numberOfTransactionsInThisSecond =
//                transactionRepository.findCountBetweenPeriod(
//                        account,
//                        LocalDateTime.now().minusSeconds(1), // a second ago
//                        LocalDateTime.now()); // now
//
//        // check the condition
//        if (numberOfTransactionsInThisSecond >= 2){
//            ((WithStatus)account).setStatus(Status.FROZEN);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account frozen: too many transactions");
//        }
//
//        // Transactions made in 24 hours that total to more than 150% of the customers highest daily total transactions
//        // in any other 24 hour period.
//
//        // get the highest daily total
//        List<Object[]> dailyTotalsResult = transactionRepository.findDailyTotalByDateOrderedDesc(account);
//        BigDecimal highestDailyTotal = (BigDecimal)dailyTotalsResult.get(0)[1];
//
//        // get the total of transactions on this day
//        List<Object[]> todayTotalResult =
//                transactionRepository.findTotalInDate(account, LocalDateTime.now().format(Transaction.DATE_FORMATTER));
//        BigDecimal todayTotal = (BigDecimal)dailyTotalsResult.get(0)[1];
//
//        // sum the amount of the current transfer
//        BigDecimal amount = moneyTransferDTO.getAmount();
//        BigDecimal newTodayTotal = todayTotal.add(amount);
//
//        // check the condition
//        if (newTodayTotal.compareTo(highestDailyTotal.multiply(BigDecimal.valueOf(1.5))) > 0) {
//            ((WithStatus)account).setStatus(Status.FROZEN);
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account frozen: suspected fraud");
//        }
//    }


    public void checkMoneyTransferV2(Account account, MoneyTransferDTO moneyTransferDTO) {

        // first check if the fraud detection applies (i.e., implements WithStatus interface)
        if (!WithStatus.class.isAssignableFrom(account.getClass()))
            return;

        // check the account is not frozen
        if (((WithStatus)account).getStatus().equals(Status.FROZEN))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen due to suspected fraud");


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
            accountRepository.save(account);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account frozen: too many transactions");
        }

        // Transactions made in 24 hours that total to more than 150% of the customers highest daily total transactions
        // in any other 24 hour period.

        // get the highest daily total
        BigDecimal highestDailyTotal = getHighestTotalIn24HPeriods(account);

        // get the total of transactions on this day
        BigDecimal todayTotal = getTotalInLast24H(account);

        // sum the amount of the current transfer
        BigDecimal amount = moneyTransferDTO.getAmount();
        BigDecimal newTodayTotal = todayTotal.add(amount);

        // check the condition
        if (highestDailyTotal.compareTo(BigDecimal.ZERO) > 0 &&
            newTodayTotal.compareTo(highestDailyTotal.multiply(BigDecimal.valueOf(1.5))) > 0) {
            ((WithStatus)account).setStatus(Status.FROZEN);
            accountRepository.save(account);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account frozen: suspected fraud");
        }
    }

    private BigDecimal getHighestTotalIn24HPeriods(Account account) {
        BigDecimal highestTotal = BigDecimal.ZERO;
        List<Transaction> transactions =
                transactionRepository.findByFromAccountOrToAccountOrderByTimestampDesc(account, account);

        for(Transaction tx : transactions) {
            BigDecimal totalIn24H =
                    transactionRepository.findTotalInPeriod(
                            account,
                            tx.getTimestamp().minusHours(24L),
                            tx.getTimestamp());
            if (totalIn24H.compareTo(highestTotal) > 0)
                highestTotal = totalIn24H;
        }
        return highestTotal;
    }

    private BigDecimal getTotalInLast24H(Account account) {
        BigDecimal total =
            transactionRepository.findTotalInPeriod(
                account,
                LocalDateTime.now().minusHours(24L),
                LocalDateTime.now());

        return total != null ? total : BigDecimal.ZERO;
    }

}
