package com.br.contas.api.controller.dto;

import com.br.contas.domain.model.Situacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContaDTO {
    private Long id;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private BigDecimal valorPago;
    private String descricao;
    private Situacao situacao;
}
