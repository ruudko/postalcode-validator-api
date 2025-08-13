package net.koedooder.kvk.pva.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.koedooder.kvk.pva.exceptions.PostalCodeValidatorNotFoundException;
import net.koedooder.kvk.pva.model.PostalCodeValidatorDTO;
import net.koedooder.kvk.pva.service.PostalCodeService;
import net.koedooder.kvk.pva.validation.CountryCodeConstraint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@OpenAPIDefinition(tags = {
        @Tag(name = "postalcodevalidator", description = "Get and create new postal code validators"),
        @Tag(name = "create", description = "Create postal code validator"),
        @Tag(name = "get", description = "Get postal code validator")
})
public class PostalCodeController {

   private final PostalCodeService postalCodeService;

    public PostalCodeController(PostalCodeService postalCodeService){
        this.postalCodeService = postalCodeService;
    }
    @Operation(summary = "Get a postal code validator by country code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the postal code validator",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostalCodeValidatorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Postal code validator not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Country code not valid (ISO 3166)",
                    content = @Content)})
    @Tag(name = "postalcodevalidator")
    @Tag(name = "get")
    @GetMapping(path = "/postalcodevalidator/{countryCode}", produces = "application/json")
    public ResponseEntity<PostalCodeValidatorDTO> getPostalCodeValidation(
            @PathVariable @CountryCodeConstraint String countryCode) throws PostalCodeValidatorNotFoundException {
        Optional<PostalCodeValidatorDTO> postalCodeValidatorForCountry =
                postalCodeService.getPostalCodeValidatorForCountry(countryCode);
        return postalCodeValidatorForCountry
                .map(ResponseEntity::ok).orElseThrow(PostalCodeValidatorNotFoundException::new);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a new postal code validator",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostalCodeValidatorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Country code not valid (ISO 3166)",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Could not connect to restcountries API",
                    content = @Content)})
    @Tag(name = "postalcodevalidator")
    @Tag(name = "create")
    @PostMapping(path = "/postalcodevalidator/{countryCode}", produces = "application/json")
    public ResponseEntity<PostalCodeValidatorDTO> setPostalCodeValidation(
            @PathVariable @CountryCodeConstraint String countryCode) {
        return new ResponseEntity<>(
                postalCodeService.addPostalCodeValidatorForCountry(countryCode), HttpStatus.CREATED);
    }
}