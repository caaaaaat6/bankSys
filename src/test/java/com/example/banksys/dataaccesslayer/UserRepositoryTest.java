package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    // 测试UserRepository能不能读到EnterpriseUser?
    // 可以
    @Test
    void saveTest() {
        EnterpriseUser enterpriseUser = new EnterpriseUser();
//        enterpriseUser.setEnterpriseId(1L);
        enterpriseUser.setUserName("李四");
        enterpriseUser.setUserPid("11111120000101000X");
        enterpriseUser.setUserType(Card.UserType.ENTERPRISE);
        enterpriseUser.setRight(EnterpriseUser.Right.SUPER);

        Enterprise enterprise = new Enterprise();
//        enterprise.setEnterpriseId(2L);
        enterprise.setEnterpriseName("华为");

        enterpriseUser.setEnterprise(enterprise);
//        userRepository.save(enterpriseUser);
        Iterable<User> all = userRepository.findAll();
        all.forEach(System.out::println);
        assertNotNull(all);
    }
}