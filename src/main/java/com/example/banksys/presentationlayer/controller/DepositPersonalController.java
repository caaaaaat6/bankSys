package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.PersonalService;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.form.DepositCurrentForm;
import com.example.banksys.presentationlayer.form.DepositFixedForm;
import com.example.banksys.presentationlayer.helper.RedirectDepositHelper;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import com.example.banksys.presentationlayer.utils.Role;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static com.example.banksys.presentationlayer.controller.DepositPersonalController.CURRENT_ACCOUNT_ATTRIBUTE;
import static com.example.banksys.presentationlayer.controller.DepositPersonalController.FIXED_ACCOUNT_ATTRIBUTE;

@Slf4j
@Controller
@RequestMapping("/users/personal/deposit/")
@SessionAttributes({CURRENT_ACCOUNT_ATTRIBUTE, FIXED_ACCOUNT_ATTRIBUTE})
public class DepositPersonalController {
    public static final String PERSONAL_ACCOUNT_ATTRIBUTE = "personalAccount";
    public static final String CURRENT_ACCOUNT_ATTRIBUTE = "currentAccount";
    public static final String FIXED_ACCOUNT_ATTRIBUTE = "fixedAccount";
    private ApplicationContext context;
    private UserRepository userRepository;
    private PersonalService personalService;

    public DepositPersonalController(ApplicationContext context, UserRepository userRepository, PersonalService personalService) {
        this.context = context;
        this.userRepository = userRepository;
        this.personalService = personalService;
    }

    @ModelAttribute("depositCurrentForm")
    public DepositCurrentForm createDepositCurrentForm() {
        return new DepositCurrentForm();
    }

    @ModelAttribute("depositFixedForm")
    public DepositFixedForm createDepositFixedForm() {
        return new DepositFixedForm();
    }

    @Transactional
    @GetMapping("/")
    public String getDepositPage(Model model, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        PersonalUserAccount personalUserAccount = (PersonalUserAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        personalUserAccount.setUser(user);
        return RedirectDepositHelper.getRedirectString(model, user.getUserId(), personalUserAccount);
    }

    @Secured(value = {Role.CURRENT_RIGHT})
    @GetMapping("/current")
    public String currentDeposit() {
        return "deposit_current";
    }

    @Secured(value = {Role.FIXED_RIGHT})
    @GetMapping("/fixed")
    public String fixedDeposit() {
        return "deposit_fixed";
    }

    @PostMapping("/current")
    @Transactional
    public String currentDepositPost(Model model, @Valid DepositCurrentForm depositCurrentForm, Errors errors) {
        if (errors.hasErrors()) {
            return "deposit_current";
        }
        BaseCurrentAccountRight accountRight = (BaseCurrentAccountRight) model.getAttribute(CURRENT_ACCOUNT_ATTRIBUTE);
        double balance = personalService.depositCurrent(accountRight, depositCurrentForm);
        model.addAttribute("successMessage", "账户余额为：" + balance);
        return "success";
    }

    @PostMapping("/fixed")
    @Transactional
    public String fixedDepositPost(Model model, @Valid DepositFixedForm depositFixedForm, Errors errors) {
        if (errors.hasErrors()) {
            return "deposit_fixed";
        }
        BaseFixedAccountRight accountRight = (BaseFixedAccountRight) model.getAttribute(FIXED_ACCOUNT_ATTRIBUTE);
        double balance = personalService.depositFixed(accountRight, depositFixedForm);
        model.addAttribute("successMessage", "账户余额为：" + balance);
        return "success";
    }
}
