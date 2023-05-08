package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EnterpriseRepository enterpriseRepository;

    // 测试UserRepository能不能读到EnterpriseUser?
    // 可以
    @Test
    void saveTest() {
        // user初始化
        EnterpriseUser enterpriseUser = new EnterpriseUser();
//        enterpriseUser.setUserId(6L);
//        enterpriseUser.setEnterpriseId(7L);
        enterpriseUser.setUserName("王五");
        enterpriseUser.setUserPid("11111120000101000X");
        enterpriseUser.setUserType(Card.UserType.ENTERPRISE);
        enterpriseUser.setRightType(EnterpriseUser.RightType.SUPER);

        // 企业初始化
        Enterprise enterprise = new Enterprise();
//        enterprise.setEnterpriseId(7L);
        enterprise.setEnterpriseName("小米");

        //企业设置user
        enterprise.setEnterpriseUserList(Arrays.asList(enterpriseUser));
//        List<EnterpriseUser> userList =  enterprise.getEnterpriseUserList();
//        if (userList == null) {
//            userList = new ArrayList<>();
//        }
//        userList.add(enterpriseUser);
        // user设置企业
        enterpriseUser.setEnterprise(enterprise);

        userRepository.save(enterpriseUser);
        Iterable<User> all = userRepository.findAll();
        all.forEach(System.out::println);
        assertNotNull(all);
    }

    @Test
    void saveEnterprise() {
        // user初始化
        EnterpriseUser enterpriseUser = new EnterpriseUser();
//        enterpriseUser.setUserId(6L);
//        enterpriseUser.setEnterpriseId(7L);
        enterpriseUser.setUserName("王五");
        enterpriseUser.setUserPid("11111120000101000X");
        enterpriseUser.setUserType(Card.UserType.ENTERPRISE);
        enterpriseUser.setRightType(EnterpriseUser.RightType.SUPER);

        // 企业初始化
        Enterprise enterprise = new Enterprise();
//        enterprise.setEnterpriseId(7L);
        enterprise.setEnterpriseName("小米");

        // user设置企业
        enterpriseUser.setEnterprise(enterprise);
        //企业设置user
//        enterprise.setEnterpriseUserList(Arrays.asList(enterpriseUser));

//        userRepository.save(enterpriseUser);

        enterpriseRepository.save(enterprise);
        userRepository.save(enterpriseUser);


        Iterable<User> all = userRepository.findAll();
        all.forEach(System.out::println);
        assertNotNull(all);
    }
}