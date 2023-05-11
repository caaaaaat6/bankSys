package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseCardExsitException;
import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.Exception.WithdrawException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
class EnterpriseUserAccountTest {

    @Autowired
    EnterpriseCurrentUserAccount enterpriseCurrentUserAccount;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    EnterpriseCardRepository enterpriseCardRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OrdinaryCurrentUserAccount ordinaryCurrentUserAccount;

    EnterpriseUserAccount enterpriseUserAccount;

    long zhaoLiuId = 12L;

    long zhangSanId = 1L;

    Card toCard;

    Long toCardId = 1L;

    Card toOrdinaryCard;

    Long toOrdinaryCardId = 1L;

    @BeforeEach
    void setUp() {
        Optional<Enterprise> enterprise = enterpriseRepository.findById(6L);

        Optional<EnterpriseUser> enterpriseUser = enterpriseUserRepository.findById(zhangSanId);

        EnterpriseCard enterpriseCard = enterpriseUser.get().getEnterpriseCard();

        toCard = cardRepository.findById(toCardId). get();

        enterpriseCurrentUserAccount.setEnterprise(enterprise.get());
        enterpriseCurrentUserAccount.setEnterpriseUser(enterpriseUser.get());

        enterpriseUserAccount = enterpriseCurrentUserAccount;
        enterpriseUserAccount.setEnterpriseCard(enterpriseCard);

        toOrdinaryCard = cardRepository.findById(toOrdinaryCardId).get();
    }

    @Test
    void openAccountTestParam() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101111X", "张三", "1111111", 6L, Card.CardType.CURRENT, 10001, null);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        assert card.get().getUserType() == "test";
    }

    @Test //test checkout
    void openAccountSUPERTest() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101111X", "张三", "1111111", 6L, Card.CardType.CURRENT, 10001, null);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        Optional<EnterpriseUser> user = enterpriseUserRepository.findById(card.get().getUserId());
        assert user.get().getRightType()   == EnterpriseUser.RightType.SUPER;
    }

    @Test //test checkout
    void openAccountUSERTest() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101003X", "董八", "1111111", 6L, Card.CardType.CURRENT, 10001, null);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        Optional<EnterpriseUser> user = enterpriseUserRepository.findById(card.get().getUserId());
        assert user.get().getRightType().equals(EnterpriseUser.RightType.USER);
    }

    @Test
    void openAccountAndCreateEnterpriseUser() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1L,"11111120000101111X", "张三", "1111111", 6L, Card.CardType.CURRENT, 10001, null);

    }

    @Test
    void withdraw() throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        double money = 1;
        long cardId = enterpriseUserAccount.getEnterpriseCard().getCardId();

        double oldBalance = enterpriseUserAccount.getEnterpriseCardRepository().findById(cardId).get().getBalance();
        enterpriseUserAccount.withdraw(money);
        double newBalance = enterpriseUserAccount.getEnterpriseCardRepository().findById(cardId).get().getBalance();

        assert oldBalance - money == newBalance;
    }

    @Test
    void transferMoneyTo() throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        double money = 1;
        double balance = enterpriseUserAccount.getCard().getBalance();
        double v = enterpriseUserAccount.transferMoneyTo(toCard, money);
        double newBalance = enterpriseUserAccount.getCard().getBalance();
        assert balance == newBalance + money;
    }

    @Test
    void transferToPersonal() {
        double money = 1;
        Assertions.assertThrows(UntransferableException.class,() -> enterpriseUserAccount.transferMoneyTo(toOrdinaryCard, money));
    }

    @Test
    void openEnterpriseAccountWithException() {
        Assert.assertThrows(EnterpriseCardExsitException.class, () -> enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101003X", "董八", "1111111", 6L, Card.CardType.CURRENT, 10001, null));
    }
}