package com.br.contas.infrastructure.persistence.specification;

import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContaSpecification {

    public static Specification<ContaEntity> dataVencimentoEquals(LocalDate dataVencimento) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dataVencimento"), dataVencimento);
    }

    public static Specification<ContaEntity> descricaoContains(String descricao) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("descricao"), "%" + descricao + "%");
    }
}
