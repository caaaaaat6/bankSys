package com.example.banksys.presentationlayer.helper;

import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.presentationlayer.form.TransferForm;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

public class PostPageHelper {

    /**
     * 转账post帮助方法，如果有错误就展示错误，否则转向成功页面
     * @param model
     * @param errors
     * @param ACCOUNT_ATTRIBUTE
     * @param service
     * @param transferForm
     * @return 名称为"success"的template页面
     */
    public static String transferPost(Model model, Errors errors, final String ACCOUNT_ATTRIBUTE, UserService service, TransferForm transferForm) {
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
