package com.example.banksys.config;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.presentationlayer.exception.CardIdNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public UserDetailsService userDetailsService(PersonalCardRepository personalCardRepository) throws CardIdNotFoundException {
        return cardId -> {
            Optional<PersonalCard> card = personalCardRepository.findById(Long.parseLong(cardId));
            if (card.isEmpty()) {
                throw new CardIdNotFoundException("Card '" + cardId + "' notfound");
            }
            return card.get();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] patternsToAuthorize = new String[]{"/users/personal/withdraw"};
        String[] patternsToPermit = new String[]{"/","/users/personal/","/users/personal/open"};
        return http
                .formLogin()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(patternsToPermit).permitAll()
                .requestMatchers("/**").hasRole("USER")
//                .requestMatchers(patternsToAuthorize).hasRole("USER")
//                .requestMatchers("/", "/**").hasRole("USER")
//                .anyRequest().denyAll()
                .and()
                .csrf()
                .disable()
                .build();
    }
}
