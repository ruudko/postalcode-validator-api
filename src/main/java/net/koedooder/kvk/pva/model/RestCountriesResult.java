package net.koedooder.kvk.pva.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestCountriesResult {
    private PostalCodeFormat postalCode;

    @Data
    @Builder
    public static class PostalCodeFormat {
        private String format;
        private String regex;
    }
}
