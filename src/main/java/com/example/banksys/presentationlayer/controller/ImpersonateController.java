package com.example.banksys.presentationlayer.controller;

import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee/impersonate")
public class ImpersonateController {

    @GetMapping
    public String getPage() {
        return "impersonate";
    }

    @GetMapping("/error")
    public String getErrorPage(Model model) {
        ToFrontendHelper.addErrorMessage(model, "客户账户ID或密码错误！");
        return "errors";
    }

    @GetMapping("/exit")
    public String getExitPage(Model model) {
        ToFrontendHelper.addSuccessMessage(model, "退出成功！");
        return "success";
    }
}
