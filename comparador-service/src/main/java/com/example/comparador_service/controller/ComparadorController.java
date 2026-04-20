package com.example.comparador_service.controller;

import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.dto.ErrorResponseDTO;
import com.example.comparador_service.service.ComparadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comparar")
@CrossOrigin("*")
public class ComparadorController {

    private final ComparadorService comparadorService;

    public ComparadorController(ComparadorService comparadorService) {
        this.comparadorService = comparadorService;
    }

    @PostMapping
    @Operation(summary = "Compara autos por criterio simple o avanzado",
                description = "Permite comparar dos o mas autos usando criterios simples como precio, "
                    + "anioFabricacion o marca, y criterios avanzados como motor, caballosFuerza, "
                    + "rendimiento, velocidadMaxima, precioSalidaEstimado y precioReferenciaActual. "
                    + "Tambien acepta alias legados como anio, hp y precioActualAproximado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comparación generada correctamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida: ids insuficientes, duplicados o criterio no soportado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-19T10:15:30",
                                      "status": 400,
                                      "error": "Solicitud invalida",
                                      "message": "Debe enviar al menos dos autos para comparar"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alguno de los autos solicitados no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-19T10:15:30",
                                      "status": 404,
                                      "error": "Recurso no encontrado",
                                      "message": "No se encontro el auto con id: 2"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error inesperado del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2026-04-19T10:15:30",
                                      "status": 500,
                                      "error": "Error interno del servidor",
                                      "message": "Fallo inesperado"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<ComparacionDTO> comparar(@RequestParam(required = false, defaultValue = "general")
                                                       String criterio, @RequestBody List<Long> ids){
        return ResponseEntity.ok(
                comparadorService.compararAutos(ids, criterio)
        );
    }

}
