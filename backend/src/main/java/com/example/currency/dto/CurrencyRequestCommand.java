package com.example.currency.dto;

import jakarta.validation.constraints.NotBlank;

public class CurrencyRequestCommand {

    @NotBlank
    private String currency;

    @NotBlank
    private String name;

    public CurrencyRequestCommand() {

    }

    public CurrencyRequestCommand(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}