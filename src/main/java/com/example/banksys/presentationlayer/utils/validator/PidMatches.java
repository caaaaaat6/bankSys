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
 * 身份证号与账户ID的匹配注解
 */
@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PidMatchesValidator.class)
@Documented
public @interface PidMatches {
    String message() default "身份证号不匹配！";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
