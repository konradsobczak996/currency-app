package com.example.currency.controller;


import com.example.currency.dto.CurrencyCodeView;
import com.example.currency.dto.ValueResponse;
import com.example.currency.service.NbpSimpleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/nbp")
public class NbpProxyController {

    private final NbpSimpleService nbp;

    public NbpProxyController(NbpSimpleService nbp) {
        this.nbp = nbp;
    }

    @GetMapping(value = "/raw", produces = "application/json")
    public String raw() {
        return nbp.getRawTablesJson();
    }

    @GetMapping("/rate/{code}")
    public ValueResponse rate(@PathVariable String code) {
        if (!code.matches("^[A-Z]{3}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid currency code");
        }
        double value = nbp.getMidRate(code);
        return new ValueResponse(value);
    }

    @GetMapping("/codes")
    public List<CurrencyCodeView> codes() {
        return nbp.listCodes();
    }

}
