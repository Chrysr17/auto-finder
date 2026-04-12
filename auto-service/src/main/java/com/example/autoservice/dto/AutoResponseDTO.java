package com.example.autoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoResponseDTO {
    private Long id;

    private String color;
    private Double precio;
    private Double precioReferenciaActual;
    private Double precioSalidaEstimado;
    private Integer anioFabricacion;
    private String motor;
    private Integer cilindradaCc;
    private Integer caballosFuerza;
    private Integer torqueNm;
    private Double consumoCiudad;
    private Double consumoCarretera;
    private Integer velocidadMaxima;
    private Double aceleracionCeroACien;
    private String tipoCombustible;
    private String transmision;
    private String traccion;
    private Integer pesoKg;
    private Integer puertas;
    private String moneda;
    private String descripcionValor;
    private String resumen;

    private Long marcaId;
    private String marcaNombre;

    private Long modeloId;
    private String modeloNombre;

    private Long categoriaId;
    private String categoriaNombre;

    private String imagenPortadaUrl;

}
