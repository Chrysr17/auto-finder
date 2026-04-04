package com.example.autoservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoRequestDTO {

    @NotBlank
    private String color;

    @NotNull
    @Positive
    private Double precio;

    @NotNull
    @Min(1886)
    private Integer anioFabricacion;

    @NotNull
    private Long marcaId;

    @NotNull
    private Long modeloId;

    @NotNull
    private Long categoriaId;
}
