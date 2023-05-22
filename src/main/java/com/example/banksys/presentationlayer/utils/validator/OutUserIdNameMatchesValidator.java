package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.presentationlayer.form.TransferForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 转账时，转出账户ID与姓名匹配注解实现
 */
public class OutUserIdNameMatchesValidator extends UserIdNameMatchesValidator implements ConstraintValidator<OutUserIdNameMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        TransferForm form = (TransferForm) value;
        return checkIdAndName(form.getOutUserId(), form.getOutName());
    }
}
