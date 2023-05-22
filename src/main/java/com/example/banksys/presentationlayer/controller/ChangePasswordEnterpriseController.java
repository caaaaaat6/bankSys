package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.form.ChangePasswordForm;
import com.example.banksys.presentationlayer.utils.validator.OrderedChecks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.banksys.presentationlayer.controller.ChangePasswordController.ACCOUNT_ATTRIBUTE;

@Controller
@SessionAttributes({"changePasswordForm", ACCOUNT_ATTRIBUTE})
@RequestMapping("/users/enterprise/change_password/")
public class ChangePasswordEnterpriseController extends ChangePasswordController{
    public ChangePasswordEnterpriseController(PasswordEncoder passwordEncoder, UserRepository userRepository, ApplicationContext context, UserService personalService) {
        super(passwordEncoder, userRepository, context, personalService);
    }

    @ModelAttribute("changePasswordForm")
    @Override
    public ChangePasswordForm createChangePasswordForm(Authentication authentication) {
        return super.createChangePasswordForm(authentication);
    }

    @GetMapping
    @Override
    public String getPage(Model model, Authentication authentication) {
        return super.getPage(model, authentication);
    }

    @Transactional
    @PostMapping
    @Override
    public String changePasswordPost(Model model, @Validated(OrderedChecks.class) ChangePasswordForm form, Errors errors) {
        return super.changePasswordPost(model, form, errors);
    }
}
