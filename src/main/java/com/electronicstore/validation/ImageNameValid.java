package com.electronicstore.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy =ImageValidation.class)
public @interface ImageNameValid  {

    String message() default "Invalid Image Name";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}
