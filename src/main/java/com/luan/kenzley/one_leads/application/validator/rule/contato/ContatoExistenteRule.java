package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContatoExistenteRule implements ContatoUpdateValidationRule {

    @Autowired
    private ContatoRepository contatoRepo;

    @Override
    public void validate(Long id, ContatoDTO dto) {
        if (!contatoRepo.existsById(id)) {
            throw new BusinessException("CONTATO_NAO_ENCONTRADO", "Contato com ID " + id + " não encontrado");
        }
    }
}

