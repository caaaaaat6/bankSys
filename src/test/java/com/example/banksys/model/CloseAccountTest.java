package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.EnterpriseFixedUserAccount;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Exception.WithdrawException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CloseAccountTest {

    @Autowired
    EnterpriseCardRepository enterpriseCardRepository;

    @Autowired
    EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    EnterpriseFixedUserAccount enterpriseFixedUserAccount;

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
    }

    @Test
    void closeTest() throws WithdrawException {
        List<Long> userIdList = new ArrayList<>();
        enterpriseFixedUserAccount.getEnterprise().getEnterpriseUserList().forEach(enterpriseUser1 -> userIdList.add(enterpriseUser1.getUserId()));

        enterpriseFixedUserAccount.closeAccount();

        List<EnterpriseUser> allById = (List<EnterpriseUser>) enterpriseUserRepository.findAllById(userIdList);
        assert enterpriseCardRepository.findById(enterpriseCardId).isEmpty() && (allById == null || allById.size() == 0);
    }
}
