package com.br.contas.infrastructure.persistence.repository;

import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SpringDataContaRepository extends JpaRepository<ContaEntity, Long> {

    @Query("SELECT SUM(c.valorPago) FROM ContaEntity c WHERE c.dataPagamento BETWEEN :startDate AND :endDate")
    BigDecimal findTotalPaidByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
