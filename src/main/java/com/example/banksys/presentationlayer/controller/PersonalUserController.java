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
import org.springframework.ui.Model;
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

    public PersonalUserController(ApplicationContext context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    @GetMapping("/deposit")
    public String getDepositPage(Model model, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        PersonalUserAccount personalUserAccount = (PersonalUserAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        personalUserAccount.setUser(user);
        BaseCurrentAccountRight currentAccount;
        BaseFixedAccountRight fixedAccount;
        if (personalUserAccount instanceof BaseCurrentAccountRight) {
            currentAccount = (BaseCurrentAccountRight) personalUserAccount;
            model.addAttribute("currentAccount", currentAccount);
            return "redirect:current-deposit";
//            return "deposit_current";
        } else if (personalUserAccount instanceof BaseFixedAccountRight) {
            fixedAccount = (BaseFixedAccountRight) personalUserAccount;
            model.addAttribute("fixedAccount", fixedAccount);
            return "redirect:fixed-deposit";
//            return "deposit_fixed";
        }

        log.info("----------------" + userId);

        return "errors";
    }

    @PostMapping("/deposit")
    public String deposit(Model model) {
        BaseCurrentAccountRight currentAccount = (BaseCurrentAccountRight) model.getAttribute("currentAccount");
        BaseFixedAccountRight fixedAccount = (BaseFixedAccountRight) model.getAttribute("fixedAccount");
        if (currentAccount != null) {

        }
        return "";
    }

    @GetMapping("/current-deposit")
    public String currentDeposit() {
        return "deposit_current";
    }

    @GetMapping("/fixed-deposit")
    public String fixedDeposit() {
        return "deposit_fixed";
    }
}
