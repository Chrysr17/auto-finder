package com.example.comparador_service.controller;

import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.service.ComparadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
    public ResponseEntity<ComparacionDTO> comparar(@RequestParam(required = false, defaultValue = "general")
                                                       String criterio, @RequestBody List<Long> ids){
        return ResponseEntity.ok(
                comparadorService.compararAutos(ids, criterio)
        );
    }

}
