package com.ng.runz.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LocalDateValidator.class)
public @interface ValidateLocalDate {
    public String message() default "Invalid Date.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
