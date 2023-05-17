package com.example.banksys.presentationlayer.controller;

import com.example.banksys.presentationlayer.utils.Role;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@SpringBootTest
@Transactional
//@SpringJUnitWebConfig
//@SpringJUnitConfig
//@WebMvcTest(controllers = DepositPersonalController.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonalUserControllerTest {

    private final MockMvc mockMvc;
    PersonalUserControllerTest(WebApplicationContext wac) {
        this.mockMvc = webAppContextSetup(wac).
                apply(springSecurity()) // 很重要这一行
                .apply(sharedHttpSession()) // 很重要这一行
                .build();
    }

    @Test
    void deposit() throws Exception {
        mockMvc.perform(get("/users/personal/deposit"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "53", password = "1", roles = "ORDINARY")
    void depositWithMockUser() {
        try {
            mockMvc.perform(get("/users/personal/deposit"))
                    .andExpect(status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "53",password = "1",authorities = Role.ORDINARY_USER)
    void getDepositPage() throws Exception {
        mockMvc.perform(get("/users/personal/deposit"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void withMockAuthenticationTest() throws Exception {

        mockMvc.perform(formLogin("/login").user("53").password("1"))
                .andExpect(authenticated())
                .andDo(print())
                .andDo(result -> {
                    this.mockMvc.perform(get("/users/personal/deposit/"))
                            .andDo(print())
                            .andExpect(redirectedUrl("current"));
                });
    }

    @Test
    void withMockAuthenticationSharedHttpSessionTest() throws Exception {

        mockMvc.perform(formLogin("/login").user("53").password("1"))
                .andExpect(authenticated())
                .andDo(print());

        mockMvc.perform(get("/users/personal/deposit"))
                .andDo(print())
//                .andExpect(status().isOk())
                .andExpect(redirectedUrl("current"));
    }
}