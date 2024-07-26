package com.br.contas.api.controller;

import com.br.contas.api.controller.dto.ContaCreateDTO;
import com.br.contas.api.controller.dto.ContaDTO;
import com.br.contas.api.controller.dto.ContaUpdateDTO;
import com.br.contas.api.controller.mapper.ContaDtoMapper;
import com.br.contas.domain.model.Conta;
import com.br.contas.domain.model.Situacao;
import com.br.contas.domain.usecase.ContaUseCase;
import com.opencsv.exceptions.CsvValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaUseCase contaUseCase;

    @Operation(summary = "Cria uma nova conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta criada com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContaCreateDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ContaDTO> createConta(@RequestBody @Valid ContaCreateDTO contaDTO) {
        Conta conta = ContaDtoMapper.toDomain(contaDTO);
        Conta savedConta = contaUseCase.save(conta);
        ContaDTO savedContaDTO = ContaDtoMapper.toDto(savedConta);
        return ResponseEntity.ok(savedContaDTO);
    }

    @Operation(summary = "Atualiza uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContaDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContaDTO> updateConta(@PathVariable Long id, @RequestBody ContaUpdateDTO contaDTO) {
        Optional<Conta> existingConta = contaUseCase.findById(id);
        if (existingConta.isPresent()) {
            Conta conta = ContaDtoMapper.toDomain(contaDTO);
            conta.setId(id);
            Conta updatedConta = contaUseCase.save(conta);
            ContaDTO updatedContaDTO = ContaDtoMapper.toDto(updatedConta);
            return ResponseEntity.ok(updatedContaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza a situação de uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Situação da conta atualizada com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContaDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    @PatchMapping("/{id}/situacao")
    public ResponseEntity<ContaDTO> updateSituacao(@PathVariable Long id, @RequestParam Situacao situacao) {
        Optional<Conta> existingConta = contaUseCase.findById(id);
        if (existingConta.isPresent()) {
            Conta conta = existingConta.get();
            conta.setSituacao(situacao);
            Conta updatedConta = contaUseCase.save(conta);
            ContaDTO updatedContaDTO = ContaDtoMapper.toDto(updatedConta);
            return ResponseEntity.ok(updatedContaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtém uma lista paginada de contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)) })
    })
    @GetMapping
    public ResponseEntity<Page<ContaDTO>> getContas(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(required = false) String descricao) {
        Page<Conta> contas = contaUseCase.findAll(pageable, dataVencimento, descricao);
        Page<ContaDTO> contaDTOs = contas.map(ContaDtoMapper::toDto);
        return ResponseEntity.ok(contaDTOs);
    }

    @Operation(summary = "Obtém uma conta pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta retornada com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContaDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> getContaById(@PathVariable Long id) {
        Optional<Conta> conta = contaUseCase.findById(id);
        return conta.map(c -> ResponseEntity.ok(ContaDtoMapper.toDto(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtém o valor total pago em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total pago retornado com sucesso",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class)) })
    })
    @GetMapping("/total-pago")
    public ResponseEntity<BigDecimal> getTotalPago(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        BigDecimal totalPago = contaUseCase.findTotalPaidByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalPago);
    }

    @Operation(summary = "Deleta uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        Optional<Conta> conta = contaUseCase.findById(id);
        if (conta.isPresent()) {
            contaUseCase.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Importa contas de um arquivo CSV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contas importadas com sucesso", content = @Content)
    })
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<Void> importContas(
            @Parameter(description = "Arquivo CSV contendo as contas", required = true, content = @Content(mediaType = "multipart/form-data"))
            @RequestParam("file") MultipartFile file) {
        try {
            contaUseCase.importContas(file);
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }
}
