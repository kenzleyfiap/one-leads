package com.luan.kenzley.one_leads.utils;

import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;

import java.util.HashSet;

public class ContatoHelper {

    public static ContatoDTO criarContatoDTO() {
        return new ContatoDTO("Contato Teste", "contato@mail.com", "", "");
    }


    public static Contato criarContatoEntity() {
        Contato contato = new Contato();
        contato.setNome("João da Silva");
        contato.setEmail("joao@email.com");
        contato.setTelefone("61999999999");
        contato.setObservacoes("Contato de teste");
        contato.setEmpresas(new HashSet<>());
        return contato;
    }

    public static ContatoResponseDTO criarContatoResponseDTO() {
        return new ContatoResponseDTO(1L, "Contato Teste", "contato@mail.com","","");
    }

}
