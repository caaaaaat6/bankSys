package com.example.banksys.presentationlayer.helper;

import org.springframework.ui.Model;

public class ToFrontendHelper {

    public static void addErrorMessage(Model model, String message) {
        model.addAttribute("errorMessage", message);
    }

    public static void addSuccessMessage(Model model, String message) {
        model.addAttribute("successMessage", message);
    }

    public static void addPostUrl(Model model, String postTo) {
        model.addAttribute("postTo", postTo);
    }
}
