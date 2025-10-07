package com.luan.kenzley.one_leads.application.validator.rule.contato;

import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.dto.ContatoDTO;
import com.luan.kenzley.one_leads.domain.exception.BusinessException;
import com.luan.kenzley.one_leads.infrastructure.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNaoDuplicadoRule implements ContatoUpdateValidationRule {

    @Autowired
    private ContatoRepository contatoRepo;

    @Override
    public void validate(Long id, ContatoDTO dto) {
        Contato atual = contatoRepo.findById(id).orElseThrow();
        if (!atual.getEmail().equals(dto.email()) &&
                contatoRepo.findByEmail(dto.email()).isPresent()) {
            throw new BusinessException("EMAIL_DUPLICADO", "E-mail já cadastrado: " + dto.email());
        }
    }
}
