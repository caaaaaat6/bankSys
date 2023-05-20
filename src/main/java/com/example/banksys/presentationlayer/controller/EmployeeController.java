package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.businesslogiclayer.service.EmployeeService;
import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Department;
import com.example.banksys.presentationlayer.form.RegisterForm;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import com.example.banksys.presentationlayer.utils.BeanNameUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/employee/")
@SessionAttributes({"registerForm","employee"})
public class EmployeeController {

    private PasswordEncoder passwordEncoder;
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private ApplicationContext context;
    private EmployeeService service;

    public EmployeeController(PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository,DepartmentRepository departmentRepository, ApplicationContext context, EmployeeService employeeServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.context = context;
        this.service = employeeServiceImpl;
    }

    @ModelAttribute("registerForm")
    public RegisterForm createRegisterForm() {
        return new RegisterForm();
    }

    @GetMapping
    public String getIndexPage(Model model, Authentication authentication) {
        GetPageHelper.addEmployee(model, authentication, employeeRepository);
        return "index_employee";
    }

    @GetMapping("/register/")
    public String getRegisterPage(Model model) {
        Iterable<Department> all = departmentRepository.findAll();
        model.addAttribute("departments", all);
        return "register_employee";
    }

    @PostMapping("/register/")
    public String postRegisterPage(Model model, @Validated RegisterForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "register_employee";
        }
        String beanName = BeanNameUtil.getBeanName(form);
        BaseEmployeeAccount account = (BaseEmployeeAccount) context.getBean(beanName);
        Long id = service.register(employeeRepository, departmentRepository, passwordEncoder, form);
        model.addAttribute("id", id);
        ToFrontendHelper.addSuccessMessage(model, "您的登录ID为" + id + "，请牢记！\n注册信息已提交系统管理员，请耐心等待审核。");
        return "success";
    }
}
