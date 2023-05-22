package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Trade;
import com.example.banksys.model.log.AccountLog;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

public class GetQueryPageHelper {

    /**
     * GetMapping帮助方法，得到查询的页面，并向model添加所查询的信息
     * @param model
     * @param authentication
     * @param context
     * @param userRepository
     * @param service
     * @return
     */
    public static String getQueryPage(Model model, Authentication authentication, ApplicationContext context, UserRepository userRepository, UserService service){
        BaseAccount account = GetPageHelper.getAccountAndSetEmployee(model, authentication, context, userRepository);

        List<AccountLog> queryLogs = service.queryQueryLogs(account);
        String balance = service.queryBalance(account);
        List<Trade> tradeLogs = service.queryTrades(account);

        model.addAttribute("balance", balance);
        model.addAttribute("tradeLogs", tradeLogs);
        model.addAttribute("queryLogs", queryLogs);

        return "query_user_ver";
    }

}
