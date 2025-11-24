package com.example.autofinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoDTO {
    private Long id;

    private String color;
    private Double precio;
    private Integer anioFabricacion;

    private Long marcaId;
    private String marcaNombre;

    private Long modeloId;
    private String modeloNombre;

    private Long categoriaId;
    private String categoriaNombre;
}
