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
public class AutoCreateRequestDTO {

    @NotBlank
    private String color;

    @NotNull
    @Positive
    private Double precio;

    @Positive
    private Double precioReferenciaActual;

    @Positive
    private Double precioSalidaEstimado;

    @NotNull
    @Min(1886)
    private Integer anioFabricacion;

    private String motor;

    @Positive
    private Integer cilindradaCc;

    @Positive
    private Integer caballosFuerza;

    @Positive
    private Integer torqueNm;

    @Positive
    private Double consumoCiudad;

    @Positive
    private Double consumoCarretera;

    @Positive
    private Integer velocidadMaxima;

    @Positive
    private Double aceleracionCeroACien;

    private String tipoCombustible;
    private String transmision;
    private String traccion;

    @Positive
    private Integer pesoKg;

    @Positive
    private Integer puertas;

    private String moneda;
    private String descripcionValor;
    private String resumen;

    @NotNull
    private Long marcaId;

    @NotNull
    private Long modeloId;

    @NotNull
    private Long categoriaId;
}
