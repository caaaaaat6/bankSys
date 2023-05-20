package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public class GetWithdrawPageHelper {

    public static String getWithdrawPage(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context,final String ACCOUNT_ATTRIBUTE) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);

        Employee employee = (Employee) model.getAttribute("employee");
        account.setEmployee(employee);

        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
        model.addAttribute("postTo","");
        return "withdraw";
    }
}
