package com.example.banksys;

import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankSysApplicationTests {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void contextLoads() {
//        tradeRepository.save(new Trade("1",1L,Trade.TradeType.CURRENT,1,new Date(), new Date()));
        Iterable<Trade> all = tradeRepository.findAll();
        all.forEach(System.out::println);
    }

}
