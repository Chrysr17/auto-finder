package com.example.autofinder.mapper;

import com.example.autofinder.dto.AutoDTO;
import com.example.autofinder.model.Auto;
import com.example.autofinder.model.Categoria;
import com.example.autofinder.model.Marca;
import com.example.autofinder.model.Modelo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoMapper {

    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "modelo.id", target = "modeloId")
    @Mapping(source = "categoria.id", target = "categoriaId")
    AutoDTO toDTO(Auto auto);

    @Mapping(source = "marcaId", target = "marca.id")
    @Mapping(source = "modeloId", target = "modelo.id")
    @Mapping(source = "categoriaId", target = "categoria.id")
    Auto toEntity(AutoDTO dto);

    // Crear objetos vacíos para permitir mapear IDs
    default Marca mapMarca(Long id) {
        return id == null ? null : Marca.builder().id(id).build();
    }

    default Modelo mapModelo(Long id) {
        return id == null ? null : Modelo.builder().id(id).build();
    }

    default Categoria mapCategoria(Long id) {
        return id == null ? null : Categoria.builder().id(id).build();
    }
}
