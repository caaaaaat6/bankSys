package com.example.banksys.presentationlayer.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 转账时，转入账户ID与姓名匹配注解
 */
@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ToUserIdNameMatchesValidator.class)
@Documented
public @interface ToUserIdNameMatches {
    String message() default "转入账户ID与转入账户姓名不匹配！";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

