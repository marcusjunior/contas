package com.br.contas.infrastructure.persistence.repository;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.port.ContaRepository;
import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import com.br.contas.infrastructure.persistence.mapper.ContaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        // Implement the logic to calculate the total paid amount in the given period
        return null;
    }

    @Override
    public Page<Conta> findAll(Pageable pageable) {
        Page<ContaEntity> contaEntities = springDataContaRepository.findAll(pageable);
        return contaEntities.map(ContaMapper::toDomain);
    }
}