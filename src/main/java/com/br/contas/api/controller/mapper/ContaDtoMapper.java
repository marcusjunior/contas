package com.br.contas.api.controller.mapper;

import com.br.contas.api.controller.dto.ContaDTO;
import com.br.contas.domain.model.Conta;

public class ContaDtoMapper {

    public static ContaDTO toDto(Conta conta) {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setId(conta.getId());
        contaDTO.setDataVencimento(conta.getDataVencimento());
        contaDTO.setDataPagamento(conta.getDataPagamento());
        contaDTO.setValor(conta.getValor());
        contaDTO.setDescricao(conta.getDescricao());
        contaDTO.setSituacao(conta.getSituacao());
        return contaDTO;
    }

    public static Conta toDomain(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setId(contaDTO.getId());
        conta.setDataVencimento(contaDTO.getDataVencimento());
        conta.setDataPagamento(contaDTO.getDataPagamento());
        conta.setValor(contaDTO.getValor());
        conta.setDescricao(contaDTO.getDescricao());
        conta.setSituacao(contaDTO.getSituacao());
        return conta;
    }
}
