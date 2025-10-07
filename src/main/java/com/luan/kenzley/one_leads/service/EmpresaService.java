package com.luan.kenzley.one_leads.service;

import com.luan.kenzley.one_leads.domain.Contato;
import com.luan.kenzley.one_leads.domain.Empresa;
import com.luan.kenzley.one_leads.dto.EmpresaDTO;
import com.luan.kenzley.one_leads.dto.EmpresaUpdateDTO;
import com.luan.kenzley.one_leads.repository.ContatoRepository;
import com.luan.kenzley.one_leads.repository.EmpresaRepository;
import com.luan.kenzley.one_leads.validator.EmpresaUpdateValidator;
import com.luan.kenzley.one_leads.validator.EmpresaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepo;

    @Autowired
    private ContatoRepository contatoRepo;

    @Autowired
    private EmpresaValidator empresaValidator;

    @Autowired
    private EmpresaUpdateValidator updateValidator;


    public Empresa criarEmpresa(EmpresaDTO dto) {
        empresaValidator.validar(dto);

        Empresa empresa = new Empresa();
        empresa.setNome(dto.nome());
        empresa.setCnpj(dto.cnpj());

        Set<Contato> contatos = dto.contatosIds().stream()
                .map(id -> contatoRepo.findById(id).orElseThrow())
                .collect(Collectors.toSet());

        empresa.setContatos(contatos);
        return empresaRepo.save(empresa);
    }


    public Empresa atualizarEmpresa(Long id, EmpresaUpdateDTO dto) {
        updateValidator.validar(id, dto);

        Empresa empresa = empresaRepo.findById(id).orElseThrow(); // já validado
        empresa.setNome(dto.nome());
        empresa.setCnpj(dto.cnpj());

        return empresaRepo.save(empresa);
    }



}
