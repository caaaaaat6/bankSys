package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public class GetPageHelper {

    public static String getPage(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, final String ACCOUNT_ATTRIBUTE, String template) {
        addAcount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        model.addAttribute("postTo","");
        return template;
    }

    public static void addAcount(Model model, Authentication authentication, UserRepository userRepository, ApplicationContext context, String ACCOUNT_ATTRIBUTE) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();
        BaseAccount account = (BaseAccount) context.getBean(BeanNameUtil.getBeanName(user.getUserType(), user.getCard().getCardType()));
        account.setUser(user);
        model.addAttribute(ACCOUNT_ATTRIBUTE, account);
    }
}
