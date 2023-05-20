package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.helper.GetQueryPageHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/enterprise/query/")
public class QueryEnterpriseController {
    private ApplicationContext context;
    private UserRepository userRepository;
    private UserService service;

    public QueryEnterpriseController(ApplicationContext context, UserRepository userRepository, UserService enterpriseService) {
        this.context = context;
        this.userRepository = userRepository;
        this.service = enterpriseService;
    }

    @Transactional
    @GetMapping("/")
    public String getQueryPage(Model model, Authentication authentication) {
        return GetQueryPageHelper.getQueryPage(model,authentication,context,userRepository,service);
    }
}
