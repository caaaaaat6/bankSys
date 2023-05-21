package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.EnterpriseService;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Enterprise;
import com.example.banksys.presentationlayer.form.AddEnterpriseForm;
import com.example.banksys.presentationlayer.form.EnterpriseOpenForm;
import com.example.banksys.presentationlayer.form.OpenForm;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import static com.example.banksys.presentationlayer.utils.BeanNameUtil.getBeanName;

@Controller
@SessionAttributes({"openForm", "employee"})
@RequestMapping("/users/enterprise/open")
public class OpenEnterpriseController {

    private PasswordEncoder passwordEncoder;
    private EnterpriseCardRepository enterpriseCardRepository;
    private EnterpriseUserRepository enterpriseUserRepository;
    private ApplicationContext context;
    private EnterpriseService enterpriseService;
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void enterpriseService(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    public OpenEnterpriseController(PasswordEncoder passwordEncoder, EnterpriseCardRepository enterpriseCardRepository, EnterpriseUserRepository enterpriseUserRepository, ApplicationContext context, EnterpriseService enterpriseService, EnterpriseRepository enterpriseRepository) {
        this.passwordEncoder = passwordEncoder;
        this.enterpriseCardRepository = enterpriseCardRepository;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.context = context;
        this.enterpriseService = enterpriseService;
        this.enterpriseRepository = enterpriseRepository;
    }

    @ModelAttribute("openForm")
    public OpenForm createOpenForm() {
        return new EnterpriseOpenForm();
    }

    @ModelAttribute
    public AddEnterpriseForm createAddEnterpriseForm() {
        return new AddEnterpriseForm();
    }

    @GetMapping
    public String openForm(Model model) {
        Iterable<Enterprise> all = enterpriseRepository.findAll();
        model.addAttribute("enterprises", all);
        return "open_enterprise";
    }

    @PostMapping
    public String processOpenEnterprise(
            @Valid
                    EnterpriseOpenForm form, Errors errors, Model model, HttpSession session, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "open_enterprise";
        }
        String beanName = getBeanName(form);
        EnterpriseUserAccount enterpriseUserAccount = (EnterpriseUserAccount) context.getBean(beanName);

        Employee employee = (Employee) model.getAttribute("employee");
        enterpriseUserAccount.setEmployee(employee);

        Long userId = null;
        try {
            userId = enterpriseService.openAccount(enterpriseUserAccount, passwordEncoder, form);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "errors";
        }
        clearOpenForm(session, sessionStatus);
        model.addAttribute("cardId" /* 实际上是user_id */, userId);
        return "open_success";
    }

    @ModelAttribute("openForm")
    public void clearOpenForm(HttpSession session, SessionStatus sessionStatus) {
        session.removeAttribute("openForm");
    }

    @GetMapping("/add_enterprise")
    public String getAddEnterprisePage() {
        return "enterprise_add_enterprise";
    }

    @PostMapping("/add_enterprise")
    public String postAddEnterprisePage(Model model, @Validated AddEnterpriseForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "enterprise_add_enterprise";
        }
        enterpriseService.addEnterprise(enterpriseRepository, form);
        ToFrontendHelper.addSuccessMessage(model, "添加企业成功！");
        return "success";
    }
}
