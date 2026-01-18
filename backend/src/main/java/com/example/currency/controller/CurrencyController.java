package com.example.currency.controller;

import com.example.currency.dto.CurrencyRequestCommand;
import com.example.currency.dto.CurrencyRequestView;
import com.example.currency.dto.CurrencyValueResponse;
import com.example.currency.entity.CurrencyRequest;
import com.example.currency.repo.CurrencyRequestRepository;
import com.example.currency.service.NbpSimpleService;
import com.example.currency.service.RequestLogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final NbpSimpleService nbp;
    private final RequestLogService logService;
    private final CurrencyRequestRepository repo;

    public CurrencyController(NbpSimpleService nbp, RequestLogService logService, CurrencyRequestRepository repo) {
        this.nbp = nbp;
        this.logService = logService;
        this.repo = repo;
    }

    @PostMapping("/get-current-currency-value-command")
    public ResponseEntity<CurrencyValueResponse> getCurrent(@Valid @RequestBody CurrencyRequestCommand command) {
        double value = nbp.getMidRate(command.getCurrency());
        logService.log(command.getCurrency(), command.getName(), value);
        return ResponseEntity.ok(new CurrencyValueResponse(value));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyRequestView>> list(@PageableDefault Pageable pageable,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
                                                          @RequestParam(required = false) String code) {
        List<CurrencyRequest> entities;

        if (to != null && from != null){
            entities = repo.findByRequestedTimeBetween(pageable,from, to);
        } else {
            if (code == null || code.isBlank()) {
                entities = repo.findAllByOrderByRequestedTimeDesc(pageable);
            } else {
                entities = repo.findByCurrencyCodeIgnoreCaseOrderByRequestedTimeDesc(code);
            }

        }


        List<CurrencyRequestView> views = new ArrayList<>(entities.size());
        for (CurrencyRequest e : entities) {
            views.add(new CurrencyRequestView(
                    e.getId(),
                    e.getCurrencyCode(),
                    e.getRequesterName(),
                    e.getRequestedTime(),
                    e.getRate()
            ));
        }

        return ResponseEntity.ok(views);

    }

    @DeleteMapping("/requests")
    public void delete(@RequestParam Long id) {
        repo.deleteById(id);
    }
}
