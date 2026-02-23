package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.model.Auto;
import com.example.autoservice.projection.AutoListadoProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "precio", target = "precio")
    @Mapping(source = "anioFabricacion", target = "anioFabricacion")

    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target = "marcaNombre")

    @Mapping(source = "modelo.id", target = "modeloId")
    @Mapping(source = "modelo.nombre", target = "modeloNombre")

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    AutoResponseDTO toResponseDTO(Auto auto);

    @Mapping(source = "marcaId", target = "marca.id")
    @Mapping(source = "modeloId", target = "modelo.id")
    @Mapping(source = "categoriaId", target = "categoria.id")
    Auto toEntity(AutoRequestDTO dto);

    @Mapping(source = "anioFabricacion", target = "anioFabricacion")
    @Mapping(source = "imagenPortadaUrl", target = "imagenPortadaUrl")
    AutoResponseDTO toResponseDTO(AutoListadoProjection p);


}
