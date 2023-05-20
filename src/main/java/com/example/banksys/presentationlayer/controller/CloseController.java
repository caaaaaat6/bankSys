package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Data
public abstract class CloseController {

    public static final String ACCOUNT_ATTRIBUTE = "account";
    private ApplicationContext context;
    private UserRepository userRepository;
    private UserService service;

    public String getClosePage(Model model, Authentication authentication) {
        GetPageHelper.addAcount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        model.addAttribute("yesUrl","y");
        model.addAttribute("noUrl","n");
        return "close";
    }

    public String closeSuccessMessageDisplay(Model model) {
        BaseAccount account = (BaseAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        double balance = service.close(account);
        ToFrontendHelper.addSuccessMessage(model, "销户成功！取出全部" + balance + "元余额！");
        return "success";
    }

    public String closeFailMessageDisplay(Model model) {
        ToFrontendHelper.addSuccessMessage(model, "您已取消销户操作！");
        return "cancel";
    }
}
