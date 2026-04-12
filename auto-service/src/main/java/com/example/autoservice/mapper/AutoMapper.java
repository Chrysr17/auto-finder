package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoCreateRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.dto.AutoUpdateRequestDTO;
import com.example.autoservice.model.Auto;
import com.example.autoservice.projection.AutoListadoProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "precio", target = "precio")
    @Mapping(source = "precioReferenciaActual", target = "precioReferenciaActual")
    @Mapping(source = "precioSalidaEstimado", target = "precioSalidaEstimado")
    @Mapping(source = "anioFabricacion", target = "anioFabricacion")
    @Mapping(source = "motor", target = "motor")
    @Mapping(source = "cilindradaCc", target = "cilindradaCc")
    @Mapping(source = "caballosFuerza", target = "caballosFuerza")
    @Mapping(source = "torqueNm", target = "torqueNm")
    @Mapping(source = "consumoCiudad", target = "consumoCiudad")
    @Mapping(source = "consumoCarretera", target = "consumoCarretera")
    @Mapping(source = "velocidadMaxima", target = "velocidadMaxima")
    @Mapping(source = "aceleracionCeroACien", target = "aceleracionCeroACien")
    @Mapping(source = "tipoCombustible", target = "tipoCombustible")
    @Mapping(source = "transmision", target = "transmision")
    @Mapping(source = "traccion", target = "traccion")
    @Mapping(source = "pesoKg", target = "pesoKg")
    @Mapping(source = "puertas", target = "puertas")
    @Mapping(source = "moneda", target = "moneda")
    @Mapping(source = "descripcionValor", target = "descripcionValor")
    @Mapping(source = "resumen", target = "resumen")

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
    Auto toEntity(AutoCreateRequestDTO dto);

    @Mapping(source = "marcaId", target = "marca.id")
    @Mapping(source = "modeloId", target = "modelo.id")
    @Mapping(source = "categoriaId", target = "categoria.id")
    Auto toEntity(AutoUpdateRequestDTO dto);

    @Mapping(source = "anioFabricacion", target = "anioFabricacion")
    @Mapping(source = "imagenPortadaUrl", target = "imagenPortadaUrl")
    AutoResponseDTO toResponseDTO(AutoListadoProjection p);


}
