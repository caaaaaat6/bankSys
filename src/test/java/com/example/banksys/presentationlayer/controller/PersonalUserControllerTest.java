package com.example.banksys.presentationlayer.controller;

import com.example.banksys.presentationlayer.utils.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


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
            mockMvc.perform(get("/users/personal/deposit/"))
                    .andExpect(status().is(302))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
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

        mockMvc.perform(get("/users/personal/deposit/"))
                .andDo(print())
//                .andExpect(status().isOk())
                .andExpect(redirectedUrl("current"));
    }

}