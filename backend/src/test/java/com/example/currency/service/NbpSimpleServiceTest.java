package com.example.currency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NbpSimpleServiceTest {

    @Mock
    private TablesFetcher fetcher;

    private ObjectMapper objectMapper;
    private NbpSimpleService service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new NbpSimpleService(fetcher, objectMapper);
    }

    @Test
    void gets_mid_rate_for_EUR() {
        String json = read("json/nbp/table-a-eur.json");
        when(fetcher.fetch()).thenReturn(json);

        double eur = service.getMidRate("EUR");

        assertThat(eur).isEqualTo(4.2954);
    }

    @Test
    void throws_when_code_not_found() {
        String json = read("json/nbp/table-a-eur.json");
        when(fetcher.fetch()).thenReturn(json);

        assertThrows(RuntimeException.class, () -> service.getMidRate("XYZ"));
    }

    private String read(String path) {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
