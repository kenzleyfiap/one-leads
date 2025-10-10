package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.application.service.EmpresaService;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Operações relacionadas a empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNPJ mal formatado"),
                    @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
            }
    )
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criar(@Valid @RequestBody EmpresaDTO dto) {
        return ResponseEntity.ok(service.criarEmpresa(dto));
    }

    @Operation(
            summary = "Atualizar empresa",
            description = "Atualiza nome e CNPJ da empresa. Contatos não são alterados neste endpoint.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateDTO dto
    ) {
        service.atualizarEmpresa(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Buscar empresa por nome ou CNPJ",
            description = "Retorna empresas que correspondem ao nome ou CNPJ informados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de empresas encontradas",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmpresaResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros de busca ausentes")
            }
    )


    @GetMapping("/buscar")
    public ResponseEntity<List<EmpresaResponseDTO>> buscarEmpresa(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnpj
    ) {
        return ResponseEntity.ok(service.buscarEmpresa(nome, cnpj));
    }

    @Operation(
            summary = "Listar empresas com primeiro contato",
            description = "Retorna todas as empresas, incluindo os dados do primeiro contato vinculado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de empresas com resumo de contato",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmpresaResumoDTO.class))))
            }
    )
    @GetMapping
    public ResponseEntity<List<EmpresaResumoDTO>> listarEmpresas() {
        return ResponseEntity.ok(service.listarEmpresasComPrimeiroContato());
    }

    @Operation(
            summary = "Listar contatos de uma empresa",
            description = "Retorna todos os contatos vinculados à empresa informada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contatos da empresa",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContatoResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
            }
    )

    @GetMapping("/{id}/contatos")
    public ResponseEntity<List<ContatoResponseDTO>> listarContatosPorEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarContatosPorEmpresa(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir empresa",
            description = "Exclui uma empresa pelo ID. Os contatos vinculados não são excluídos.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Empresa excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
            }
    )
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        service.excluirEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Vincular contato a uma empresa",
            description = """
        Realiza o vínculo entre uma empresa existente e um contato existente.
        Ambos devem estar previamente cadastrados. O vínculo é bidirecional e persistido.
        """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "IDs da empresa e do contato a serem vinculados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VinculoContatoEmpresaDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo realizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa ou contato não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Contato já está vinculado à empresa")
            }
    )
    @PostMapping("/vincular-contato")
    public ResponseEntity<Void> vincularContato(@RequestBody VinculoContatoEmpresaDTO dto) {
        service.vincularContato(dto.empresaId(), dto.contatoId());
        return ResponseEntity.noContent().build();
    }
}
