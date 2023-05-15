package com.example.banksys.presentationlayer.controller;

import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@SpringJUnitWebConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenEnterpriseControllerTest {

    private final MockMvc mockMvc;
    OpenEnterpriseControllerTest(WebApplicationContext wac) {
        this.mockMvc = webAppContextSetup(wac).build();
    }


    @Test
    void processOpenEnterprise(@Autowired EnterpriseRepository enterpriseRepository) throws Exception {
        Enterprise enterprise = enterpriseRepository.findById(6L).get();
        mockMvc.perform(post("/users/enterprise/open")
                        .param("userName","杨过")
                        .param("userPid","11111120000102002X")
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","10000")
//                        .param("enterprise", "Enterprise(enterpriseId=6,+enterpriseName=华为)")
                        .param("enterpriseId", "6")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}