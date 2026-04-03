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
    private Integer anioMin;
    private Integer anioMax;
    private String color;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}
