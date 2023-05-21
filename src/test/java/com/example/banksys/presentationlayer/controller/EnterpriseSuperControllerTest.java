package com.example.banksys.presentationlayer.controller;

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
class EnterpriseSuperControllerTest {
    private final MockMvc mockMvc;
    EnterpriseSuperControllerTest(WebApplicationContext wac) {
        this.mockMvc = webAppContextSetup(wac)
                .apply(springSecurity()) // 很重要这一行
                .apply(sharedHttpSession()) // 很重要这一行
                .build();
    }
    @Test
    void getManagePage() throws Exception {
        mockMvc.perform(formLogin("/login").user("73").password("1"))
                .andExpect(authenticated())
                .andDo(print());

        mockMvc.perform(get("/users/enterprise/"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/enterprise/manage"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void postAddPage() throws Exception {
        mockMvc.perform(formLogin("/login").user("73").password("1"))
                .andExpect(authenticated())
                .andDo(print());

        mockMvc.perform(get("/users/enterprise/"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/enterprise/manage/add")
                        .param("userName", "测试日志切面")
                        .param("userPid", "11111120000102012X")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}