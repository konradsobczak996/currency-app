package com.example.currency.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "currency_request")
public class CurrencyRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "requester_name", nullable = false)
    private String requesterName;

    @Column(name = "requested_time", nullable = false)
    private Instant requestedTime;

    @Column(name = "rate", nullable = false)
    private double rate;

    public CurrencyRequest() {

    }

    public CurrencyRequest(String currencyCode, String requesterName, Instant requestedTIme, double rate) {
        this.currencyCode = currencyCode.toUpperCase();
        this.requesterName = requesterName;
        this.requestedTime = requestedTIme;
        this.rate = rate;
    }


    public Long getId() {
        return id;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public Instant getRequestedTime() {
        return requestedTime;
    }
    public double getRate() {
        return rate;
    }
}