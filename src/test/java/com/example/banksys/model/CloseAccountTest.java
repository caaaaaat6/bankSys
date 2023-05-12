package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.EnterpriseFixedUserAccount;
import com.example.banksys.businesslogiclayer.PersonalUserAccount;
import com.example.banksys.businesslogiclayer.VIPCurrentUserAccount;
import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Exception.WithdrawException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CloseAccountTest {

    @Autowired
    EnterpriseCardRepository enterpriseCardRepository;

    @Autowired
    EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    EnterpriseFixedUserAccount enterpriseFixedUserAccount;

    @Autowired
    PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VIPCurrentUserAccount vipCurrentUserAccount;

    PersonalUserAccount personalUserAccount;

    PersonalCard personalCard;

    User user;

    Long personalCardId = 30L;

    EnterpriseCard enterpriseCard;

    EnterpriseUser enterpriseUser;

    Long enterpriseCardId = 32L;

    @BeforeEach
    void setup() {
        enterpriseCard = enterpriseCardRepository.findById(enterpriseCardId).get();
        enterpriseUser = enterpriseUserRepository.findById(enterpriseCard.getUserId()).get();
        Enterprise enterprise = enterpriseUser.getEnterprise();

        enterpriseFixedUserAccount.setEnterprise(enterprise);
        enterpriseFixedUserAccount.setEnterpriseCard(enterpriseCard);
        enterpriseFixedUserAccount.setEnterpriseUser(enterpriseUser);

        personalUserAccount = vipCurrentUserAccount;
        personalCard = personalCardRepository.findById(personalCardId).get();
        user = userRepository.findByCard(personalCard);
        personalUserAccount.setPersonalCard(personalCard);
        personalUserAccount.setUser(user);
    }

    @Test
    void closeEnterpriseAccountTest() throws WithdrawException {
        List<Long> userIdList = new ArrayList<>();
        enterpriseFixedUserAccount.getEnterprise().getEnterpriseUserList().forEach(enterpriseUser1 -> userIdList.add(enterpriseUser1.getUserId()));

        enterpriseFixedUserAccount.closeAccount();

        List<EnterpriseUser> allById = (List<EnterpriseUser>) enterpriseUserRepository.findAllById(userIdList);
        assert enterpriseCardRepository.findById(enterpriseCardId).isEmpty() && (allById == null || allById.size() == 0);
    }

    @Test
    void closePersonalAccountTest() {
        Long userId = user.getUserId();

        personalUserAccount.closeAccount();

        Optional<User> deletedUser = userRepository.findById(userId);
        Optional<PersonalCard> deletedPersonalCard = personalCardRepository.findById(personalCardId);

        assert deletedUser.isEmpty() && deletedPersonalCard.isEmpty();
    }
}
