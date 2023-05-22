package com.example.banksys.presentationlayer.helper;

import org.springframework.ui.Model;

public class ToFrontendHelper {

    /**
     * 向model添加错误信息
     * @param model
     * @param message
     */
    public static void addErrorMessage(Model model, String message) {
        model.addAttribute("errorMessage", message);
    }

    /**
     * 向model添加成功信息
     * @param model
     * @param message
     */
    public static void addSuccessMessage(Model model, String message) {
        model.addAttribute("successMessage", message);
    }

    /**
     * 向model添加表单的Post url
     * @param model
     * @param postTo
     */
    public static void addPostUrl(Model model, String postTo) {
        model.addAttribute("postTo", postTo);
    }
}
