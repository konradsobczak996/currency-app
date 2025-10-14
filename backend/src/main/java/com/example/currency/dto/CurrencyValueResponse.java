package com.example.currency.dto;

public class CurrencyValueResponse {

    private final double value;

    public CurrencyValueResponse(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
