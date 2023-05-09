package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.PersonalCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PersonalCardRepositoryTest {

    @Autowired
    PersonalCardRepository cardRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findPersonalCardsByUserPid() {
        List<PersonalCard> cardList = cardRepository.findPersonalCardsByUserPid("11111120000101111X").get();
        System.out.println("-----------------------------" + cardList.size());
        assert cardList.size() == 1;
    }
}