package com.example.autoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoFiltroRequestDTO {

    private Long marcaId;
    private Long modeloId;
    private Long categoriaId;
    private Double precioMin;
    private Double precioMax;
    private Double precioReferenciaActualMin;
    private Double precioReferenciaActualMax;
    private Double precioSalidaEstimadoMin;
    private Double precioSalidaEstimadoMax;
    private Integer anioMin;
    private Integer anioMax;
    private Integer caballosFuerzaMin;
    private Integer caballosFuerzaMax;
    private Integer torqueNmMin;
    private Integer torqueNmMax;
    private Integer velocidadMaximaMin;
    private Integer velocidadMaximaMax;
    private Double aceleracionCeroACienMin;
    private Double aceleracionCeroACienMax;
    private String color;
    private String motor;
    private String tipoCombustible;
    private String transmision;
    private String traccion;
    private String texto;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}
