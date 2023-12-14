package com.example.banksys.config;

import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.MyLogoutSuccessHandler;
import com.example.banksys.presentationlayer.utils.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class PresentationLayerConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置UserDetailsService
     * @param userRepository
     * @param employeeRepository
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        // 返回一个接口的实现
        return userId -> {
            Long id = Long.parseLong(userId);
            Optional<Employee> employee = employeeRepository.findById(id);
            // 给一个ID，如果雇员表存在这个ID，那么返回雇员
            if (employee.isPresent()) {
                return employee.get();
            }
            // 否则雇员表不存在这个ID，那检查客户表
            Optional<User> byId = userRepository.findById(id);
            // 客户表不存在，抛错
            if (byId.isEmpty()) {
                throw new UsernameNotFoundException("Account '" + userId + "' not found");
            }
            // 否则返回客户
            return byId.get();
        };
    }

    /**
     * 配置SwitchUserFilter，用来雇员切换为客户账户，为客户操作账户
     * @param userRepository
     * @return 自定义的SwitchUserFilter
     */
    @Bean
    public SwitchUserFilter customSwitchUserFilter(UserRepository userRepository) {
        SwitchUserFilter filter = new CustomSwitchUserFilter(userRepository, passwordEncoder());
        filter.setUserDetailsService(userId -> {
            Long id = Long.parseLong(userId);
            Optional<User> byId = userRepository.findById(id);
            if (byId.isEmpty()) {
                throw new UsernameNotFoundException("Account '" + userId + "' not found");
            }
            return byId.get();
        });

        filter.setSwitchUserUrl("/employee/impersonate");
        filter.setExitUserUrl("/employee/impersonate/exit");
        filter.setSwitchFailureUrl("/employee/impersonate/error");
        filter.setTargetUrl("/");
        return filter;
    }

    /**
     * security配置，权限配置
     * @param http
     * @return
     * @throws Exception
     */
    /*
    * SecurityFilterChain这个Bean用来过滤权限
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/info").hasAnyRole(Role.ENTERPRISE_USER, Role.ORDINARY_USER, Role.VIP_USER)
                        .requestMatchers("/users/personal/").permitAll()
                        .requestMatchers("/users/personal/open").permitAll()
                        .requestMatchers("/users/personal/deposit/**").hasAnyRole("ORDINARY", "VIP")
                        .requestMatchers("/users/personal/deposit/current").hasRole("CURRENT")
                        .requestMatchers("/users/personal/deposit/fixed").hasRole("FIXED")
                        .requestMatchers("/users/personal/**").hasAnyRole("ORDINARY", "VIP")
                        .requestMatchers("/users/enterprise/open").permitAll()
                        .requestMatchers("/users/enterprise/open/**").permitAll()
                        .requestMatchers("/users/enterprise/manage").hasRole(Role.ENTERPRISE_SUPER)
                        .requestMatchers("/users/enterprise/deposit/current").hasRole("CURRENT")
                        .requestMatchers("/users/enterprise/deposit/fixed").hasRole("FIXED")
                        .requestMatchers("/users/enterprise/**").hasAnyRole(Role.ENTERPRISE_USER)
                        .requestMatchers("/admin/").hasRole(Role.SYSTEM_ADMIN)
                        .requestMatchers("/employee/register/").permitAll()
                        .requestMatchers("/employee/register/**").permitAll()
//                        .requestMatchers("/employee/impersonate/exit").hasAnyRole(Role.FRONT_DESK_EMPLOYEE, Role.MANAGER_EMPLOYEE, Role.CURRENT_HEAD_EMPLOYEE, Role.FIXED_HEAD_EMPLOYEE, "PREVIOUS_ADMINISTRATOR")
                        .requestMatchers("/employee/impersonate/exit").authenticated()
                        .requestMatchers("/employee/impersonate").hasAnyRole(Role.FRONT_DESK_EMPLOYEE, Role.MANAGER_EMPLOYEE, Role.CURRENT_HEAD_EMPLOYEE, Role.FIXED_HEAD_EMPLOYEE)
                        .requestMatchers("/employee/**").hasAnyRole(Role.FRONT_DESK_EMPLOYEE, Role.MANAGER_EMPLOYEE, Role.CURRENT_HEAD_EMPLOYEE, Role.FIXED_HEAD_EMPLOYEE)
                        .anyRequest().permitAll())
                .formLogin() // 自动生成登录表单
                .and()
                .logout() // 进行退出配置
                    .logoutSuccessHandler(new MyLogoutSuccessHandler())
                    .logoutUrl("/logout") // 退出endpoints
                    .invalidateHttpSession(true) // 使会话无效
                    .deleteCookies("JSESSIONID") // 当调用 deleteCookies("JSESSIONID") 时，通常是在清除用户的会话信息，使其注销或退出登录。这样，浏览器中存储的 JSESSIONID Cookie 将被删除
                    .permitAll() // 退出网页是所有用户都允许访问的
                .and()
                .csrf()
                    .disable()
                .build();
    }
}
