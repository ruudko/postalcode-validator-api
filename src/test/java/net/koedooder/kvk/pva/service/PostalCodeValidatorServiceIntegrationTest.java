package net.koedooder.kvk.pva.service;


import net.koedooder.kvk.pva.model.PostalCodeValidatorDTO;
import net.koedooder.kvk.pva.model.RestCountriesAPIResult;
import net.koedooder.kvk.pva.repository.PostalCodeRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ContextConfiguration
public class PostalCodeValidatorServiceIntegrationTest {
    static MockWebServer mockBackEnd;

    @Autowired
    PostalCodeService postalCodeService;

    @MockitoBean
    PostalCodeRepository postalCodeRepository;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("custom.restcountries.api.url", () ->
                String.format("http://127.0.0.1:%s/v3.1/alpha", mockBackEnd.getPort()));
    }

    @Test
    public void givenUnknownCountryCode_whenGetPostalCode_thenReturnEmpty(){
        Mockito.when(postalCodeRepository.findByCountryCode(any())).thenReturn(Optional.empty());
        Optional<PostalCodeValidatorDTO> nlValidator = postalCodeService.getPostalCodeValidatorForCountry("nl");
        assertTrue(nlValidator.isEmpty());
    }
    @Test
    public void givenUnknownCountryCode_whenAddPostalCode_thenReturnNewPostalCode(){
        RestCountriesAPIResult.builder()
                .postalCode(RestCountriesAPIResult.PostalCodeFormat.builder()
                        .format("").regex("").build()).build();
        mockBackEnd.enqueue(new MockResponse()
                .setBody("{\"postalCode\":{\"format\":\"#### @@\",\"regex\":\"^(\\\\d{4}[A-Z]{2})$\"}}")
                .addHeader("Content-Type", "application/json"));
        PostalCodeValidatorDTO nlValidator = postalCodeService.addPostalCodeValidatorForCountry("nl");

        assertEquals("^(\\d{4}[A-Z]{2})$", nlValidator.getPostalCodeCheckRegex());
        assertEquals("#### @@", nlValidator.getPostalCodeFormat());
        assertEquals("nl", nlValidator.getCountryCode());
    }
    @Test
    public void givenUnknownCountryCode_whenAPITimout_thenReturnServiceUnavailable(){
        //stop mock http server
        //request, check http code 503
    }
}
