package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.exception.VipOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.service.PersonalService;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.utils.OpenForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("openForm")
@RequestMapping("/users/personal/open")
public class OpenController {

//    private CardRepository cardRepository;
    private PasswordEncoder passwordEncoder;
    private PersonalCardRepository personalCardRepository;
    private UserRepository userRepository;
    private ApplicationContext context;
    private PersonalService personalService;

    @Autowired
    public void context(ApplicationContext context) { this.context = context; }

    @Autowired
    public void personalService(PersonalService personalService) {
        this.personalService = personalService;
    }

    public OpenController(PersonalCardRepository personalCardRepository,UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.personalCardRepository = personalCardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("openForm")
    public OpenForm createOpenForm() {
        return new OpenForm();
    }

    @GetMapping
    public String openForm(Model model) {
//        model.addAttribute("openForm", new OpenForm());
        return "open";
    }

    @PostMapping
    public String processOpen(
            @Valid
                    OpenForm form, Errors errors, Model model,HttpSession session,SessionStatus sessionStatus)  {
        if (errors.hasErrors()) {
            return "open";
        }
        String beanName = getBeanName(form);
        PersonalUserAccount personalUserAccount = (PersonalUserAccount) context.getBean(beanName);
        Long cardId;
        try {
            cardId = personalService.openAccount(personalUserAccount, passwordEncoder, userRepository, form);
        } catch (VipOpenMoneyNotEnoughException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errors";
        }
//        clearOpenForm(session,sessionStatus);
        model.addAttribute("cardId",cardId);
        return "open_success";
    }

    @ModelAttribute("openForm")
    public void clearOpenForm(HttpSession session,SessionStatus sessionStatus) {
        session.removeAttribute("openForm");
//        sessionStatus.setComplete();
    }

    private String getBeanName(OpenForm form) {
        String userAccount = "UserAccount";
        StringBuilder beanName = new StringBuilder();
        beanName.append(form.getUserType());
        beanName.append(StringUtils.capitalize(form.getCardType()));
        beanName.append(userAccount);
        return beanName.toString();
    }
}
