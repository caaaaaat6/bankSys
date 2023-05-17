package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.presentationlayer.controller.DepositEnterpriseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

@Slf4j
public class RedirectDepositHelper {
    public static String getRedirectString(Model model, Long userId, BaseAccount userAccount) {
        BaseCurrentAccountRight currentAccount;
        BaseFixedAccountRight fixedAccount;
        if (userAccount instanceof BaseCurrentAccountRight) {
            currentAccount = (BaseCurrentAccountRight) userAccount;
            model.addAttribute(DepositEnterpriseController.CURRENT_ACCOUNT_ATTRIBUTE, currentAccount);
            return "redirect:current";
        } else if (userAccount instanceof BaseFixedAccountRight) {
            fixedAccount = (BaseFixedAccountRight) userAccount;
            model.addAttribute(DepositEnterpriseController.FIXED_ACCOUNT_ATTRIBUTE, fixedAccount);
            return "redirect:fixed";
        }
        return "errors";
    }
}
