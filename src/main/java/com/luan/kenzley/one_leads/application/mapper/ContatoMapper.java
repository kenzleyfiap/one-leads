package com.luan.kenzley.one_leads.application.mapper;

import com.luan.kenzley.one_leads.domain.model.Contato;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoDTO;
import com.luan.kenzley.one_leads.interfaces.dto.contato.ContatoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ContatoMapper {

    private final ModelMapper modelMapper;

    public ContatoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Contato toEntity(ContatoDTO dto) {
        return modelMapper.map(dto, Contato.class);
    }

    public ContatoResponseDTO toResponse(Contato entity) {
        return modelMapper.map(entity, ContatoResponseDTO.class);
    }

    public ContatoDTO toDTO(Contato entity) {
        return modelMapper.map(entity, ContatoDTO.class);
    }
}