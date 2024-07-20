package com.br.contas.domain.port;

import com.br.contas.domain.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ContaRepository {
    Conta save(Conta conta);
    Optional<Conta> findById(Long id);
    void deleteById(Long id);
    BigDecimal findTotalPaidByPeriod(LocalDate startDate, LocalDate endDate);
    Page<Conta> findAll(Pageable pageable);
}
