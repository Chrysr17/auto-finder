package com.example.comparador_service.controller;

import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.service.ComparadorService;
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

    public ResponseEntity<ComparacionDTO> comprar(
            @RequestParam(required = false, defaultValue = "general") String criterio,
            @RequestBody List<Long> ids
            ){
        return ResponseEntity.ok(
                comparadorService.compararAutos(ids, criterio)
        );
    }

}
