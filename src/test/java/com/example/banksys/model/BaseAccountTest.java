package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.PersonalUserAccount;
import com.example.banksys.businesslogiclayer.VIPCurrentUserAccount;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BaseAccountTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VIPCurrentUserAccount vipCurrentUserAccount;

    PersonalUserAccount personalUserAccount;

    BaseAccount baseAccount;

    Long zhaoLiuCardId = 30L;

    Long queryBalanceCardId = 1L;

    @BeforeEach
    void setUp() {
        personalUserAccount = vipCurrentUserAccount;
        Optional<PersonalCard> personalCard = personalCardRepository.findById(queryBalanceCardId);
        User user = userRepository.findByCard(personalCard.get());

        personalUserAccount.setPersonalCard(personalCard.get());
        personalUserAccount.setUser(user);

        baseAccount = vipCurrentUserAccount;
    }

    @Test
    void openAccount() {
//        Card card = new Card("11111120000101111X","张三",Card.UserType.ORDINARY,100);
//        baseAccount.openAccount(1, "11111120000101111X", "张三", Card.UserType.ORDINARY, "123456", Card.CardType.CURRENT, 100);
        Optional<Card> byId = cardRepository.findById(1L);
        System.out.println(byId);
        assertNotNull(byId);
    }

    @Test
    void withdrawal() {
    }

    @Test
    void queryBalanceHavingFixedRecord() {
        String queryBalance = baseAccount.queryBalance();
        double balance = baseAccount.getCard().getBalance();
        assert queryBalance.equals(BLLUtil.presentQueryBalanceResult(balance - 1, balance));
    }

    @Test
    void queryBalanceNotHavingFixedRecord() {
        String queryBalance = baseAccount.queryBalance();
        double balance = baseAccount.getCard().getBalance();
        assert queryBalance.equals(BLLUtil.presentQueryBalanceResult(balance, balance));
    }

    @Test
    void transferMoneySelf() {
    }

    @Test
    void closeAccount() {
    }
}