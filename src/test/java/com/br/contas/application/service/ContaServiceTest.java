package com.br.contas.application.service;

import com.br.contas.domain.model.Conta;
import com.br.contas.domain.port.ContaRepository;
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

    @Test
    void save() {
        Conta conta = new Conta();
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.save(conta);

        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void findById() {
        Conta conta = new Conta();
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
        Conta conta = new Conta();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Collections.singletonList(conta));
        when(contaRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Conta> result = contaService.findAll(pageable);

        assertEquals(page, result);
        verify(contaRepository, times(1)).findAll(pageable);
    }
}
