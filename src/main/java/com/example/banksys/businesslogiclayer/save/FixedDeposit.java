package com.example.banksys.businesslogiclayer.save;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;

public interface FixedDeposit {

    /**
     * 定期存款
     * @param depositDays 定期天数
     * @return 存款后的余额
     */
    double deposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, Long employeeId, int depositDays);
}
