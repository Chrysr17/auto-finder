package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.service.AutoImagenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoImagenController {

    private final AutoImagenService autoImagenService;

    public AutoImagenController(AutoImagenService autoImagenService) {
        this.autoImagenService = autoImagenService;
    }

    @GetMapping("/{autoId}/imagenes")
    public ResponseEntity<List<AutoImagenResponseDTO>> listarImagenes(@PathVariable Long autoId){
        return ResponseEntity.ok(autoImagenService.listarImagenes(autoId));
    }

    @PostMapping("/{autoId}/imagenes")
    public ResponseEntity<AutoImagenResponseDTO> agregarImagen(@PathVariable Long autoId, @Valid @RequestBody AutoImagenRequestDTO requestDTO){
        AutoImagenResponseDTO creada = autoImagenService.agregarImagen(autoId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
    @PutMapping("/imagenes/{imagenId}")
    public ResponseEntity<AutoImagenResponseDTO> actualizarImagen(@PathVariable Long imagenId, @Valid @RequestBody AutoImagenRequestDTO request) {
        return ResponseEntity.ok(autoImagenService.editarImagen(imagenId, request));
    }

}
