package net.koedooder.kvk.pva.controller;

import net.koedooder.kvk.pva.model.PostalCode;
import net.koedooder.kvk.pva.service.PostalCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PostalCodeController {
   private final PostalCodeService postalCodeService;

    public PostalCodeController(PostalCodeService postalCodeService){
        this.postalCodeService = postalCodeService;
    }
    @GetMapping(path = "/postalcode", produces = "application/json")
    public ResponseEntity<PostalCode> getPostalCode(@RequestParam String countryCode) {
        return ResponseEntity.ok(postalCodeService.getPostalCodeInfo(countryCode));
    }
}