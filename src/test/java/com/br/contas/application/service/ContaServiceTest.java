package com.br.contas.application.service;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.model.Situacao;
import com.br.contas.domain.port.ContaRepository;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());
        conta.setValor(BigDecimal.valueOf(100));
        conta.setValorPago(BigDecimal.valueOf(100));
        conta.setDescricao("Descrição de teste");
        conta.setSituacao(Situacao.PAGO);
    }

    @Test
    void save() {
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.save(conta);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void findById() {
        when(contaRepository.findById(any(Long.class))).thenReturn(Optional.of(conta));

        Optional<Conta> result = contaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(conta, result.get());
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        doNothing().when(contaRepository).deleteById(any(Long.class));

        contaService.deleteById(1L);

        verify(contaRepository, times(1)).deleteById(1L);
    }

    @Test
    void findTotalPaidByPeriod() {
        BigDecimal totalPaid = BigDecimal.valueOf(1000);
        when(contaRepository.findTotalPaidByPeriod(any(LocalDate.class), any(LocalDate.class))).thenReturn(totalPaid);

        BigDecimal result = contaService.findTotalPaidByPeriod(LocalDate.now().minusDays(30), LocalDate.now());

        assertEquals(totalPaid, result);
        verify(contaRepository, times(1)).findTotalPaidByPeriod(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Collections.singletonList(conta));
        when(contaRepository.findAll(any(Pageable.class), any(LocalDate.class), any(String.class))).thenReturn(page);

        Page<Conta> result = contaService.findAll(pageable, LocalDate.now(), "descrição");

        assertEquals(page, result);
        verify(contaRepository, times(1)).findAll(any(Pageable.class), any(LocalDate.class), any(String.class));
    }

    @Test
    void importContas() throws IOException, CsvValidationException {
        String csvContent = "dataVencimento,dataPagamento,valor,valorPago,descricao,situacao\n" +
                "2024-07-24,2024-07-24,100,100,Teste de importação,PAGO\n";
        MultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", new ByteArrayInputStream(csvContent.getBytes()));

        doNothing().when(contaRepository).saveAll(anyList());

        contaService.importContas(file);

        verify(contaRepository, times(1)).saveAll(anyList());
    }
}
