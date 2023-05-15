package com.example.banksys.config;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.MyLogoutHandler;
import com.example.banksys.presentationlayer.MyLogoutSuccessHandler;
import com.example.banksys.presentationlayer.exception.CardIdNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class PresentationLayerConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return userId -> {
            Optional<User> byId = userRepository.findById(Long.parseLong(userId));
            if (byId.isEmpty()) {
                throw new UsernameNotFoundException("Account '" + userId + "' not found");
            }
            return byId.get();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] patternsToAuthorize = new String[]{"/users/personal/withdraw"};
        String[] patternsToPermit = new String[]{"/","/users/personal/","/users/personal/open"};
        return http

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/personal/deposit").hasAnyRole("ORDINARY", "VIP")
                        .anyRequest().permitAll())
                .formLogin()
                    .and()
                .logout()
//                    .addLogoutHandler(new MyLogoutHandler())
                    .logoutSuccessHandler(new MyLogoutSuccessHandler())
                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                .csrf()
                    .disable()
                .build();
    }
}
