package net.koedooder.kvk.pva.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.koedooder.kvk.pva.model.PostalCodeValidator;
import net.koedooder.kvk.pva.model.PostalCodeValidatorDTO;
import net.koedooder.kvk.pva.model.RestCountriesAPIResult;
import net.koedooder.kvk.pva.repository.PostalCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class PostalCodeService {
    private final PostalCodeRepository postalCodeRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${custom.restcountries.api.url}")
    private String restCountriesApiUrl;
    @Value("${custom.restcountries.api.timeout_in_seconds:5}")
    private int restCountriesApiTimeoutInSeconds;

    private WebClient restCountriesClient;

    public PostalCodeService(PostalCodeRepository postalCodeRepository,
                             WebClient.Builder webClientBuilder){
        this.postalCodeRepository = postalCodeRepository;
        this.webClientBuilder = webClientBuilder;
    }
    @PostConstruct
    public void init(){
        createWebClient(restCountriesApiUrl);
    }
    private void createWebClient(String baseURL) {
        this.restCountriesApiUrl = baseURL;
        this.restCountriesClient = webClientBuilder
                .baseUrl(restCountriesApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    public Optional<PostalCodeValidatorDTO> getPostalCodeValidatorForCountry(final String countryCode){
        return postalCodeRepository.findByCountryCode(countryCode).map(PostalCodeValidatorDTO::of);
    }
    public PostalCodeValidatorDTO addPostalCodeValidatorForCountry(final String countryCode){
        RestCountriesAPIResult restCountriesApiResult = getRestCountriesApiResult(countryCode);
        PostalCodeValidator newPostalCode = PostalCodeValidator.builder()
                .postalCodeFormat(restCountriesApiResult.getPostalCode().getFormat())
                .countryCode(countryCode)
                .postalCodeCheckRegex(restCountriesApiResult.getPostalCode().getRegex())
                .build();
        postalCodeRepository.save(newPostalCode);
        return PostalCodeValidatorDTO.of(newPostalCode);
    }

    private RestCountriesAPIResult getRestCountriesApiResult (String countryCode){
        return restCountriesClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{countryCode}/")
                        .queryParam("fields", "postalCode")
                        .build(countryCode))
                .retrieve()
                .bodyToMono(RestCountriesAPIResult.class)
                .block(Duration.ofSeconds(restCountriesApiTimeoutInSeconds));
    }
}
