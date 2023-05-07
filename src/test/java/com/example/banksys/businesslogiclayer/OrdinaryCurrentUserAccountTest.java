package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.model.Card;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrdinaryCurrentUserAccountTest {

    @Autowired
    OrdinaryCurrentUserAccount ordinaryCurrentUserAccount;

    @Autowired
    EnterpriseCurrentUserAccount enterpriseCurrentUserAccount;

    @Autowired
    EnterpriseFixedUserAccount enterpriseFixedUserAccount;

    @BeforeEach
    void setUp( @Autowired CardRepository cardRepository) {
        Optional<Card> card = cardRepository.findById(1L);
//        account.setCard(card.get());
//        enterpriseCurrentUserAccount.setCard(card.get());
        enterpriseFixedUserAccount.setCard(card.get());
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
}