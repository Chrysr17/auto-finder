package com.example.comparador_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Ficha resumida del auto dentro de una comparación, incluyendo highlights y alertas.")
public class AutoComparadoDTO {
    @Schema(description = "Id del auto comparado.", example = "1")
    private Long id;

    @Schema(description = "Marca del auto.", example = "Toyota")
    private String marcaNombre;

    @Schema(description = "Modelo del auto.", example = "Supra")
    private String modeloNombre;

    @Schema(description = "Precio de lista o referencia base del auto.", example = "55000.0")
    private Double precio;

    @Schema(description = "Precio de referencia actual estimado.", example = "62000.0")
    private Double precioReferenciaActual;

    @Schema(description = "Precio histórico o de salida estimado.", example = "58000.0")
    private Double precioSalidaEstimado;

    @Schema(description = "Año de fabricación del auto.", example = "2021")
    private Integer anioFabricacion;

    @Schema(description = "Color principal del auto.", example = "Rojo")
    private String color;

    @Schema(description = "Descripción corta del motor.", example = "3.0 Turbo")
    private String motor;

    @Schema(description = "Cilindrada en centímetros cúbicos.", example = "3000")
    private Integer cilindradaCc;

    @Schema(description = "Potencia del auto en caballos de fuerza.", example = "382")
    private Integer caballosFuerza;

    @Schema(description = "Torque del auto en Newton metro.", example = "500")
    private Integer torqueNm;

    @Schema(description = "Consumo estimado en ciudad.", example = "13.0")
    private Double consumoCiudad;

    @Schema(description = "Consumo estimado en carretera.", example = "9.0")
    private Double consumoCarretera;

    @Schema(description = "Velocidad máxima estimada.", example = "250")
    private Integer velocidadMaxima;

    @Schema(description = "Aceleración de 0 a 100 km/h en segundos.", example = "4.1")
    private Double aceleracionCeroACien;

    @Schema(description = "Tipo de combustible del auto.", example = "Gasolina")
    private String tipoCombustible;

    @Schema(description = "Tipo de transmisión.", example = "Automatica")
    private String transmision;

    @Schema(description = "Configuración de tracción.", example = "Trasera")
    private String traccion;

    @Schema(description = "Peso del auto en kilogramos.", example = "1540")
    private Integer pesoKg;

    @Schema(description = "Cantidad de puertas.", example = "2")
    private Integer puertas;

    @Schema(description = "Moneda de los valores monetarios.", example = "USD")
    private String moneda;

    @Schema(description = "Texto corto para contextualizar el valor del auto.",
            example = "El valor actual refleja alta demanda en unidades originales.")
    private String descripcionValor;

    @Schema(description = "Resumen general del posicionamiento del auto.", example = "Deportivo equilibrado")
    private String resumen;

    @Schema(description = "Categoría comercial o de segmento.", example = "Coupe")
    private String categoriaNombre;

    @ArraySchema(schema = @Schema(implementation = String.class),
            arraySchema = @Schema(description = "Fortalezas detectadas automáticamente para este auto."))
    private List<String> fortalezas;

    @ArraySchema(schema = @Schema(implementation = String.class),
            arraySchema = @Schema(description = "Alertas o consideraciones detectadas automáticamente para este auto."))
    private List<String> alertas;

    @Schema(description = "URL de la imagen de portada del auto.", example = "https://cdn.example.com/autos/1.jpg")
    private String imagenPortadaUrl;
}
