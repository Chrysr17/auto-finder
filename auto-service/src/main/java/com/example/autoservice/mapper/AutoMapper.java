package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoDTO;
import com.example.autoservice.model.Auto;
import com.example.autoservice.model.Categoria;
import com.example.autoservice.model.Marca;
import com.example.autoservice.model.Modelo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoMapper {

    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target = "marcaNombre")
    @Mapping(source = "modelo.id", target = "modeloId")
    @Mapping(source = "modelo.nombre", target = "modeloNombre")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    AutoDTO toDTO(Auto auto);

    @Mapping(target = "marca", expression = "java(mapMarca(dto.getMarcaId()))")
    @Mapping(target = "modelo", expression = "java(mapModelo(dto.getModeloId()))")
    @Mapping(target = "categoria", expression = "java(mapCategoria(dto.getCategoriaId()))")
    Auto toEntity(AutoDTO dto);

    // Métodos helper para crear solo IDs
    default Marca mapMarca(Long id) {
        if (id == null) return null;
        Marca m = new Marca();
        m.setId(id);
        return m;
    }

    default Modelo mapModelo(Long id) {
        if (id == null) return null;
        Modelo m = new Modelo();
        m.setId(id);
        return m;
    }

    default Categoria mapCategoria(Long id) {
        if (id == null) return null;
        Categoria c = new Categoria();
        c.setId(id);
        return c;
    }

}
