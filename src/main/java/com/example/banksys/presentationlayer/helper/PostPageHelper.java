package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.service.Service;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.presentationlayer.form.TransferForm;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

public class PostPageHelper {

    public static String transferPost(Model model, Errors errors, final String ACCOUNT_ATTRIBUTE, Service service, TransferForm transferForm) {
        ToFrontendHelper.addPostUrl(model, "");
        if (errors.hasErrors()) {
            return "transfer";
        }
        double balance = 0;
        BaseAccount account = (BaseAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        try {
            balance = service.transfer(account, transferForm);
        } catch (Exception | UntransferableException | WithdrawException e) {
            ToFrontendHelper.addErrorMessage(model, e.getMessage());
            return "errors";
        }
        ToFrontendHelper.addSuccessMessage(model,"账户余额为：" + balance);
        return "success";
    }
}
