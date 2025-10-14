package com.example.currency.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class NbpTablesFetcher implements TablesFetcher {

    private final String BASE_URL = "http://api.nbp.pl";
    private final String TABLE_URI = "/api/exchangerates/tables/A?format=json";

    private final RestClient rest;

    NbpTablesFetcher(RestClient.Builder builder) {
        this.rest = builder.baseUrl(BASE_URL).build();
    }

    @Override
    public String fetch() {
        return rest.get().uri(TABLE_URI)
                .retrieve().body(String.class);
    }

}
