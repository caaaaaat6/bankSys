package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.useraccount.OrdinaryFixedUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrdinaryUserAccountTest {

    @Autowired
    PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrdinaryFixedUserAccount ordinaryFixedUserAccount;

    PersonalUserAccount personalUserAccount;

    @BeforeEach
    void setUp() {
        PersonalCard card = personalCardRepository.findById(1L).get();
        ordinaryFixedUserAccount.setPersonalCard(card);

        User user = userRepository.findByCard(card);
        ordinaryFixedUserAccount.setUser(user);

        personalUserAccount = ordinaryFixedUserAccount;

    }

    @Test
    void withdraw() throws WithdrawException {
        double withdrawal = 1;
        double oldBalance = personalUserAccount.getCard().getBalance();
        double balance = personalUserAccount.withdraw(withdrawal);
        assert balance == oldBalance - withdrawal;
    }
}