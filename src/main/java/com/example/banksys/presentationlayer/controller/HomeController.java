package com.example.banksys.presentationlayer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@RequestMapping("/")
//@SessionAttributes("")
public class HomeController {

    @GetMapping("/")
    public String routUser() {
        return "rout";
    }

    @GetMapping("/users/personal/")
    public String personalUser() {
        return "personaluser";
    }
}
