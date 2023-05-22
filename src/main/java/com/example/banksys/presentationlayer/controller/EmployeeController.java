package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.businesslogiclayer.service.EmployeeService;
import com.example.banksys.businesslogiclayer.service.UserService;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Department;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.AddDepartmentForm;
import com.example.banksys.presentationlayer.form.AddEnterpriseForm;
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

import java.util.List;

import static com.example.banksys.presentationlayer.controller.EmployeeController.EMPLOYEE_ACCOUNT;


@Controller
@RequestMapping("/employee/")
@SessionAttributes({"registerForm", "employee", EMPLOYEE_ACCOUNT})
public class EmployeeController {
    public static final String EMPLOYEE_ACCOUNT = "employeeAccount";
    private PasswordEncoder passwordEncoder;
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private ApplicationContext context;
    private EmployeeService service;

    public EmployeeController(PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ApplicationContext context, EmployeeService employeeServiceImpl) {
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

    @ModelAttribute
    public AddDepartmentForm createAddDepartmentForm() {
        return new AddDepartmentForm();
    }

    @GetMapping
    public String getIndexPage(Model model, Authentication authentication) {
        GetPageHelper.addEmployee(model, authentication, employeeRepository);
        GetPageHelper.addEmployeeAccountAttribute(model, authentication, context, employeeRepository);
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
        Long id = service.register(employeeRepository, departmentRepository, passwordEncoder, form);
        model.addAttribute("id", id);
        ToFrontendHelper.addSuccessMessage(model, "您的登录ID为" + id + "，请牢记！\n注册信息已提交系统管理员，请耐心等待审核。");
        return "success";
    }

    @GetMapping("/register/add_department")
    public String getAddDepartmentPage() {
        return "employee_add_department";
    }

    @PostMapping("/register/add_department")
    public String postAddDepartmentPage(Model model, @Validated AddDepartmentForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "employee_add_department";
        }
        service.addDepartment(departmentRepository, form);
        ToFrontendHelper.addSuccessMessage(model, "添加部门成功！");
        return "success";
    }

    @GetMapping("/find_managed")
    public String findManaged(Model model) {
        BaseEmployeeAccount account = (BaseEmployeeAccount) model.getAttribute(EMPLOYEE_ACCOUNT);
        List<Employee> employeeManaged = service.findEmployeeManaged(account);
        model.addAttribute("employees", employeeManaged);
        return "find_managed";
    }

    @GetMapping("/find_report")
    public String findReport(Model model) {
        BaseEmployeeAccount account = (BaseEmployeeAccount) model.getAttribute(EMPLOYEE_ACCOUNT);
        List<AccountLog> report = service.findReport(account);
        model.addAttribute("report", report);
        return "/find_report";
    }


}
