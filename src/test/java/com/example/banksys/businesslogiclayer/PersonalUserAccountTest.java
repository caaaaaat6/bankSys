package com.example.banksys.businesslogiclayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class PersonalUserAccountTest {

    @Autowired
    private OrdinaryFixedUserAccount ordinaryFixedUserAccount;

    private PersonalUserAccount personalUserAccount;

    @BeforeEach
    void setup() {
        personalUserAccount = ordinaryFixedUserAccount;
        Optional<PersonalCard> personalCard = personalUserAccount.getPersonalCardRepository().findById(1L);
        personalUserAccount.setPersonalCard(personalCard.get());
    }

    @Test
    void openAccountTest() {
        personalUserAccount.openAccount(1, "11111120000101111X", "赵六", Card.UserType.ORDINARY, "123456",null, Card.CardType.CURRENT, 10001, null);
    }

    @Test
    void withdrawalTest() throws WithdrawException {
        double beforeWithdraw = personalUserAccount.getPersonalCard().getBalance();
        personalUserAccount.withdraw(1);
        double afterWithdraw = personalUserAccount.getPersonalCard().getBalance();
        assert afterWithdraw == beforeWithdraw - 1;
    }
}