package com.luan.kenzley.one_leads.controller;

import com.luan.kenzley.one_leads.domain.Empresa;
import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService service;

    @Operation(
            summary = "Criar nova empresa",
            description = """
        Cria uma nova empresa com nome e CNPJ obrigatórios, vinculando ao menos um contato existente.
        O CNPJ deve ser único e válido. Os contatos são referenciados por seus IDs.
        """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da empresa a ser criada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmpresaDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa criada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empresa.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNPJ mal formatado"),
                    @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado ou e-mail de contato duplicado")
            }
    )
    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody EmpresaDTO dto) {
        Empresa empresa = service.criarEmpresa(dto);
        return ResponseEntity.ok(empresa);
    }

    @Operation(
            summary = "Atualizar empresa",
            description = "Atualiza nome e CNPJ da empresa. Contatos não são alterados neste endpoint.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empresa.class))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizarEmpresa(
            @PathVariable Long id,
            @RequestBody EmpresaUpdateDTO dto
    ) {
        Empresa atualizada = service.atualizarEmpresa(id, dto);
        return ResponseEntity.ok(atualizada);
    }

}
