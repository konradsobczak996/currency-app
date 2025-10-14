package com.example.currency.dto;

public class ValueResponse {

    private final double value;

    public ValueResponse(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}