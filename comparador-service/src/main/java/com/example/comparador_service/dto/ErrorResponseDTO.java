package com.example.comparador_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta estándar de error del comparador.")
public class ErrorResponseDTO {

    @Schema(description = "Fecha y hora en que se generó el error.", example = "2026-04-19T10:15:30")
    private LocalDateTime timestamp;

    @Schema(description = "Código HTTP del error.", example = "400")
    private Integer status;

    @Schema(description = "Título corto del error.", example = "Solicitud invalida")
    private String error;

    @Schema(description = "Detalle legible del problema.", example = "Debe enviar al menos dos autos para comparar")
    private String message;
}
