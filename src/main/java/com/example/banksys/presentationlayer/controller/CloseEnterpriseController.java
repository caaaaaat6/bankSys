package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.Service;
import com.example.banksys.dataaccesslayer.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import static com.example.banksys.presentationlayer.controller.TransferPersonalController.ACCOUNT_ATTRIBUTE;

@Controller
@RequestMapping("/users/enterprise/close/")
@SessionAttributes({ACCOUNT_ATTRIBUTE})
public class CloseEnterpriseController extends CloseController {
    public CloseEnterpriseController(ApplicationContext context, UserRepository userRepository, Service enterpriseService) {
        setContext(context);
        setUserRepository(userRepository);
        setService(enterpriseService);
    }

    @GetMapping("/")
    @Override
    public String getClosePage(Model model, Authentication authentication) {
        return super.getClosePage(model, authentication);
    }

    @GetMapping("/y")
    @Transactional
    @Override
    public String closeSuccessMessageDisplay(Model model) {
        return super.closeSuccessMessageDisplay(model);
    }

    @GetMapping("/n")
    @Transactional
    @Override
    public String closeFailMessageDisplay(Model model) {
        return super.closeFailMessageDisplay(model);
    }
}
