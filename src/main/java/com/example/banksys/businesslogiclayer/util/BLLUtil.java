package com.example.banksys.businesslogiclayer.util;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;

import java.util.Calendar;
import java.util.Date;

public class BLLUtil {

    /**
     * 自行活期存款
     * @param cardRepository
     * @param tradeRepository
     * @param card
     * @param money
     * @return 存款后余额
     */
    public static double currentDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double currentDepositByEmployee(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Employee employee) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), employee, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double fixedDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, int depositDays) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Date expireDate = getExpireDate(depositDays);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.FIXED, money, expireDate, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double fixedDepositByEmployee(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Employee employee, int depositDays) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Date expireDate = getExpireDate(depositDays);

        Trade trade = new Trade(card.getCardId(), employee, Trade.TradeType.FIXED, money, expireDate, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static Date getExpireDate(int depositDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, depositDays);
        Date expireDate = calendar.getTime();
        return expireDate;
    }

    public static double queryBalance(Card card) {
        return card.getBalance();
    }

    public static double queryDesirableBalance(TradeRepository tradeRepository, Card card) {
        double fixedBalance = tradeRepository.findFixedBalance(card);
        return card.getBalance() - fixedBalance;
    }

    public static String presentQueryBalanceResult(double desirableBalance, double balance) {
        return desirableBalance + "/" + balance;
    }

    public static double withdrawFixedAccount(TradeRepository tradeRepository, CardRepository cardRepository, Card card, Employee employee, double money) throws WithdrawException {
        double desirableBalance = BLLUtil.queryDesirableBalance(tradeRepository,card);
        if (desirableBalance < money) {
            throw new WithdrawException("定期账户可取余额不足" + money + "元！");
        }
        double oldBalance = card.getBalance();
        double newBalance = oldBalance - money;
        card.setBalance(newBalance);
        cardRepository.save(card);
        Trade trade = new Trade(card.getCardId(), employee, Trade.TradeType.WITHDRAW, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static void checkDesirableBalanceBeforeTransfer(TradeRepository tradeRepository, Card card, double money) throws WithdrawException {
        if (BLLUtil.queryDesirableBalance(tradeRepository, card) < money) {
            throw new WithdrawException("可取余额不足" + money + "元！");
        }
    }
}
