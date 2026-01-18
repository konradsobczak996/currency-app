package com.example.currency.repo;

import com.example.currency.entity.CurrencyRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CurrencyRequestRepository extends JpaRepository<CurrencyRequest, Long> {
    List<CurrencyRequest> findAllByOrderByRequestedTimeDesc(Pageable pageable);
    List<CurrencyRequest> findByCurrencyCodeIgnoreCaseOrderByRequestedTimeDesc(String currencyCode);
    List<CurrencyRequest> findByRequestedTimeBetween(Pageable pageable, Instant start, Instant end);
}
