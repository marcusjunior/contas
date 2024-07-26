package com.br.contas.application.service;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.model.Situacao;
import com.br.contas.domain.port.ContaRepository;
import com.br.contas.domain.usecase.ContaUseCase;
import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

@Service
public class ContaService implements ContaUseCase {

    @Autowired
    private ContaRepository contaRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    @Override
    public Page<Conta> findAll(Pageable pageable, LocalDate dataVencimento, String descricao) {
        return contaRepository.findAll(pageable, dataVencimento, descricao);
    }

    @Override
    public void importContas(MultipartFile file) throws IOException, CsvValidationException {
        List<ContaEntity> contas = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                ContaEntity conta = new ContaEntity();
                conta.setDataVencimento(LocalDate.parse(line[0], DATE_FORMATTER));
                conta.setDataPagamento(LocalDate.parse(line[1], DATE_FORMATTER));
                conta.setValor(new BigDecimal(line[2]));
                conta.setValorPago(new BigDecimal(line[3]));
                conta.setDescricao(line[4]);
                conta.setSituacao(Situacao.valueOf(line[5].toUpperCase()));
                contas.add(conta);
            }
        }
        contaRepository.saveAll(contas);
    }
}