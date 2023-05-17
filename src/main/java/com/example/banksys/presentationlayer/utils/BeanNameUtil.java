package com.example.banksys.presentationlayer.utils;

import com.example.banksys.presentationlayer.form.OpenForm;
import org.springframework.util.StringUtils;

public class BeanNameUtil {

    public static String getBeanName(OpenForm form) {
        String userAccount = "UserAccount";
        StringBuilder beanName = new StringBuilder();
        beanName.append(form.getUserType());
        beanName.append(StringUtils.capitalize(form.getCardType()));
        beanName.append(userAccount);
        return beanName.toString();
    }

    public static String getBeanName(String userType, String cardType) {

        StringBuilder beanName = new StringBuilder();
        beanName.append(userType);
        beanName.append(StringUtils.capitalize(cardType));

        String userAccount = "UserAccount";

        beanName.append(userAccount);

        return beanName.toString();
    }
}
