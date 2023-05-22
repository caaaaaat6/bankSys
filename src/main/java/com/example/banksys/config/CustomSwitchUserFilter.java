package com.example.banksys.config;

import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import java.util.Optional;

/**
 * 自定义SwitchUserFilter，以实现雇员登录用户账户时不仅输入用户账户的ID，还要输入用户账户的密码，并验证
 */
public class CustomSwitchUserFilter extends SwitchUserFilter  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public CustomSwitchUserFilter(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 重写切换用户方法
     * @param request
     * @return
     * @throws BadCredentialsException 账户ID或密码错误
     */
    @Override
    protected Authentication attemptSwitchUser(HttpServletRequest request) throws BadCredentialsException {
        // Retrieve the user's name and password from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> byUserIdAndPassword = userRepository.findById(Long.parseLong(username));

        if (byUserIdAndPassword.isEmpty()) {
            throw new BadCredentialsException("账户ID或密码错误！");
        }

        boolean matches = passwordEncoder.matches(password, byUserIdAndPassword.get().getPassword());

        if (!matches) {
            throw new BadCredentialsException("账户ID或密码错误！");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(byUserIdAndPassword.get(), password, byUserIdAndPassword.get().getAuthorities());
        return authentication;
    }
}
