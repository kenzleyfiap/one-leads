package com.luan.kenzley.one_leads.validator.rule.contato;

import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.exception.BusinessException;
import com.luan.kenzley.one_leads.repository.ContatoRepository;
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
