package com.example.currency.repo;

import com.example.currency.entity.CurrencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRequestRepository extends JpaRepository<CurrencyRequest, Long> {
    List<CurrencyRequest> findAllByOrderByRequestedTimeDesc();
    List<CurrencyRequest> findByCurrencyCodeIgnoreCaseOrderByRequestedTimeDesc(String currencyCode);
}
