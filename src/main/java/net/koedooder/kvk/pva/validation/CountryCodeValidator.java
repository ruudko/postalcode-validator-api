package net.koedooder.kvk.pva.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Locale;

public class CountryCodeValidator implements ConstraintValidator<CountryCodeConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return List.of(Locale.getISOCountries()).contains(value.toUpperCase());
    }
}
