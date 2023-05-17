package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.EnterpriseService;
import com.example.banksys.businesslogiclayer.useraccount.*;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.model.EnterpriseUser;
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

import static com.example.banksys.presentationlayer.controller.DepositEnterpriseController.CURRENT_ACCOUNT_ATTRIBUTE;
import static com.example.banksys.presentationlayer.controller.DepositEnterpriseController.FIXED_ACCOUNT_ATTRIBUTE;

@Slf4j
@Controller
@RequestMapping("/users/enterprise/deposit/")
@SessionAttributes({CURRENT_ACCOUNT_ATTRIBUTE,FIXED_ACCOUNT_ATTRIBUTE})
public class DepositEnterpriseController {
    public static final String CURRENT_ACCOUNT_ATTRIBUTE = "currentAccount";
    public static final String FIXED_ACCOUNT_ATTRIBUTE = "fixedAccount";
    private ApplicationContext context;
    private EnterpriseUserRepository enterpriseUserRepository;
    private EnterpriseService enterpriseService;

    public DepositEnterpriseController(ApplicationContext context, EnterpriseUserRepository enterpriseUserRepository, EnterpriseService enterpriseService) {
        this.context = context;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.enterpriseService = enterpriseService;
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
        EnterpriseUser user = enterpriseUserRepository.findById(userId).get();
        EnterpriseUserAccount enterpriseUserAccount = (EnterpriseUserAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        enterpriseUserAccount.setEnterpriseUser(user);
        return RedirectDepositHelper.getRedirectString(model, userId, enterpriseUserAccount);
    }

    @Secured(value = {Role.CURRENT_RIGHT})
    @GetMapping("/current")
    public String currentDeposit() {
        return "deposit_current_enterprise";
    }

    @Secured(value = {Role.FIXED_RIGHT})
    @GetMapping("/fixed")
    public String fixedDeposit() {
        return "deposit_fixed_enterprise";
    }

    @PostMapping("/current")
    public String currentDepositPost(Model model, @Valid DepositCurrentForm depositCurrentForm, Errors errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(System.out::println);
            return "deposit_current_enterprise";
        }
        BaseCurrentAccountRight accountRight = (BaseCurrentAccountRight) model.getAttribute(CURRENT_ACCOUNT_ATTRIBUTE);
        double balance = enterpriseService.depositCurrent(accountRight, depositCurrentForm);
        model.addAttribute("successMessage", "账户余额为：" + balance);
        return "success";
    }

    @PostMapping("/fixed")
    public String fixedDepositPost(Model model, @Valid DepositFixedForm depositFixedForm, Errors errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(System.out::println);
            return "deposit_fixed_enterprise";
        }
        BaseFixedAccountRight accountRight = (BaseFixedAccountRight) model.getAttribute(FIXED_ACCOUNT_ATTRIBUTE);
        double balance = enterpriseService.depositFixed(accountRight, depositFixedForm);
        model.addAttribute("successMessage", "账户余额为：" + balance);
        return "success";
    }
}
