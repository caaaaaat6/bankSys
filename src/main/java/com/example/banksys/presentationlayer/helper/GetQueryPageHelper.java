package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

public class GetQueryPageHelper {


    public static String getQueryPage(Model model, Authentication authentication, ApplicationContext context, UserRepository userRepository, UserService service){
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);

        List<AccountLog> queryLogs = service.queryQueryLogs(account);
        String balance = service.queryBalance(account);
        List<Trade> tradeLogs = service.queryTrades(account);

        model.addAttribute("balance", balance);
        model.addAttribute("tradeLogs", tradeLogs);
        model.addAttribute("queryLogs", queryLogs);

        return "query_user_ver";
    }
}
