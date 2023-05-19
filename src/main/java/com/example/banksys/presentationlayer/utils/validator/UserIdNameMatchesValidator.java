package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class UserIdNameMatchesValidator  {

    @Autowired
    private UserRepository userRepository;

    public UserIdNameMatchesValidator() {
    }

    public UserIdNameMatchesValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
