package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.form.ChangePasswordForm;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

/**
 * 改密码控制器基类
 */
public abstract class ChangePasswordController {

    public static final String ACCOUNT_ATTRIBUTE = "account";
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ApplicationContext context;
    private UserService service;

    public ChangePasswordController(PasswordEncoder passwordEncoder, UserRepository userRepository, ApplicationContext context, UserService personalService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.context = context;
        this.service = personalService;
    }

    public ChangePasswordForm createChangePasswordForm(Authentication authentication) {
        return new ChangePasswordForm(Long.parseLong(authentication.getName()));
    }

    public String getPage(Model model, Authentication authentication) {
        return GetPageHelper.getPage(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE, "change_password");
    }

    public String changePasswordPost(Model model,  ChangePasswordForm form, Errors errors) {
        ToFrontendHelper.addPostUrl(model, "");
        if (errors.hasErrors()) {
            return "change_password";
        }
        BaseAccount account = (BaseAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);

        service.changePassword(account,passwordEncoder, form);

        ToFrontendHelper.addSuccessMessage(model,"修改密码成功！");
        return "success";
    }
}
