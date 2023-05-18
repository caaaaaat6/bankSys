package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.Service;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.helper.GetQueryPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/personal/query/")
public class QueryPersonalController {
    private ApplicationContext context;
    private UserRepository userRepository;
    private Service service;

    public QueryPersonalController(ApplicationContext context, UserRepository userRepository, Service personalService) {
        this.context = context;
        this.userRepository = userRepository;
        this.service = personalService;
    }

    @GetMapping("/")
    public String getQueryPage(Model model, Authentication authentication) {
        return GetQueryPageHelper.getQueryPage(model, authentication, context, userRepository, service);
    }

}
