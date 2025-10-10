package com.luan.kenzley.one_leads.interfaces.dto.empresa;

import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

public class EmpresaResponseDTO {

    @Schema(description = "Identificação da empresa", example = "1")
    private Long id;

    @Schema(description = "Cnpj da empresa", example = "20496329000100")
    private String cnpj;

    @Schema(description = "Nome da empresa", example = "Empresa LTDA")
    private String nome;

    @ArraySchema(
            arraySchema = @Schema(description = "Lista de contatos vinculados à empresa"),
            schema = @Schema(implementation = ContatoResponseDTO.class)
    )
    private Set<ContatoResponseDTO> contatos = new HashSet<>();

    public EmpresaResponseDTO() {}

    public EmpresaResponseDTO(long id, String cnpj, String nome) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<ContatoResponseDTO> getContatos() {
        return contatos;
    }

    public void setContatos(Set<ContatoResponseDTO> contatos) {
        this.contatos = contatos;
    }
}