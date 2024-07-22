package com.br.contas.infrastructure.repository;

import com.br.contas.domain.model.Conta;
import com.br.contas.infrastructure.persistence.entity.ContaEntity;
import com.br.contas.infrastructure.persistence.repository.JpaContaRepository;
import com.br.contas.infrastructure.persistence.repository.SpringDataContaRepository;
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
class JpaContaRepositoryTest {

    @InjectMocks
    private JpaContaRepository jpaContaRepository;

    @Mock
    private SpringDataContaRepository springDataContaRepository;

    private Conta conta;
    private ContaEntity contaEntity;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setId(1L);
        contaEntity = new ContaEntity();
        contaEntity.setId(1L);
    }

    @Test
    void save() {
        when(springDataContaRepository.save(any(ContaEntity.class))).thenReturn(contaEntity);

        Conta result = jpaContaRepository.save(conta);

        assertEquals(conta.getId(), result.getId());
        verify(springDataContaRepository, times(1)).save(any(ContaEntity.class));
    }

    @Test
    void findById() {
        when(springDataContaRepository.findById(any(Long.class))).thenReturn(Optional.of(contaEntity));

        Optional<Conta> result = jpaContaRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(conta.getId(), result.get().getId());
        verify(springDataContaRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void deleteById() {
        doNothing().when(springDataContaRepository).deleteById(any(Long.class));

        jpaContaRepository.deleteById(1L);

        verify(springDataContaRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void findTotalPaidByPeriod() {
        BigDecimal totalPaid = BigDecimal.valueOf(1000);
        when(springDataContaRepository.findTotalPaidByPeriod(any(LocalDate.class), any(LocalDate.class))).thenReturn(totalPaid);

        BigDecimal result = jpaContaRepository.findTotalPaidByPeriod(LocalDate.now().minusDays(30), LocalDate.now());

        assertEquals(totalPaid, result);
        verify(springDataContaRepository, times(1)).findTotalPaidByPeriod(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContaEntity> contaEntities = new PageImpl<>(Collections.singletonList(contaEntity));
        when(springDataContaRepository.findAll(any(Pageable.class))).thenReturn(contaEntities);

        Page<Conta> result = jpaContaRepository.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(conta.getId(), result.getContent().get(0).getId());
        verify(springDataContaRepository, times(1)).findAll(any(Pageable.class));
    }
}
