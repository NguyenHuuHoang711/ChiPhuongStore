
package com.my_shop.authservice.src.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordLengthValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidPasswordLength {
    String message() default  "INVALID_LENGTH";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
