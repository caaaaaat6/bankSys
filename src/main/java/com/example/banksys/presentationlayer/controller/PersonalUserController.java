package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/users/personal/")
public class PersonalUserController {
    public static final String PERSONAL_ACCOUNT_ATTRIBUTE = "personalAccount";
    private ApplicationContext context;
    private UserRepository userRepository;


    @GetMapping("/deposit")
    public String getDepositPage(Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
//        User user = userRepository.findById(userId).get();
//        BaseCurrentAccountRight currentAccount;
//        BaseFixedAccountRight fixedAccount;
//        switch (user.getCard().getCardType()) {
//            case Card.CardType.CURRENT:
//                PersonalUserAccount personalUserAccount = (PersonalUserAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), Card.CardType.CURRENT));
//                personalUserAccount.setUser(user);
//                currentAccount = (BaseCurrentAccountRight) personalUserAccount;
//                break;
//            case Card.CardType.FIXED:
//
//                break;
//            default:
//        }

        log.info("----------------" + userId);

        return "deposit";
    }
}
