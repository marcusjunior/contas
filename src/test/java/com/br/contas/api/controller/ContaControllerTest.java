package com.br.contas.api.controller;

import com.br.contas.api.controller.dto.ContaDTO;
import com.br.contas.api.controller.mapper.ContaDtoMapper;
import com.br.contas.domain.model.Conta;
import com.br.contas.infrastructure.persistence.repository.SpringDataContaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataContaRepository springDataContaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaDtoMapper contaDtoMapper;

    private Conta conta;
    private ContaDTO contaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conta = new Conta();
        conta.setId(1L);
        contaDTO = new ContaDTO();
        contaDTO.setId(1L);
    }

    @Test
    void createConta() throws Exception {
        when(contaDtoMapper.toDomain(any(ContaDTO.class))).thenReturn(conta);
        when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(post("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void updateConta() throws Exception {
        when(contaDtoMapper.toDomain(any(ContaDTO.class))).thenReturn(conta);
        when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(put("/api/contas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void updateSituacao() throws Exception {
        when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(patch("/api/contas/{id}/situacao", 1L)
                        .param("situacao", "Nova Situacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void getContas() throws Exception {
        when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(get("/api/contas")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(contaDTO.getId()));
    }

    @Test
    void getContaById() throws Exception {
        when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(get("/api/contas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void getTotalPago() throws Exception {
        BigDecimal totalPago = BigDecimal.valueOf(1000);

        mockMvc.perform(get("/api/contas/total-pago")
                        .param("startDate", LocalDate.now().minusDays(30).toString())
                        .param("endDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(totalPago.toString()));
    }

    @Test
    void importContas() throws Exception {
        mockMvc.perform(post("/api/contas/import")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("file", "fileContent"))
                .andExpect(status().isOk());
    }
}
