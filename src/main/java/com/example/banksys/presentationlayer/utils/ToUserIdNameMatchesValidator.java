package com.example.banksys.presentationlayer.utils;

import com.example.banksys.presentationlayer.form.TransferForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ToUserIdNameMatchesValidator extends UserIdNameMatchesValidator implements ConstraintValidator<ToUserIdNameMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        TransferForm form = (TransferForm) value;
        return checkIdAndName(form.getToUserId(), form.getToName());
    }
}
