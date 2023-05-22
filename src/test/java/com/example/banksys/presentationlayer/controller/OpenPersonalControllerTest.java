package com.example.banksys.presentationlayer.controller;

import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.PersonalCard;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OpenPersonalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired//如果该对象需要mock，则加上此Annotate，在这里我们就是要模拟testService的query()行为
//    @Mock
    private PersonalCardRepository personalCardRepository;

    @Autowired
    UserRepository userRepository;

    @InjectMocks//使mock对象的使用类可以注入mock对象,在这里myController使用了testService（mock对象）,所以在MyController此加上此Annotate
    OpenPersonalController myController;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(myController).build();//这个对象是Controller单元测试的关键
    }

    @Test
    void openPage() throws Exception {
        mockMvc.perform(get("/users/personal/open"))
                .andDo(print());
    }

    @Test
    public void testQueryDataFromController() throws Exception{
        //静态引入使得很像人类的语言，当...然后返回...
//        when(personalCardRepository.query("code-1001","name-wangxindong")).thenReturn("成功");//这里模拟行为，返回"成功" 而不是原始的"test-code-name"

        mockMvc.perform(post("/users/personal/open")
                        .param("userName","张三丰")
                        .param("userPid","111111200001020001X")
                        .param("userType", Card.UserType.ORDINARY)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","0")
                        .param("password", "123456")
                        .param("confirm", "11"))

                .andDo(print());
//               .andExpect(status().isOk()).andExpect(content().string(is("{\"status\":\""+ result + "\"}")));

//        verify(testService).query("code-1001","name-wangxindong");

    }

    @Test
    public void openPersonalAccountSuccessTest() throws Exception{
        //静态引入使得很像人类的语言，当...然后返回...
        //这里模拟行为，返回"成功" 而不是原始的"test-code-name"

        mockMvc.perform(post("/users/personal/open")
                        .param("userName","张三丰")
                        .param("userPid","11111120000102001X")
                        .param("userType", Card.UserType.ORDINARY)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","0")
                        .param("password", "123456")
                        .param("confirm", "123456"))

//                .andExpect(status().isOk()).andExpect(content().string(is("{\"status\":\""+ result + "\"}"))
                .andDo(print());

//        verify(personalCardRepository).findPersonalCardsByUserPid("111111200001020001X");
        List<PersonalCard> personalCards = personalCardRepository.findPersonalCardsByUserPid("11111120000102001X").get();
        assert personalCards.size() > 0;
    }

    @Test
    void openVipAccountTest() throws Exception {
        String userPid = "11111120000102001X";
        mockMvc.perform(post("/users/personal/open")
                        .param("userName","张三丰")
                        .param("userPid",userPid)
                        .param("userType", Card.UserType.VIP)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","1000000")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print());

        Optional<List<PersonalCard>> personalCards = personalCardRepository.findPersonalCardsByUserPid(userPid);
        boolean areVip = true;
        for (PersonalCard personalCard : personalCards.get()) {
            areVip &= personalCard.getUserType().equals(Card.UserType.VIP);
        }
        assert areVip;
    }

    @Test
    void openVipAccountChangeUserTypeInUserTableTest() throws Exception {
        String userPid = "11111120000102001X";
        mockMvc.perform(post("/users/personal/open")
                        .param("userName","张三丰")
                        .param("userPid",userPid)
                        .param("userType", Card.UserType.VIP)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney","1000000")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(content().string(contains("账户ID为")));
//                .andExpect(content().xml(contains("账户ID为")));

        Optional<List<PersonalCard>> personalCards = personalCardRepository.findPersonalCardsByUserPid(userPid);
        boolean areVip = true;
        for (PersonalCard personalCard : personalCards.get()) {
            areVip &= personalCard.getUserType().equals(Card.UserType.VIP);
            areVip &= userRepository.findByCard(personalCard).getUserType().equals(Card.UserType.VIP);
        }
        assert areVip;
    }

    @Test
    void openVipAccountGetResponseTest() throws Exception {
        String userPid = "11111120000102001X";
        MvcResult mvcResult = mockMvc.perform(post("/users/personal/open")
                        .param("userName", "张三丰")
                        .param("userPid", userPid)
                        .param("userType", Card.UserType.VIP)
                        .param("cardType", Card.CardType.CURRENT)
                        .param("openMoney", "1000000")
                        .param("password", "1")
                        .param("confirm", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
//                .andExpect(content().string(contains("账户ID为")));
        assert  mvcResult.getResponse().getContentAsString().contains("账户ID为");

        Optional<List<PersonalCard>> personalCards = personalCardRepository.findPersonalCardsByUserPid(userPid);
        boolean areVip = true;
        for (PersonalCard personalCard : personalCards.get()) {
            areVip &= personalCard.getUserType().equals(Card.UserType.VIP);
            areVip &= userRepository.findByCard(personalCard).getUserType().equals(Card.UserType.VIP);
        }
        assert areVip;
    }
}