package com.example.banksys.presentationlayer.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
  implements ConstraintValidator<PasswordMatches, Object> {
    
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        OpenForm openForm = (OpenForm) o;
        if (openForm.getPassword() == null || openForm.getConfirm() == null) {
            return false;
        }
        return openForm.getPassword().equals(openForm.getConfirm());
    }
}