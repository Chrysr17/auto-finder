package com.example.autoservice.mapper;

import com.example.autoservice.dto.CategoriaDTO;
import com.example.autoservice.model.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDTO(Categoria categoria);

    Categoria toEntity(CategoriaDTO dto);
}
