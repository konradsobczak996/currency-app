package com.example.currency.dto;

import java.time.Instant;

public class CurrencyRequestView {

    private final Long id;
    private final String currency;
    private final String name;
    private final Instant date;
    private final double value;

    public CurrencyRequestView(Long id,String currency, String name, Instant date, double value) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.date = date;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }
    public String getName() {
        return name;
    }
    public Instant getDate() {
        return date;
    }
    public double getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }}