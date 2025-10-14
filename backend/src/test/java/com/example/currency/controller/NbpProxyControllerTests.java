package com.example.currency.controller;


import com.example.currency.dto.CurrencyCodeView;
import com.example.currency.service.NbpSimpleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NbpProxyControllerTests {

    @Mock
    NbpSimpleService nbp;

    @InjectMocks
    NbpProxyController controller;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }


    @Test
    void rate_returns_value_response() throws Exception {
        when(nbp.getMidRate("EUR")).thenReturn(4.2954);

        mvc.perform(get("/nbp/rate/EUR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").value(4.2954));
    }

    @Test
    void rate_returns_not_found_when_service_throws() throws Exception {
        when(nbp.getMidRate("XYZ")).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get("/nbp/rate/XYZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rate_returns_400_on_bad_format() throws Exception {
        mvc.perform(get("/nbp/rate/xx"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void codes_returns_list_of_currency_codes() throws Exception {
        List<CurrencyCodeView> list = List.of(
                new CurrencyCodeView("EUR", "euro"),
                new CurrencyCodeView("USD", "dolar amerykański")
        );
        when(nbp.listCodes()).thenReturn(list);

        mvc.perform(get("/nbp/codes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("EUR"))
                .andExpect(jsonPath("$[0].name").value("euro"))
                .andExpect(jsonPath("$[1].code").value("USD"));
    }

    private String read(String path) throws Exception {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
