package com.example.banksys.presentationlayer.controller;

import com.example.banksys.model.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@SpringBootTest
@Transactional
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeControllerTest {
    private final MockMvc mockMvc;

    EmployeeControllerTest(WebApplicationContext wac) {
        this.mockMvc = webAppContextSetup(wac).
                apply(springSecurity()) // 很重要这一行
                .apply(sharedHttpSession()) // 很重要这一行
                .build();
    }

    @Test
    void openPersonalAccountWithEmployee() throws Exception {
        mockMvc.perform(formLogin("/login").user("84").password("1"))
                .andExpect(authenticated())
//                .andDo(print())
        ;

        mockMvc.perform(get("/employee/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/personal/open"));

        mockMvc.perform(post("/users/personal/open")
                        .param("userName","测试雇员操作开户")
                        .param("userPid","11111120000102009X")
                        .param("userType", Card.UserType.VIP)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","1000000")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void openEnterpriseAccountWithEmployee() throws Exception {
        mockMvc.perform(formLogin("/login").user("84").password("1"))
                .andExpect(authenticated())
//                .andDo(print())
        ;

        mockMvc.perform(get("/employee/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/enterprise/open"));

        mockMvc.perform(post("/users/enterprise/open")
                        .param("userName","测试雇员操作开户")
                        .param("userPid","11111120000102009X")
                        .param("userType", Card.UserType.ENTERPRISE)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","1000000")
                        .param("enterpriseId", "21")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}