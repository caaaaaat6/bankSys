package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.form.ChangePasswordForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;

public class PidMatchesValidator implements ConstraintValidator<PidMatches, Object> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ChangePasswordForm form = (ChangePasswordForm) value;
        Long userId = form.getUserId();
        String pid = form.getUserPid();
        User user = userRepository.findById(userId).get();
        return user.getUserPid().equals(pid);
    }
}
