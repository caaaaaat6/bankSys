package com.example.banksys.presentationlayer.controller;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.OpenForm;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/personal/open")
public class OpenController {

//    private CardRepository cardRepository;
    private PasswordEncoder passwordEncoder;
    private PersonalCardRepository personalCardRepository;
    private UserRepository userRepository;

    public OpenController(PersonalCardRepository personalCardRepository,UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.personalCardRepository = personalCardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String openForm(Model model) {
        model.addAttribute("openForm", new OpenForm());
        return "open";
    }

    @PostMapping
    public String processOpen(
            @Valid
                    OpenForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "open";
        }
        personalCardRepository.save(form.toCard(userRepository, passwordEncoder));
        return "redirect:/users/personal";
    }
}
