package net.koedooder.kvk.pva.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostalCodeValidatorDTO {
    private String countryCode;
    private String postalCodeFormat;
    private String postalCodeCheckRegex;

    public static PostalCodeValidatorDTO of(PostalCodeValidator postalCodeValidator){
        return PostalCodeValidatorDTO.builder()
                .countryCode(postalCodeValidator.getCountryCode())
                .postalCodeCheckRegex(postalCodeValidator.getPostalCodeCheckRegex())
                .postalCodeFormat(postalCodeValidator.getPostalCodeFormat())
                .build();
    }
}
