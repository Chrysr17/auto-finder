package com.example.autoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoRequestDTO {

    private String color;
    private Double precio;
    private Integer anioFabricacion;

    private Long marcaId;
    private Long modeloId;
    private Long categoriaId;
}
