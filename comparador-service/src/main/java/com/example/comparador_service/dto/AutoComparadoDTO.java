package com.example.comparador_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoComparadoDTO {
    private Long id;
    private String marcaNombre;
    private String modeloNombre;

    private Double precio;
    private Integer anioFabricacion;
    private String color;
    private String categoriaNombre;
}
