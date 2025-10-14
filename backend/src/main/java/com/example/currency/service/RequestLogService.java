package com.example.currency.service;

import com.example.currency.entity.CurrencyRequest;
import com.example.currency.repo.CurrencyRequestRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RequestLogService {

    private final CurrencyRequestRepository repo;

    public RequestLogService(CurrencyRequestRepository repo) {
        this.repo = repo;
    }

    public void log(String currencyCode, String requesterName, double value) {
        repo.save(new CurrencyRequest(currencyCode, requesterName, Instant.now(), value));
    }
}
