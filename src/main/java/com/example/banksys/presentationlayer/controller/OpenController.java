package com.example.banksys.presentationlayer.controller;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.presentationlayer.OpenForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String openForm() {
        return "open";
    }

    @PostMapping
    public String processOpen( OpenForm form) {
        personalCardRepository.save(form.toCard(userRepository, passwordEncoder));
        return "redirect:/users/personal/open";
    }
}
