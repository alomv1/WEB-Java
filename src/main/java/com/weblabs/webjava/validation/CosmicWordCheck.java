package com.weblabs.webjava.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicWordValidator.class)
@Documented
public @interface CosmicWordCheck {
    String message() default "Product name must contain cosmic terms like 'star', 'galaxy', 'comet', 'cosmic', 'space', 'nebula'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}