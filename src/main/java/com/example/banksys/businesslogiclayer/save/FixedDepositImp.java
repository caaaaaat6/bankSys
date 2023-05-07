package com.example.banksys.businesslogiclayer.save;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Trade;

import java.util.Calendar;
import java.util.Date;

public class FixedDepositImp implements FixedDeposit {

    @Override
    public double deposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId, int depositDays) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, depositDays);
        Date expireDate = calendar.getTime();

        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.CURRENT, money, new Date(), expireDate);
        tradeRepository.save(trade);
        return newBalance;
    }
}
