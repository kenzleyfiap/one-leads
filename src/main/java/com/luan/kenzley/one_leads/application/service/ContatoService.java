package com.luan.kenzley.one_leads.application.service;

import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.application.validator.ContatoUpdateValidator;
import com.luan.kenzley.one_leads.application.validator.ContatoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {
    @Autowired
    private ContatoRepository contatoRepo;

    @Autowired
    private ContatoValidator contatoValidator;

    @Autowired
    private ContatoUpdateValidator updateValidator;


    public Contato criarContato(ContatoDTO dto) {
        contatoValidator.validar(dto);

        Contato contato = new Contato();
        contato.setNome(dto.nome());
        contato.setEmail(dto.email());
        contato.setTelefone(dto.telefone());
        contato.setObservacoes(dto.observacoes());

        return contatoRepo.save(contato);
    }

    public Contato atualizarContato(Long id, ContatoDTO dto) {
        updateValidator.validar(id, dto);

        Contato contato = contatoRepo.findById(id).orElseThrow();
        contato.setNome(dto.nome());
        contato.setEmail(dto.email());
        contato.setTelefone(dto.telefone());
        contato.setObservacoes(dto.observacoes());

        return contatoRepo.save(contato);
    }



}
