package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.form.TransferForm;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.PostPageHelper;
import com.example.banksys.presentationlayer.utils.validator.OrderedChecks;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.banksys.presentationlayer.controller.TransferPersonalController.ACCOUNT_ATTRIBUTE;

@Controller
@RequestMapping("/users/personal/transfer/")
@SessionAttributes({ACCOUNT_ATTRIBUTE,"transferForm"})
public class TransferPersonalController {
    public static final String ACCOUNT_ATTRIBUTE = "account";
    private ApplicationContext context;
    private UserRepository userRepository;
    private UserService service;

    public TransferPersonalController(ApplicationContext context, UserRepository userRepository, UserService personalService) {
        this.context = context;
        this.userRepository = userRepository;
        this.service = personalService;
    }

    @ModelAttribute("transferForm")
    public TransferForm createTransferForm(Authentication authentication) {
        return new TransferForm(authentication.getName());
    }

    @GetMapping("/")
    public String getTransferPage(Model model, Authentication authentication) {
        return GetPageHelper.getPage(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE, "transfer");
    }

    @PostMapping("/")
    @Transactional
    public String transferPost(Model model, @Validated(OrderedChecks.class) TransferForm transferForm, Errors errors) {
        return PostPageHelper.transferPost(model, errors, ACCOUNT_ATTRIBUTE, service, transferForm);
    }
}
