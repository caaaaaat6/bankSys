package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseCurrentUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseFixedUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.OrdinaryCurrentUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.OrdinaryUserAccount;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class OrdinaryCurrentUserAccountTest {

    @Autowired
    OrdinaryCurrentUserAccount ordinaryCurrentUserAccount;

    @Autowired
    EnterpriseCurrentUserAccount enterpriseCurrentUserAccount;

    @Autowired
    EnterpriseFixedUserAccount enterpriseFixedUserAccount;

    Card fromOrdinaryCard;

    Long fromOrdinaryCardId = 1L;

    Card toVipCard;

    Long toVipCardId = 30L;

    Card toOrdinaryCard;

    Long toOrdinaryCardId = 22L;

    OrdinaryUserAccount ordinaryUserAccount;


    @BeforeEach
    void setUp( @Autowired CardRepository cardRepository) {
        Optional<Card> card = cardRepository.findById(1L);
        enterpriseFixedUserAccount.setCard(card.get());

        toVipCard = cardRepository.findById(toVipCardId).get();
        toOrdinaryCard = cardRepository.findById(toOrdinaryCardId).get();
        fromOrdinaryCard = cardRepository.findById(fromOrdinaryCardId).get();


        ordinaryUserAccount = ordinaryCurrentUserAccount;
        ordinaryUserAccount.setCard(fromOrdinaryCard);
    }

    @Test
    void deposit() {
//        enterpriseCurrentUserAccount.deposit(1);
//
        enterpriseFixedUserAccount.deposit(1, 1);

    }

    @Test
    void adviceThrowException() {
        try {
            enterpriseFixedUserAccount.deposit(1, 1);
        } catch (RuntimeException e) {
            System.out.println("Exception is caught");
        }
    }

    @Test
    void checkTransferToVip() {
        double money = 1;
        Assertions.assertThrows(UntransferableException.class, () -> ordinaryUserAccount.transferMoneyTo(toVipCard, money));
    }

    @Test
    void checkTransferToOtherOrdinary() {
        double money = 1;
        Assertions.assertThrows(UntransferableException.class, () -> ordinaryUserAccount.transferMoneyTo(toOrdinaryCard, money));
    }

    @Test
    void checkTransferToSelf() {
        double money = 1;
        Assertions.assertDoesNotThrow(() -> ordinaryUserAccount.transferMoneyTo(fromOrdinaryCard, money));
    }
}