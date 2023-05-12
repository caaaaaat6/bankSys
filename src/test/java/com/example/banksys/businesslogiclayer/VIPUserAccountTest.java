package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.VIPCurrentUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
class VIPUserAccountTest {

    @Autowired
    PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VIPCurrentUserAccount vipCurrentUserAccount;

    @Autowired
    EnterpriseCardRepository enterpriseCardRepository;

    PersonalUserAccount personalUserAccount;

    Card toCard;

    Card toEnterpriseCard;

    Long toPersonalCardId = 1L;

    Long toEnterpriseCardId = 17L;

    @BeforeEach
    void setUp() {
        personalUserAccount = vipCurrentUserAccount;
        Optional<PersonalCard> personalCard = personalCardRepository.findById(30L);
        User user = userRepository.findByCard(personalCard.get());

        vipCurrentUserAccount.setPersonalCard(personalCard.get());
        vipCurrentUserAccount.setUser(user);
//        personalUserAccount.setPersonalCard(personalCard.get());
//        personalUserAccount.setUser(user);

        toCard = personalCardRepository.findById(toPersonalCardId).get();

        toEnterpriseCard = enterpriseCardRepository.findById(toEnterpriseCardId).get();

        personalUserAccount = vipCurrentUserAccount;
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

    @Test
    void transferTo() throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        double money = 1;
        double oldBalance = personalUserAccount.getCard().getBalance();
        double newBalance = personalUserAccount.transferMoneyTo(toCard, money);
        assert newBalance == oldBalance - money;
    }

    @Test
    void checkTransferTo() throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        double money = 1;
        Assert.assertThrows(UntransferableException.class, () -> personalUserAccount.transferMoneyTo(toEnterpriseCard, money));
    }

}