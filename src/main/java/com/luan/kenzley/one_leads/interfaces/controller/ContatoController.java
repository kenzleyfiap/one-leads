package com.luan.kenzley.one_leads.interfaces.controller;

import com.luan.kenzley.one_leads.application.service.ContatoService;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import com.luan.kenzley.one_leads.shared.ContatoSwaggerExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContatoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "E-mail ou Nome obrigatório", value = ContatoSwaggerExamples.EMAIL_OBRIGATORIO_EXAMPLE))),
                    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "E-mail duplicado", value = ContatoSwaggerExamples.EMAIL_DUPLICADO_EXAMPLE)))
            }
    )
    @PostMapping
    public ResponseEntity<ContatoResponseDTO> criar(
            @Valid
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
                    @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Contato não encontrado", value = ContatoSwaggerExamples.CONTATO_NAO_ENCONTRADO_EXAMPLE))),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody ContatoDTO dto) {
        service.atualizarContato(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Buscar contato por nome ou e-mail",
            description = "Retorna contatos que correspondem ao nome ou e-mail informados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contatos encontrados",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContatoResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Contato não encontrado", value = ContatoSwaggerExamples.CONTATO_NAO_ENCONTRADO_EXAMPLE))),
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<ContatoResponseDTO>> buscarContato(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email
    ) {
        return ResponseEntity.ok(service.buscarContato(nome, email));
    }

    @DeleteMapping("/{empresaId}/contatos/{contatoId}")
    @Operation(
            summary = "Remover vínculo de contato com empresa",
            description = "Remove o vínculo entre uma empresa e um contato. Não exclui o contato.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vínculo removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa ou contato não encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Contato não encontrado", value = ContatoSwaggerExamples.CONTATO_NAO_ENCONTRADO_EXAMPLE))),
                    @ApiResponse(responseCode = "400", description = "Empresa possui apenas um contato, vínculo não pode ser removido", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Contato único", value = ContatoSwaggerExamples.CONTATO_UNICO_EXAMPLE)))
            }
    )
    public ResponseEntity<Void> desvincularContato(
            @PathVariable Long empresaId,
            @PathVariable Long contatoId
    ) {
        service.removerVinculoContato(empresaId, contatoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sem-empresa")
    @Operation(
            summary = "Listar contatos sem vínculo com empresa",
            description = "Retorna todos os contatos que não estão vinculados a nenhuma empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contatos sem vínculo",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContatoResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "contatos não encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Contato não encontrado", value = ContatoSwaggerExamples.CONTATO_NAO_ENCONTRADO_EXAMPLE))),

            }
    )
    public ResponseEntity<List<ContatoResponseDTO>> listarContatosSemEmpresa() {
        return ResponseEntity.ok(service.listarContatosSemEmpresa());
    }
}