package com.example.banksys.businesslogiclayer.save;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Trade;

import java.util.Date;

public class CurrentDepositImp implements CurrentDeposit {

    @Override
    public double deposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId) {
        double newBalance = card.save(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }
}
