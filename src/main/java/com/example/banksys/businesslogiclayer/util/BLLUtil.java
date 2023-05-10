package com.example.banksys.businesslogiclayer.util;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
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
     * @return
     */
    public static double currentDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double currentDepositByEmployee(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double fixedDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, int depositDays) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Date expireDate = getExpireDate(depositDays);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.FIXED, money, new Date(), expireDate);
        tradeRepository.save(trade);
        return newBalance;
    }

    public static double fixedDepositByEmployee(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId, int depositDays) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Date expireDate = getExpireDate(depositDays);

        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.FIXED, money, new Date(), expireDate);
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
        double fixedBalance = tradeRepository.findFixedBalance();
        return card.getBalance() - fixedBalance;
    }
}
