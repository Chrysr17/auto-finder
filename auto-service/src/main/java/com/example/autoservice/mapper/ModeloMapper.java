package com.example.autoservice.mapper;

import com.example.autoservice.dto.ModeloDTO;
import com.example.autoservice.model.Modelo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModeloMapper {

    ModeloDTO toDTO(Modelo modelo);

    Modelo toEntity(ModeloDTO dto);
}
