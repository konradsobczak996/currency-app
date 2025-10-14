package com.example.currency.service;

import com.example.currency.dto.CurrencyCodeView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class NbpSimpleService {

    private final TablesFetcher fetcher;
    private final ObjectMapper mapper;

    public NbpSimpleService(TablesFetcher fetcher, ObjectMapper mapper) {
        this.fetcher = fetcher;
        this.mapper = mapper;
    }

    public String getRawTablesJson() {
        try {
            return fetcher.fetch(); }
        catch (RestClientException error) {
            throw new ResponseStatusException(BAD_GATEWAY, "Gateway error", error);
        }
    }


    public double getMidRate(String code) {
        String target;
        String body;

        if (StringUtils.isBlank(code)) {
            target = "";
        } else {
            target = code.trim().toUpperCase();
        }

        try {
            body = getRawTablesJson();
        } catch (RestClientException error) {
            throw new ResponseStatusException(BAD_GATEWAY, "Gateway error", error);
        }

        try {
            JsonNode rates = mapper.readTree(body).get(0).get("rates");
            for (JsonNode rate : rates) {
                if (target.equals(rate.get("code").asText())) {
                    return rate.get("mid").asDouble();
                }
            }
            throw new ResponseStatusException(NOT_FOUND, "Currency not found: " + target);
        } catch (ResponseStatusException error) {
            throw error;
        } catch (IOException error) {
            throw new ResponseStatusException(BAD_GATEWAY, "Response parsing error", error);
        } catch (Exception error) {
            throw new ResponseStatusException(BAD_GATEWAY, "Error", error);
        }
    }

    public List<CurrencyCodeView> listCodes() {
        String body = getRawTablesJson();
        try {
            JsonNode root = mapper.readTree(body);
            JsonNode rates = root.get(0).get("rates");

            List<CurrencyCodeView> out = new ArrayList<>();
            for (JsonNode r : rates) {
                String code = r.get("code").asText();
                String name = r.get("currency").asText();
                out.add(new CurrencyCodeView(code, name));
            }

            out.sort(Comparator.comparing(CurrencyCodeView::getCode));
            return out;

        } catch (IOException e) {
            throw new ResponseStatusException(BAD_GATEWAY, "Response parsing error", e);
        }
    }
}