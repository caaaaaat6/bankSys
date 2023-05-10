package com.example.banksys.businesslogiclayer.util;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BLLUtilTest {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    CardRepository cardRepository;

    Card card;

    Long zhaoLiuCardId = 30L;

    @BeforeEach
    void setup() {
        card = cardRepository.findById(zhaoLiuCardId).get();
    }

    @Test
    void queryDesirableBalance() {
        double v = BLLUtil.queryDesirableBalance(tradeRepository, card);
        assert  v == card.getBalance();
    }

    @Test
    void queryDesirableBalanceHavingFixedRecord() {
        double v = BLLUtil.queryDesirableBalance(tradeRepository, card);
        assert  v == card.getBalance() - 1;
    }
}