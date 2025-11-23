package com.example.autofinder.dto;

import com.example.autofinder.model.Auto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    AutoMapper INSTANCE = Mappers.getMapper(AutoMapper.class);

    // ENTITY → DTO
    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target = "marcaNombre")
    @Mapping(source = "modelo.id", target = "modeloId")
    @Mapping(source = "modelo.nombre", target = "modeloNombre")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    AutoDto toDTO(Auto auto);

    // DTO → ENTITY (solo para registrar o actualizar)
    @Mapping(target = "marca.id", source = "marcaId")
    @Mapping(target = "modelo.id", source = "modeloId")
    @Mapping(target = "categoria.id", source = "categoriaId")
    Auto toEntity(AutoDto autoDTO);
}
