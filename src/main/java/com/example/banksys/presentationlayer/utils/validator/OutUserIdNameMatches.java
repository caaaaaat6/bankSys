package com.example.banksys.presentationlayer.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = OutUserIdNameMatchesValidator.class)
@Documented
public @interface OutUserIdNameMatches {
    String message() default "转出账户ID与转出账户姓名不匹配";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
