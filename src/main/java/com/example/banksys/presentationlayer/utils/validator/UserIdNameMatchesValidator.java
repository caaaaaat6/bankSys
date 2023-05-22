package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * 用户账户Id与姓名的匹配注解实现基类
 */
public abstract class UserIdNameMatchesValidator  {

    @Autowired
    private UserRepository userRepository;

    public UserIdNameMatchesValidator() {
    }

    public UserIdNameMatchesValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 检查账户ID与姓名
     * @param userId
     * @param userName
     * @return 匹配返回true，否则返回false
     */
    public boolean checkIdAndName(Long userId, String userName) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            return false;
        }
        boolean equal = byId.get().getUserName().equals(userName);
        if (equal) {
            return true;
        }
        return false;
    }
}
