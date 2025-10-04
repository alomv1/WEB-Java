package com.weblabs.webjava.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final List<String> COSMIC_WORDS = Arrays.asList(
            "star", "galaxy", "comet", "cosmic", "space", "nebula",
            "planet", "asteroid", "meteor", "orbit", "lunar", "solar",
            "stellar", "astro", "celestial", "interstellar", "supernova"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String lowerValue = value.toLowerCase();
        return COSMIC_WORDS.stream().anyMatch(lowerValue::contains);
    }
}