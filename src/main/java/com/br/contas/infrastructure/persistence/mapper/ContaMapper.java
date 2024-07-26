package com.br.contas.infrastructure.persistence.mapper;

import com.br.contas.domain.model.Conta;
import com.br.contas.infrastructure.persistence.entity.ContaEntity;

public class ContaMapper {

    public static ContaEntity toEntity(Conta conta) {
        ContaEntity contaEntity = new ContaEntity();
        contaEntity.setId(conta.getId());
        contaEntity.setDataVencimento(conta.getDataVencimento());
        contaEntity.setDataPagamento(conta.getDataPagamento());
        contaEntity.setValor(conta.getValor());
        contaEntity.setValorPago(conta.getValorPago());
        contaEntity.setDescricao(conta.getDescricao());
        contaEntity.setSituacao(conta.getSituacao());
        return contaEntity;
    }

    public static Conta toDomain(ContaEntity contaEntity) {
        Conta conta = new Conta();
        conta.setId(contaEntity.getId());
        conta.setDataVencimento(contaEntity.getDataVencimento());
        conta.setDataPagamento(contaEntity.getDataPagamento());
        conta.setValor(contaEntity.getValor());
        conta.setValorPago(contaEntity.getValorPago());
        conta.setDescricao(contaEntity.getDescricao());
        conta.setSituacao(contaEntity.getSituacao());
        return conta;
    }
}
