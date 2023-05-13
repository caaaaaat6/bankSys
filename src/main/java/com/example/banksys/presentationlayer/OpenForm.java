package com.example.banksys.presentationlayer;

import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Data
@NoArgsConstructor(force = true)
public class OpenForm {

    private String userPid;
    private String userName;
    private String userType;
    private String password;
    private String cardType;
    private double openMoney;



    public PersonalCard toCard(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        String encrypted = passwordEncoder.encode(password);
        User user = new User();
        user.setUserPid(userPid);
        user.setUserName(userName);
        user.setUserType(userType);
        user.setPassword(null); // 企业用户这里才需要设置密码；
        Long userId = userRepository.save(user).getUserId();
        PersonalCard card = new PersonalCard();
        card.setUserId(userId);
        card.setUserPid(userPid);
        card.setUserName(userName);
        card.setUserType(userType);
        card.setPassword(encrypted);
        card.setCardType(cardType);
        card.setOpenMoney(openMoney);
        return card;
    }
}
