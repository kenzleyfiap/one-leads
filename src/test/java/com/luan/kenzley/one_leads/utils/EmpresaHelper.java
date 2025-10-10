package com.luan.kenzley.one_leads.utils;

import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.*;

import java.util.HashSet;
import java.util.List;

public class EmpresaHelper {

    public static EmpresaDTO criarEmpresaDTO() {
        return new EmpresaDTO("Empresa Teste", "20496329000100", List.of(1L));
    }

    public static EmpresaDTO criarEmpresaComDoisContatosDTO() {
        return new EmpresaDTO("Empresa Dois Contatos", "16184289000186", List.of(1L, 2L));
    }

    public static Empresa criarEmpresaEntity() {
        Empresa empresa = new Empresa();
        empresa.setCnpj("20496329000100");
        empresa.setNome("Empresa Teste");
        empresa.setContatos(new HashSet<>());
        return empresa;
    }

    public static EmpresaResponseDTO criarEmpresaResponseDTO() {
        return new EmpresaResponseDTO(1L, "20496329000100", "nome");
    }

    public static EmpresaResumoDTO criarEmpresaResumoDTO() {
        return new EmpresaResumoDTO(
                1L,
                "Empresa Teste",
                "06412226000186",
                new ContatoResponseDTO(1L, "Teste", "teste@mail.com", "", ""));
    }

    public static EmpresaUpdateDTO criarEmpresaUpdateDTO() {
        return new EmpresaUpdateDTO("Empresa Teste", "20496329000100");
    }

    public static VinculoContatoEmpresaDTO criarVinculoContatoEmpresaDTO() {
        return new VinculoContatoEmpresaDTO(1L, 2L);
    }

}
