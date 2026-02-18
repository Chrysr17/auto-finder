package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.service.AutoImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
