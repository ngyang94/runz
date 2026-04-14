package com.ng.runz.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateValidator implements ConstraintValidator<ValidateLocalDate, LocalDateTime> {


    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {

        return true;
    }
}
