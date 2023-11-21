package com.electronicstore.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidation implements ConstraintValidator<ImageNameValid,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
     if (value.isBlank()) {
         return false;
     }
         return true;

    }
}
