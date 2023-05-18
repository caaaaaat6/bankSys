package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.Service;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.form.WithdrawForm;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static com.example.banksys.presentationlayer.controller.WithdrawPersonalController.ACCOUNT_ATTRIBUTE;

@Controller
@RequestMapping("/users/personal/withdraw/")
@SessionAttributes({ACCOUNT_ATTRIBUTE})
public class WithdrawPersonalController {
    public static final String ACCOUNT_ATTRIBUTE = "account";
    private ApplicationContext context;
    private UserRepository userRepository;
    private Service service;

    public WithdrawPersonalController(ApplicationContext context, UserRepository userRepository, Service personalService) {
        this.context = context;
        this.userRepository = userRepository;
        this.service = personalService;
    }

    @ModelAttribute("withdrawForm")
    public WithdrawForm createWithdrawForm() {
        return new WithdrawForm();
    }

    @GetMapping("/")
    public String getWithdrawPage(Model model, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);
        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
        model.addAttribute("postTo","");
        return "withdraw";
    }

    @PostMapping("/")
    @Transactional
    public String withdrawPost(Model model, @Valid WithdrawForm withdrawForm, Errors errors) {
        if (errors.hasErrors()) {
            return "withdraw";
        }
        double balance = 0;
        BaseAccount account = (BaseAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        try {
            balance = service.withdraw(account, withdrawForm);
        } catch (Exception | WithdrawException e) {
//            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "errors";
        }
        model.addAttribute("successMessage", "账户余额为：" + balance);
        return "success";
    }

}
