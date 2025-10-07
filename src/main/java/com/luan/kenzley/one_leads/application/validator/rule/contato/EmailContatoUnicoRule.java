package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailContatoUnicoRule implements ContatoValidationRule {

    @Autowired
    private ContatoRepository contatoRepo;

    @Override
    public void validate(ContatoDTO dto) {
        if (contatoRepo.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("E-mail %s duplicado", dto.email());
        }
    }
}
