package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.EnterpriseService;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.presentationlayer.form.AddEnterpriseUserForm;
import com.example.banksys.presentationlayer.form.SelectedUserIdForm;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.banksys.presentationlayer.controller.EnterpriseSuperController.ACCOUNT_ATTRIBUTE;

@Controller
@RequestMapping("/users/enterprise/manage")
@SessionAttributes({ACCOUNT_ATTRIBUTE})
public class EnterpriseSuperController {

    public static final String ACCOUNT_ATTRIBUTE = "account";
    private PasswordEncoder passwordEncoder;
    private EnterpriseService enterpriseService;
    private UserRepository userRepository;
    private EnterpriseUserRepository enterpriseUserRepository;
    private ApplicationContext context;

    public EnterpriseSuperController(EnterpriseService enterpriseService, UserRepository userRepository, ApplicationContext context, EnterpriseUserRepository enterpriseUserRepository, PasswordEncoder passwordEncoder) {
        this.enterpriseService = enterpriseService;
        this.userRepository = userRepository;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.context = context;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute
    public SelectedUserIdForm createSelectedUserIdForm() {
        return new SelectedUserIdForm();
    }

    @ModelAttribute
    public AddEnterpriseUserForm createAddEnterpriseForm() {
        return new AddEnterpriseUserForm();
    }

    @GetMapping
    public String getManagePage(Model model, Authentication authentication) {
        EnterpriseUserAccount account = (EnterpriseUserAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        if (account == null) {
            GetPageHelper.addAccount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
            account = (EnterpriseUserAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        }
        List<EnterpriseUser> enterpriseUsers = enterpriseService.getEnterpriseUsers(account);
        model.addAttribute("enterpriseUsers", enterpriseUsers);
        return "enterprise_super_manage";
    }

    @PostMapping
    public String postManagePage(Model model, SelectedUserIdForm form) {
        enterpriseService.deleteEnterpriseUser(userRepository, form);
        ToFrontendHelper.addSuccessMessage(model, "删除成功！");
        return "success";
    }

    @GetMapping("/add")
    public String getAddPage(Model model, Authentication authentication) {
        EnterpriseUserAccount account = (EnterpriseUserAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        if (account == null) {
            GetPageHelper.addAccount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        }
        return "enterprise_super_add";
    }

    @PostMapping("/add")
    public String postAddPage(Model model, @Validated AddEnterpriseUserForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "enterprise_super_add";
        }
        EnterpriseUserAccount account = (EnterpriseUserAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        Long userId = enterpriseService.addEnterpriseUser(account,enterpriseUserRepository, form,passwordEncoder);
        ToFrontendHelper.addSuccessMessage(model, "新建企业账户ID为" + userId + "\n请牢记！");
        return "success";
    }
}
