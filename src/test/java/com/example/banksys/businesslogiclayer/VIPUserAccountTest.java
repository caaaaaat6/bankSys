package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.lang.model.type.UnionType;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VIPUserAccountTest {

    @Autowired
    PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VIPCurrentUserAccount vipCurrentUserAccount;

    PersonalUserAccount personalUserAccount;

    @BeforeEach
    void setUp() {
        personalUserAccount = vipCurrentUserAccount;
        Optional<PersonalCard> personalCard = personalCardRepository.findById(30L);
        User user = userRepository.findByCard(personalCard.get());

        personalUserAccount.setPersonalCard(personalCard.get());
        personalUserAccount.setUser(user);
    }

    @Test
    void withdraw() throws WithdrawException {
        personalUserAccount.withdraw(1);
        Optional<PersonalCard> personalCard = personalCardRepository.findById(30L);
        assert personalCard.get().getUserType().equals(Card.UserType.ORDINARY);

    }

    @Test
    void updateUserTypeTest() {
        Optional<PersonalCard> byId = personalCardRepository.findById(30L);
        byId.get().setUserType(Card.UserType.ORDINARY);
        personalCardRepository.save(byId.get());
        assert personalCardRepository.findById(30L).get().getUserType().equals(Card.UserType.ORDINARY);
    }

}