package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.VIPCurrentUserAccount;
import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Exception.WithdrawException;
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

    Long transferInCardId = 1L;

    Long queryBalanceCardId = 1L;

    Card toCard;

    @BeforeEach
    void setUp() {
        personalUserAccount = vipCurrentUserAccount;
        Optional<PersonalCard> personalCard = personalCardRepository.findById(zhaoLiuCardId);
        User user = userRepository.findByCard(personalCard.get());

        personalUserAccount.setPersonalCard(personalCard.get());
        personalUserAccount.setUser(user);

        baseAccount = vipCurrentUserAccount;

        toCard = cardRepository.findById(transferInCardId).get();
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
        assert queryBalance.equals(BLLUtil.presentQueryBalanceResult(balance, balance));
    }

    @Test
    void queryBalanceNotHavingFixedRecord() {
        String queryBalance = baseAccount.queryBalance();
        double balance = baseAccount.getCard().getBalance();
        assert queryBalance.equals(BLLUtil.presentQueryBalanceResult(balance, balance));
    }

    @Test
    void transferTo() throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        baseAccount.transferMoneyTo(toCard,1);
    }

    @Test
    void closeAccount() {
    }
}