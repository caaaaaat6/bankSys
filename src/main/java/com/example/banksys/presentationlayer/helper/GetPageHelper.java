package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public class GetPageHelper {

    /**
     * GetMapping帮助类，添加表单的post URL，负责添加登录用户的信息Attribute
     * @param model
     * @param authentication
     * @param userRepository
     * @param context
     * @param ACCOUNT_ATTRIBUTE
     * @param template
     * @return template模板名称
     */
    public static String getPage(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, final String ACCOUNT_ATTRIBUTE, String template) {
        addAccount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        model.addAttribute("postTo", "");
        return template;
    }

    /**
     * 责添加登录用户的信息Attribute
     * @param model
     * @param authentication
     * @param userRepository
     * @param context
     * @param ACCOUNT_ATTRIBUTE
     */
    public static void addAccount(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, String ACCOUNT_ATTRIBUTE) {
        BaseAccount account = getAccountAndSetEmployee(model, authentication, context, userRepository);
        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
    }

    /**
     * 添加雇员Attribute
     * @param model
     * @param authentication
     * @param employeeRepository
     */
    public static void addEmployee(Model model, Authentication authentication, EmployeeRepository employeeRepository) {
        Long userId = Long.parseLong(authentication.getName());
        Employee employee = employeeRepository.findById(userId).get();
        model.addAttribute("employee", employee);
    }

    /**
     * 从authentication得到用户账户，并给用户账户设置雇员
     * @param model
     * @param authentication
     * @param context
     * @param userRepository
     * @return
     */
    public static BaseAccount getAccountAndSetEmployee(Model model, Authentication authentication, ApplicationContext context, UserRepository userRepository) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);

        // 从model里获取操作客户账户的雇员，如果雇员不存在（为null）就设置为null，否则就是雇员
        Employee employee = (Employee) model.getAttribute("employee");
        account.setEmployee(employee);
        return account;
    }

    /**
     * 添加雇员账户Attribute
     * @param model
     * @param authentication
     * @param context
     * @param employeeRepository
     * @return
     */
    public static BaseEmployeeAccount addEmployeeAccountAttribute(Model model, Authentication authentication, ApplicationContext context, EmployeeRepository employeeRepository) {
        Employee employee = (Employee) model.getAttribute("employee");
        BaseEmployeeAccount account = (BaseEmployeeAccount) context.getBean(BeanNameUtil.getBeanName(employee.getEmployeeType()));
        account.setEmployee(employee);
        model.addAttribute("employeeAccount", account);
        return account;
    }
}
