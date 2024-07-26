package com.br.contas.infrastructure.persistence.repository;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.model.Situacao;
import com.br.contas.domain.port.ContaRepository;
import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import com.br.contas.infrastructure.persistence.mapper.ContaMapper;
import com.br.contas.infrastructure.persistence.specification.ContaSpecification;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaContaRepository implements ContaRepository {

    @Autowired
    private SpringDataContaRepository springDataContaRepository;

    @Override
    public Conta save(Conta conta) {
        ContaEntity contaEntity = ContaMapper.toEntity(conta);
        ContaEntity savedEntity = springDataContaRepository.save(contaEntity);
        return ContaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Conta> findById(Long id) {
        Optional<ContaEntity> contaEntity = springDataContaRepository.findById(id);
        return contaEntity.map(ContaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        springDataContaRepository.deleteById(id);
    }

    @Override
    public BigDecimal findTotalPaidByPeriod(LocalDate startDate, LocalDate endDate) {
        return springDataContaRepository.findTotalPaidByPeriod(startDate, endDate);
    }

    @Override
    public Page<Conta> findAll(Pageable pageable, LocalDate dataVencimento, String descricao) {
        Specification<ContaEntity> spec = Specification.where(null);

        if (dataVencimento != null) {
            spec = spec.and(ContaSpecification.dataVencimentoEquals(dataVencimento));
        }

        if (descricao != null && !descricao.isEmpty()) {
            spec = spec.and(ContaSpecification.descricaoContains(descricao));
        }

        Page<ContaEntity> contaEntities = springDataContaRepository.findAll(spec, pageable);
        return contaEntities.map(ContaMapper::toDomain);
    }

    @Override
    public void saveAll(List<ContaEntity> contas) {
        springDataContaRepository.saveAll(contas);
    }
}