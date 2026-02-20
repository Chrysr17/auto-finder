package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoDetalleResponseDTO;
import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.model.Auto;
import com.example.autoservice.model.AutoImagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

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

    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target = "marcaNombre")
    @Mapping(source = "modelo.id", target = "modeloId")
    @Mapping(source = "modelo.nombre", target = "modeloNombre")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(source = "imagenes", target = "imagenes")
    AutoDetalleResponseDTO toDetalleResponseDTO(Auto auto);

    Auto toEntity(AutoRequestDTO dto);

    @Named("obtenerPortaladaUrl")
    default String obtenerPortadaUrl(List<AutoImagen> imagenes){
        if (imagenes == null || imagenes.isEmpty()) return null;
        return  imagenes.stream()
                .filter(img -> img.getOrden() != null && img.getOrden() == 1)
                .map(AutoImagen::getUrl)
                .findFirst()
                .orElse(null);
    }

}
