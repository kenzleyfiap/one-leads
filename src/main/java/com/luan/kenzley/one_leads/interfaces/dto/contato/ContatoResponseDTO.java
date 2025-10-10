package com.luan.kenzley.one_leads.interfaces.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;

public class ContatoResponseDTO {

        @Schema(description = "Identificação do contato", example = "1")
        private Long id;

        @Schema(description = "Nome completo do contato", example = "Maria Silva")
        private String nome;

        @Schema(description = "E-mail do contato", example = "maria.silva@email.com")
        private String email;

        @Schema(description = "Telefone do contato", example = "+55 61 91234-5678")
        private String telefone;

        @Schema(description = "Observações adicionais sobre o contato", example = "Cliente interessado em planos premium")
        private String observacoes;


        public ContatoResponseDTO(Long id, String nome, String email, String telefone, String observacoes) {
                this.id = id;
                this.nome = nome;
                this.email = email;
                this.telefone = telefone;
                this.observacoes = observacoes;
        }

        public ContatoResponseDTO() {
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getTelefone() {
                return telefone;
        }

        public void setTelefone(String telefone) {
                this.telefone = telefone;
        }

        public String getObservacoes() {
                return observacoes;
        }

        public void setObservacoes(String observacoes) {
                this.observacoes = observacoes;
        }
}