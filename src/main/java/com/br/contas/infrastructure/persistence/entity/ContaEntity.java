package com.br.contas.infrastructure.persistence.entity;

import com.br.contas.domain.model.Situacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "conta")
@Data
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private BigDecimal valorPago;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;
}
