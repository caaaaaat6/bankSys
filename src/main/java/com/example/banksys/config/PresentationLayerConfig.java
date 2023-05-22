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
//@EnableWebMvc
public class PresentationLayerConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        return userId -> {
            Long id = Long.parseLong(userId);
            Optional<Employee> employee = employeeRepository.findById(id);
            if (employee.isPresent()) {
                return employee.get();
            }
            Optional<User> byId = userRepository.findById(id);
            if (byId.isEmpty()) {
                throw new UsernameNotFoundException("Account '" + userId + "' not found");
            }
            return byId.get();
        };
    }



//    @Bean
//    public SwitchUserFilter switchUserFilter(UserDetailsService userDetailsService, UserRepository userRepository) {
//        SwitchUserFilter filter = new SwitchUserFilter();
//        filter.setUserDetailsService(userId -> {
//            Long id = Long.parseLong(userId);
//            Optional<User> byId = userRepository.findById(id);
//            if (byId.isEmpty()) {
//                throw new UsernameNotFoundException("Account '" + userId + "' not found");
//            }
//            return byId.get();
//        });
//
//        filter.setSwitchUserUrl("/employee/impersonate");
//        filter.setExitUserUrl("/employee/impersonate/exit");
//        filter.setSwitchFailureUrl("/employee/");
//        filter.setTargetUrl("/");
//        return filter;
//    }

//    @Bean
//    public UserDetailsService customUserDetailsService(UserRepository userRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
//        return new CustomUserDetailsService(userRepository, passwordEncoder, employeeRepository);
//    }

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
                .formLogin()
                .and()
                .logout()
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .build();
    }
}
