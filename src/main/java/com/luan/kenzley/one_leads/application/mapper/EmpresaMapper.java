package com.luan.kenzley.one_leads.application.mapper;

import com.luan.kenzley.one_leads.domain.model.Empresa;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaDTO;
import com.luan.kenzley.one_leads.interfaces.dto.empresa.EmpresaResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    private final ModelMapper modelMapper;

    public EmpresaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Empresa toEntity(EmpresaDTO dto) {
        return modelMapper.map(dto, Empresa.class);
    }

    public EmpresaResponseDTO toResponse(Empresa entity) {
        return modelMapper.map(entity, EmpresaResponseDTO.class);
    }

    public EmpresaDTO toDTO(Empresa entity) {
        return modelMapper.map(entity, EmpresaDTO.class);
    }
}