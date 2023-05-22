package com.example.banksys.presentationlayer.utils;

import com.example.banksys.presentationlayer.form.OpenForm;
import com.example.banksys.presentationlayer.form.RegisterForm;
import org.springframework.util.StringUtils;

public class BeanNameUtil {

    /**
     * 根据开户信息得到账户的beanName
     * @param form 用户账户开户表单
     * @return 账户的beanName
     */
    public static String getBeanName(OpenForm form) {
        String userAccount = "UserAccount";
        StringBuilder beanName = new StringBuilder();
        beanName.append(form.getUserType());
        beanName.append(StringUtils.capitalize(form.getCardType()));
        beanName.append(userAccount);
        return beanName.toString();
    }

    /**
     * 根据雇员注册信息得到雇员的beanName
     * @param form 雇员注册表单
     * @return 雇员的beanName
     */
    public static String getBeanName(RegisterForm form) {
        String employeeAccount = "EmployeeAccount";
        StringBuilder beanName = new StringBuilder();
        beanName.append(form.getEmployeeType());
        beanName.append(employeeAccount);
        return beanName.toString();
    }

    /**
     * 根据用户类型和银行卡类型得到用户账户的beanName
     * @param userType
     * @param cardType
     * @return 雇员的beanName
     */
    public static String getBeanName(String userType, String cardType) {

        StringBuilder beanName = new StringBuilder();
        beanName.append(userType);
        beanName.append(StringUtils.capitalize(cardType));

        String userAccount = "UserAccount";

        beanName.append(userAccount);

        return beanName.toString();
    }

    /**
     * 根据雇员类型得到雇员账户的beanName
     * @param employeeTypeEn
     * @return
     */
    public static String getBeanName(String employeeTypeEn) {
        String employeeAccount = "EmployeeAccount";
        return employeeTypeEn + employeeAccount;
    }
}
