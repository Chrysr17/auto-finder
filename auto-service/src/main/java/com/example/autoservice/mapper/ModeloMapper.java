package com.example.autoservice.mapper;

import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.model.Modelo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModeloMapper {

    ModeloResponseDTO toDTO(Modelo modelo);

    Modelo toEntity(ModeloResponseDTO dto);
}
