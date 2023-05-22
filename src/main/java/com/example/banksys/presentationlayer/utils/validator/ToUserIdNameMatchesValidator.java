package com.example.banksys.presentationlayer.utils.validator;

import com.example.banksys.presentationlayer.form.TransferForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 转账时，转入账户ID与姓名匹配注解实现类
 */
public class ToUserIdNameMatchesValidator extends UserIdNameMatchesValidator implements ConstraintValidator<ToUserIdNameMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        TransferForm form = (TransferForm) value;
        return checkIdAndName(form.getToUserId(), form.getToName());
    }
}
