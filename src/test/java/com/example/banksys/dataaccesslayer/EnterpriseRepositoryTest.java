package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EnterpriseRepositoryTest {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void oneToManyTest() {
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseName("华为");

        EnterpriseUser enterpriseUser = new EnterpriseUser();
//        enterpriseUser.setUserId(1L);
//        enterpriseUser.setEnterpriseId(1L);
        enterpriseUser.setUserName("李四");
        enterpriseUser.setUserPid("11111120000101000X");
        enterpriseUser.setUserType(Card.UserType.ENTERPRISE);
        enterpriseUser.setRoot(EnterpriseUser.Root.SUPER);

        // 试一下双方都设置好引用
        // 结果可以
        enterprise.setEnterpriseUserList(Arrays.asList(enterpriseUser));
        enterpriseUser.setEnterprise(enterprise);

//        enterpriseRepository.save(enterprise);
//        userRepository.save(enterpriseUser);

        List<EnterpriseUser> enterpriseUserList = enterprise.getEnterpriseUserList();
        enterpriseUserList.get(0);
        System.out.println(enterprise.getEnterpriseUserList().size());
        enterpriseUserList.forEach(System.out::println);
        assertNotNull(enterpriseUserList);
    }
}