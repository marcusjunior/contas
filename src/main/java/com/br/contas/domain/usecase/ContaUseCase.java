package com.br.contas.domain.usecase;

import com.br.contas.domain.model.Conta;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ContaUseCase {
    Conta save(Conta conta);
    Optional<Conta> findById(Long id);
    void deleteById(Long id);
    BigDecimal findTotalPaidByPeriod(LocalDate startDate, LocalDate endDate);
    Page<Conta> findAll(Pageable pageable, LocalDate dataVencimento, String descricao);
    void importContas(MultipartFile file) throws IOException, CsvValidationException;
}
