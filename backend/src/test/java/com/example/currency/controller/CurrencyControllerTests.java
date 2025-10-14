package com.example.currency.controller;

import com.example.currency.service.TablesFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTests {

    @Autowired MockMvc mvc;

    @MockitoBean
    TablesFetcher fetcher;

    @Test
    void post_happy_path_then_get_history() throws Exception {
        when(fetcher.fetch()).thenReturn(read("json/nbp/table-a-eur.json"));

        String body = read("json/requests/post-eur.json");

        mvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").value(4.2954));

        mvc.perform(get("/currencies/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[0].name").value("Jan Nowak"))
                .andExpect(jsonPath("$[0].value").value(4.2954));
    }

    @Test
    void post_returns_400_on_validation_error() throws Exception {
        mvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(read("json/requests/post-invalid.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_returns_404_when_currency_not_found() throws Exception {
        when(fetcher.fetch()).thenReturn(read("json/nbp/table-a-empty.json"));

        mvc.perform(post("/currencies/get-current-currency-value-command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(read("json/requests/post-xyz.json")))
                .andExpect(status().isNotFound());
    }

    private String read(String path) throws Exception {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
