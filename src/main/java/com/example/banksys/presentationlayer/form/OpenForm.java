package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.validator.PasswordMatches;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PasswordMatches
@Slf4j
@Data
@NoArgsConstructor(force = true) // 如果有其他有参构造方法而没有无参构造，则强迫生成无参构造方法。“强迫”在是否有其他有参构造方法
public class OpenForm implements ConfirmPasswordForm {

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


//
//    public PersonalCard toCard(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        String encrypted = passwordEncoder.encode(password);
//        User user = new User();
//        user.setUserPid(userPid);
//        user.setUserName(userName);
//        user.setUserType(userType);
//        user.setPassword(null); // 企业用户这里才需要设置密码；
//        Long userId = userRepository.save(user).getUserId();
//        PersonalCard card = new PersonalCard();
//        card.setUserId(userId);
//        card.setUserPid(userPid);
//        card.setUserName(userName);
//        card.setUserType(userType);
//        card.setPassword(encrypted);
//        card.setCardType(cardType);
//        card.setOpenMoney(openMoney);
//        return card;
//    }
}
