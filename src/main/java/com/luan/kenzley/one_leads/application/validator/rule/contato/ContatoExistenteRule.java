package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.domain.exception.ContatoError;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContatoExistenteRule implements ContatoUpdateValidationRule {

    @Autowired
    private ContatoRepository contatoRepo;

    @Override
    public void validate(Long id, ContatoDTO dto) {
        if (!contatoRepo.existsById(id)) {
            throw new BusinessException(ContatoError.CONTATO_NAO_ENCONTRADO);
        }
    }
}

