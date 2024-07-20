package com.br.contas.infrastructure.persistence.repository;

import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataContaRepository extends JpaRepository<ContaEntity, Long> {
}
