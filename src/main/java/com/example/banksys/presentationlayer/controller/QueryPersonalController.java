package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.Service;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import com.example.banksys.model.log.AccountLog;
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
    public static final String ACCOUNT_ATTRIBUTE = "account";
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
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);

        String balance = service.queryBalance(account);
        List<Trade> tradeLogs = service.queryTrades(account);
        List<AccountLog> queryLogs = service.queryQueryLogs(account);

        model.addAttribute("balance", balance);
        model.addAttribute("tradeLogs", tradeLogs);
        model.addAttribute("queryLogs", queryLogs);
//        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
//        model.addAttribute("postTo","");

        return "query_user_ver";
    }
}
