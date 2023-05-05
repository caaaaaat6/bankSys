package com.example.banksys.model;

import com.example.banksys.dataaccesslayer.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BaseAccountTest {

    @Autowired
    protected CardRepository cardRepository;

//    @Autowired
//    private BaseAccount baseAccount;

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
    void searchRecord() {
    }

    @Test
    void transferMoneySelf() {
    }

    @Test
    void closeAccount() {
    }
}