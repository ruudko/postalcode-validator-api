package net.koedooder.kvk.pva.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.koedooder.kvk.pva.model.PostalCode;
import net.koedooder.kvk.pva.model.RestCountriesResult;
import net.koedooder.kvk.pva.repository.PostalCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Slf4j
public class PostalCodeService {
    private final PostalCodeRepository postalCodeRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${custom.restcountries.api.url}")
    private String restCountriesApiUrl;
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
    @Transactional
    public PostalCode getPostalCodeInfo(final String countryCode){
        Optional<PostalCode> postalCode = postalCodeRepository.findByCountryCode(countryCode);

        if (postalCode.isPresent()) {
            return postalCode.get();
        } else {
            RestCountriesResult restCountriesApiResult = getRestCountriesApiResult(countryCode);
            PostalCode newPostalCode = new PostalCode();
            newPostalCode.setCountryCode(countryCode);
            newPostalCode.setPostalCodeFormat(restCountriesApiResult.getPostalCode().getFormat());
            newPostalCode.setPostalCodeCheckRegex(restCountriesApiResult.getPostalCode().getRegex());
            postalCodeRepository.save(newPostalCode);
            return newPostalCode;
        }
    }

    private RestCountriesResult getRestCountriesApiResult(String countryCode){
        return restCountriesClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{countryCode}/")
                        .queryParam("fields", "postalCode")
                        .build(countryCode))
                .retrieve()
                .bodyToMono(RestCountriesResult.class)
                .block();
    }
}
