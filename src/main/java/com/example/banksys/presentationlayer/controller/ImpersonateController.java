package com.example.banksys.presentationlayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee/impersonate")
public class ImpersonateController {

    @GetMapping
    public String getPage() {
        return "impersonate";
    }
}
