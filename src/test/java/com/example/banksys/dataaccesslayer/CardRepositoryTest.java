package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.EnterpriseCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    EnterpriseCardRepository enterpriseCardRepository;

    @Test
    void findCardsByUserPid() {
        List<Card> cardsByUserPid = cardRepository.findCardsByUserPid("11111120000101111X");
        int size = cardsByUserPid.size();
        System.out.println("-----------------------------" + size);
        assert size == 7;
    }

    @Test
    void findEnterpriseCardByUserPid() {
        List<EnterpriseCard> cardList = enterpriseCardRepository.findEnterpriseCardsByUserPid("11111120000101111X");
        System.out.println("-----------------------------" + cardList.size());
        assert cardList.size() == 7;
    }
}