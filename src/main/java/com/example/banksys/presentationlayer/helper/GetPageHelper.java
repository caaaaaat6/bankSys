package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.ui.Model;

import java.util.Collection;

public class GetPageHelper {

    public static String getPage(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, final String ACCOUNT_ATTRIBUTE, String template) {
        addAccount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        model.addAttribute("postTo","");
        return template;
    }

    public static void addAccount(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, String ACCOUNT_ATTRIBUTE) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);
        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
    }

    public static void addEmployee(Model model, Authentication authentication, EmployeeRepository employeeRepository) {
        Long userId = Long.parseLong(authentication.getName());
        Employee employee = employeeRepository.findById(userId).get();
        model.addAttribute("employee", employee);
    }
}
