package com.example.comparador_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutoComparadoDTO {
    private Long id;
    private String marcaNombre;
    private String modeloNombre;

    private Double precio;
    private Double precioReferenciaActual;
    private Double precioSalidaEstimado;
    private Integer anioFabricacion;
    private String color;
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
    private String categoriaNombre;
    private List<String> fortalezas;
    private List<String> alertas;

    private String imagenPortadaUrl;
}
