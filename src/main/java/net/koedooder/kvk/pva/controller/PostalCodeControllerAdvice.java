package net.koedooder.kvk.pva.controller;

import net.koedooder.kvk.pva.exceptions.PostalCodeValidatorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeoutException;

@RestControllerAdvice(assignableTypes = PostalCodeController.class)
public class PostalCodeControllerAdvice {

    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<String> handleRestCountriesApiTimeout() {
        return new ResponseEntity<>("Failed to receive results from restcountries API.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(PostalCodeValidatorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePostalCodeValidatorNotFoundException() {
        return new ResponseEntity<>("Postal code validator not found.",
                HttpStatus.NOT_FOUND);
    }
}
