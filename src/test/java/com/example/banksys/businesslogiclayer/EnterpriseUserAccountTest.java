package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.EnterpriseUser;
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

    @BeforeEach
    void setUp() {
        Optional<Enterprise> enterprise = enterpriseRepository.findById(6L);

        Optional<EnterpriseUser> enterpriseUser = enterpriseUserRepository.findById(1L);

        enterpriseCurrentUserAccount.setEnterprise(enterprise.get());
        enterpriseCurrentUserAccount.setEnterpriseUser(enterpriseUser.get());
    }

    @Test
    void openAccountTestParam() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101111X", "张三", Card.UserType.ENTERPRISE, "1111111", 6L, Card.CardType.CURRENT, 10001);
//        Optional<EnterpriseCard> card = enterpriseCurrentUserAccount.getEnterpriseCardRepository().findById(cardId);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        assert card.get().getUserType() == "test";
    }

    @Test //test checkout
    void openAccountSUPERTest() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101111X", "张三", Card.UserType.ENTERPRISE, "1111111", 6L, Card.CardType.CURRENT, 10001);
//        Optional<EnterpriseCard> card = enterpriseCurrentUserAccount.getEnterpriseCardRepository().findById(cardId);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        Optional<EnterpriseUser> user = enterpriseUserRepository.findById(card.get().getUserId());
        assert user.get().getRightType()   == EnterpriseUser.RightType.SUPER;
    }

    @Test //test checkout
    void openAccountUSERTest() {
        long cardId = enterpriseCurrentUserAccount.openEnterpriseAccount(1, "11111120000101111X", "张三", Card.UserType.ENTERPRISE, "1111111", 6L, Card.CardType.CURRENT, 10001);
//        Optional<EnterpriseCard> card = enterpriseCurrentUserAccount.getEnterpriseCardRepository().findById(cardId);
        Optional<Card> card = enterpriseCurrentUserAccount.getCardRepository().findById(cardId);
        Optional<EnterpriseUser> user = enterpriseUserRepository.findById(card.get().getUserId());
        assert user.get().getRightType().equals(EnterpriseUser.RightType.USER);
    }
}