package com.br.contas.application.service;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.port.ContaRepository;
import com.br.contas.domain.usecase.ContaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ContaService implements ContaUseCase {

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public Conta save(Conta conta) {
        return contaRepository.save(conta);
    }

    @Override
    public Optional<Conta> findById(Long id) {
        return contaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        contaRepository.deleteById(id);
    }

    @Override
    public BigDecimal findTotalPaidByPeriod(LocalDate startDate, LocalDate endDate) {
        return contaRepository.findTotalPaidByPeriod(startDate, endDate);
    }


    public Page<Conta> findAll(Pageable pageable) {
        return contaRepository.findAll(pageable);
    }
}