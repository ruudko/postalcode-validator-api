package net.koedooder.kvk.pva.controller;

import net.koedooder.kvk.pva.model.PostalCodeValidatorDTO;
import net.koedooder.kvk.pva.service.PostalCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostalCodeController.class)
public class PostCodeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostalCodeService postalCodeService;

    @Test
    void givenRequestCountryCode_whenNotAvailable_thenReturnNotFound() throws Exception {
        when(postalCodeService.getPostalCodeValidatorForCountry(anyString())).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/api/v1/postalcodevalidator/nl")).andExpect(status().isNotFound());
    }
    @Test
    void givenRequestCountryCode_whenAvailable_thenReturnOk() throws Exception {
        PostalCodeValidatorDTO postalCodeValidatorDTO = PostalCodeValidatorDTO.builder().postalCodeFormat("format")
                .postalCodeCheckRegex("regex").countryCode("cc").build();
        when(postalCodeService.getPostalCodeValidatorForCountry(anyString())).thenReturn(Optional.of(postalCodeValidatorDTO));
        this.mockMvc.perform(get("/api/v1/postalcodevalidator/nl")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("{\"countryCode\":\"cc\",\"postalCodeFormat\":\"format\",\"postalCodeCheckRegex\":\"regex\"}")));
    }
    @Test
    void givenPostCountryCode_whenNotAvailable_thenReturnOk() throws Exception {
        when(postalCodeService.addPostalCodeValidatorForCountry(anyString())).thenReturn(mock(PostalCodeValidatorDTO.class));
        this.mockMvc.perform(post("/api/v1/postalcodevalidator/nl")).andExpect(status().isOk());
    }
    @Test
    void givenUnknownPostCountryCode_whenNotAvailable_thenReturnOk() throws Exception {
        when(postalCodeService.addPostalCodeValidatorForCountry(anyString())).thenReturn(mock(PostalCodeValidatorDTO.class));
        this.mockMvc.perform(post("/api/v1/postalcodevalidator/nlx")).andExpect(status().is(400));
    }
}
