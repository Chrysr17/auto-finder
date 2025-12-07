package com.example.comparador_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
