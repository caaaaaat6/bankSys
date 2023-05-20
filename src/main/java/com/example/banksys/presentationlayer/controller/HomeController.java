package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.EmployeeService;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.presentationlayer.form.ReviewForm;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    public HomeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @ModelAttribute("reviewForm")
    public ReviewForm createReviewForm() {
        return new ReviewForm();
    }

    @GetMapping("/")
    public String routUser() {
        return "rout";
    }

    @GetMapping("/users/personal/")
    public String personalUser() {
        return "personal_user";
    }

    @GetMapping("/users/enterprise/")
    public String enterpriseUser() {
        return "enterprise_user";
    }

    @GetMapping("/employee")
    public String employee() {
        return "index_employee";
    }

    @GetMapping("/admin/")
    public String admin(Model model) {
        List<Employee> employeeNotEnabled = employeeService.findEmployeeNotEnabled(employeeRepository);
        model.addAttribute("employees", employeeNotEnabled);
        return "admin";
    }

    @PostMapping("/admin/")
    public String adminPost(Model model, ReviewForm form) {
        employeeService.reviewEmployee(employeeRepository, form);
        ToFrontendHelper.addSuccessMessage(model, "审核操作成功！");
        return "success";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
}
