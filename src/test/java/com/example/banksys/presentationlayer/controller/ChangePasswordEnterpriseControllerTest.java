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
class ChangePasswordEnterpriseControllerTest {
    private final MockMvc mockMvc;
    ChangePasswordEnterpriseControllerTest(WebApplicationContext wac) {
        this.mockMvc = webAppContextSetup(wac)
                .apply(springSecurity()) // 很重要这一行
                .apply(sharedHttpSession()) // 很重要这一行
                .build();
    }

    @Test
    void changePasswordPost() throws Exception {
        mockMvc.perform(formLogin("/login").user("76").password("1"))
                .andExpect(authenticated())
                .andDo(print());

        mockMvc.perform(get("/users/enterprise/change_password/"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/enterprise/change_password/")
                        .param("userPid", "11111120000102003X")
                        .param("password", "2")
                        .param("confirm", "2"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(formLogin("/login").user("76").password("2"))
                .andDo(print())
                .andExpect(authenticated());
    }
}