package com.example.banksys.businesslogiclayer.save;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;

public interface CurrentDeposit {

    /**
     * 活期存款
     * @return 存款后的余额
     * @param cardRepository
     * @param tradeRepository
     * @param card
     * @param money
     * @param employeeId
     */
    double deposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId);
}
