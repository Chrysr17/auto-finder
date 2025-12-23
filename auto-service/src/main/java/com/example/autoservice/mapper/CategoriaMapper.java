package com.example.autoservice.mapper;

import com.example.autoservice.dto.CategoriaRequestDTO;
import com.example.autoservice.dto.CategoriaResponseDTO;
import com.example.autoservice.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "nombre", target = "nombre")
//    @Mapping(source = "descripcion", target = "descripcion")
    CategoriaResponseDTO toResponseDTO(Categoria categoria);

//    @Mapping(source = "nombre", target = "nombre")
//    @Mapping(source = "descripcion", target = "descripcion")
    Categoria toEntity(CategoriaRequestDTO dto);
}
