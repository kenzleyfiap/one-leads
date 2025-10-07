package com.luan.kenzley.one_leads.controller;

import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.application.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contatos")
@Tag(name = "Contatos", description = "Operações relacionadas a contatos")
public class ContatoController {

    @Autowired
    private ContatoService service;

    @Operation(
            summary = "Criar novo contato",
            description = "Cria um novo contato com nome, e-mail, telefone e observações. Nome e e-mail são obrigatórios.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contato.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos obrigatórios ausentes"),
                    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
            }
    )
    @PostMapping
    public ResponseEntity<Contato> criar(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do contato a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContatoDTO.class))
            ) ContatoDTO dto
    ) {
        return ResponseEntity.ok(service.criarContato(dto));
    }

    @Operation(
            summary = "Atualizar contato",
            description = "Atualiza todos os campos de um contato existente. O e-mail deve continuar único.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contato.class))),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
                    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado por outro contato")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Contato> atualizar(@PathVariable Long id, @RequestBody ContatoDTO dto) {
        return ResponseEntity.ok(service.atualizarContato(id, dto));
    }
}