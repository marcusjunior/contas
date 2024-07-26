package com.br.contas.api.controller;

import com.br.contas.api.controller.dto.ContaCreateDTO;
import com.br.contas.api.controller.dto.ContaDTO;
import com.br.contas.api.controller.dto.ContaUpdateDTO;
import com.br.contas.api.controller.mapper.ContaDtoMapper;
import com.br.contas.domain.model.Conta;
import com.br.contas.domain.model.Situacao;
import com.br.contas.domain.usecase.ContaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContaUseCase contaUseCase;

    @InjectMocks
    private ContaController contaController;

    @Mock
    private ContaDtoMapper contaDtoMapper;

    private ObjectMapper objectMapper;

    private Conta conta;
    private ContaDTO contaDTO;
    private ContaCreateDTO contaCreateDTO;
    private ContaUpdateDTO contaUpdateDTO;

    @BeforeEach
    void setUp() {
        PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(contaController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());
        conta.setValor(BigDecimal.valueOf(1000));
        conta.setValorPago(BigDecimal.valueOf(1000));
        conta.setDescricao("Descrição");
        conta.setSituacao(Situacao.PAGO);

        contaDTO = new ContaDTO();
        contaDTO.setId(1L);
        contaDTO.setDataVencimento(LocalDate.now());
        contaDTO.setDataPagamento(LocalDate.now());
        contaDTO.setValor(BigDecimal.valueOf(1000));
        contaDTO.setValorPago(BigDecimal.valueOf(1000));
        contaDTO.setDescricao("Descrição");
        contaDTO.setSituacao(Situacao.PAGO);

        contaCreateDTO = new ContaCreateDTO();
        contaCreateDTO.setDataVencimento(LocalDate.now());
        contaCreateDTO.setDataPagamento(LocalDate.now());
        contaCreateDTO.setValor(BigDecimal.valueOf(1000));
        contaCreateDTO.setValorPago(BigDecimal.valueOf(1000));
        contaCreateDTO.setDescricao("Descrição");
        contaCreateDTO.setSituacao(Situacao.PAGO);

        contaUpdateDTO = new ContaUpdateDTO();
        contaUpdateDTO.setDataVencimento(LocalDate.now());
        contaUpdateDTO.setDataPagamento(LocalDate.now());
        contaUpdateDTO.setValor(BigDecimal.valueOf(1000));
        contaUpdateDTO.setValorPago(BigDecimal.valueOf(1000));
        contaUpdateDTO.setDescricao("Descrição");
        contaUpdateDTO.setSituacao(Situacao.PAGO);
    }

    @Test
    void createConta() throws Exception {
        when(contaUseCase.save(any(Conta.class))).thenReturn(conta);

        mockMvc.perform(post("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void updateConta() throws Exception {
        when(contaUseCase.findById(any(Long.class))).thenReturn(Optional.of(conta));
        when(contaUseCase.save(any(Conta.class))).thenReturn(conta);

        mockMvc.perform(put("/api/contas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void updateSituacao() throws Exception {
        when(contaUseCase.findById(any(Long.class))).thenReturn(Optional.of(conta));
        when(contaUseCase.save(any(Conta.class))).thenReturn(conta);
        //when(contaDtoMapper.toDto(any(Conta.class))).thenReturn(contaDTO);

        mockMvc.perform(patch("/api/contas/{id}/situacao", 1L)
                        .param("situacao", "PAGO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void getContaById() throws Exception {
        when(contaUseCase.findById(any(Long.class))).thenReturn(Optional.of(conta));

        mockMvc.perform(get("/api/contas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaDTO.getId()));
    }

    @Test
    void getTotalPago() throws Exception {
        BigDecimal totalPago = BigDecimal.valueOf(1000);
        when(contaUseCase.findTotalPaidByPeriod(any(LocalDate.class), any(LocalDate.class))).thenReturn(totalPago);

        mockMvc.perform(get("/api/contas/total-pago")
                        .param("startDate", LocalDate.now().minusDays(30).toString())
                        .param("endDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(totalPago.toString()));
    }

    @Test
    void getContas() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> contasPage = new PageImpl<>(Collections.singletonList(conta), pageable, 1);

        when(contaUseCase.findAll(any(Pageable.class), any(LocalDate.class), anyString())).thenReturn(contasPage);

        mockMvc.perform(get("/api/contas")
                        .param("page", "0")
                        .param("size", "10")
                        .param("dataVencimento", "2024-07-23")
                        .param("descricao", "some description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(contaDTO.getId()));
    }

    @Test
    void deleteConta_Success() throws Exception {
        Long contaId = 1L;
        when(contaUseCase.findById(contaId)).thenReturn(Optional.of(new Conta()));

        mockMvc.perform(delete("/api/contas/{id}", contaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteConta_NotFound() throws Exception {
        Long contaId = 1L;
        when(contaUseCase.findById(contaId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/contas/{id}", contaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void importContas_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "contas.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "dataVencimento,dataPagamento,valor,valorPago,descricao,situacao\n2024-07-24,2024-07-24,100,100,descricao,PENDENTE".getBytes()
        );

        mockMvc.perform(multipart("/api/contas/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void importContas_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "contas.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "dataVencimento,dataPagamento,valor,valorPago,descricao,situacao\n2024-07-24,2024-07-24,100,100,descricao,PENDENTE".getBytes()
        );

        doThrow(IOException.class).when(contaUseCase).importContas(any());

        mockMvc.perform(multipart("/api/contas/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }
}
