package com.example.banksys.presentationlayer.controller;

import com.example.banksys.businesslogiclayer.service.EmployeeService;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.presentationlayer.form.ReviewForm;
import com.example.banksys.presentationlayer.helper.GetPageHelper;
import com.example.banksys.presentationlayer.helper.ToFrontendHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.banksys.presentationlayer.controller.HomeController.ACCOUNT_ATTRIBUTE;

@Slf4j // 日志注解
@Controller // @Controller 实际是一个@Component
@RequestMapping("/") // 将请求是"/"的全部映射到这里
@SessionAttributes({ACCOUNT_ATTRIBUTE})  // 会话属性
public class HomeController {
    public static final String ACCOUNT_ATTRIBUTE = "account";
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;
    private ApplicationContext context;
    private UserRepository userRepository;

    // 在 Spring 中，如果一个类只有一个构造函数，并且该构造函数的所有参数都是 Spring 管理的 bean，
    // Spring 将会尝试自动将这些 bean 注入到相应的构造函数参数中。
    public HomeController(EmployeeService employeeService, EmployeeRepository employeeRepository, ApplicationContext context, UserRepository userRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.context = context;
        this.userRepository = userRepository;
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
    public String enterpriseUser(Model model,Authentication authentication) {
        EnterpriseUserAccount account = (EnterpriseUserAccount) model.getAttribute(ACCOUNT_ATTRIBUTE);
        // 验证通过，检查账户是否为null，为null则从authentication处获取客户user
        if (account == null) {
            // 得到客户账户（并且在这个助手中还设置了操作客户的雇员）
            GetPageHelper.addAccount(model,authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        }
        return "enterprise_user";
    }

    @GetMapping("/employee")
    public String employee() {
        return "index_employee";
    }

    @GetMapping("/admin/")
    public String admin(Model model) {
        // 管理员页面，展示待审核的名单
        List<Employee> employeeNotEnabled = employeeService.findEmployeeNotEnabled(employeeRepository);
        // 加入展示列表，并在model上设置属性employees，方便前端展示
        model.addAttribute("employees", employeeNotEnabled);
        return "admin";
    }

    // 管理员提交审核结果
    @PostMapping("/admin/")
    public String adminPost(Model model, ReviewForm form) {
        // business logic layer中的雇员service对提交来的表单设置enable属性
        employeeService.reviewEmployee(employeeRepository, form);
        // 设置成功消息，给success.xml页面展示
        ToFrontendHelper.addSuccessMessage(model, "审核操作成功！");
        return "success";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // 进行退出工作
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // 重定向至登录页面
        return "redirect:/login?logout"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    @GetMapping("/info")
    // authentication自动装配
    public String getInfo(Model model, Authentication authentication) {
        // 给model添加account属性，以供在info.html页面展示
        GetPageHelper.addAccount(model, authentication, userRepository, context, ACCOUNT_ATTRIBUTE);
        return "info";
    }
}
