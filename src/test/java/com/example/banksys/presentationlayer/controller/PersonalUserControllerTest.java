package com.example.banksys.presentationlayer.controller;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonalUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks//使mock对象的使用类可以注入mock对象,在这里myController使用了testService（mock对象）,所以在MyController此加上此Annotate
    PersonalUserController myController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(myController).build();//这个对象是Controller单元测试的关键
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
}