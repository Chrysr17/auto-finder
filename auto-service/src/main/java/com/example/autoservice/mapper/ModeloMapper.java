package com.example.autoservice.mapper;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.model.Modelo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ModeloMapper {

    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target = "marcaNombre")
    ModeloResponseDTO toDTO(Modelo modelo);

    Modelo toEntity(ModeloRequestDTO dto);
}
