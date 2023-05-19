package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.presentationlayer.form.TransferForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OutUserIdNameMatchesValidator extends UserIdNameMatchesValidator implements ConstraintValidator<OutUserIdNameMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        TransferForm form = (TransferForm) value;
        return checkIdAndName(form.getOutUserId(), form.getOutName());
    }
}
