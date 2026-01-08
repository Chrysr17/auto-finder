package org.example.favoritoservice.dto;

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
    private String marcaNombre;
    private String modeloNombre;
    private String categoriaNombre;
    private Double precio;
    private Integer anioFabricacion;

}
