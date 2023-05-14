package com.example.banksys.presentationlayer;

import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.PasswordMatches;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@PasswordMatches
@Slf4j
@Data
@NoArgsConstructor(force = true)
public class OpenForm {

    @NotBlank(message = "不能为空")
    private String userPid;
    @NotBlank(message = "不能为空")
    private String userName;
    @NotBlank(message = "不能为空")
    private String userType;
    @NotBlank(message = "不能为空")
    private String password;
    @NotBlank(message = "不能为空")
    private String confirm;
    @NotBlank(message = "不能为空")
    private String cardType;
    @NotNull
    @Min(value = 0, message = "开户金额不能为负数")
    private double openMoney;

    private String error;



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
