package com.example.autoservice.controller;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;
import com.example.autoservice.service.MarcaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> listar() {
        List<MarcaResponseDTO> marcas = marcaService.listar();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> buscarPorId(@PathVariable Long id) {
        return marcaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDTO> registrar(@RequestBody MarcaRequestDTO marcaRequestDTO){
        MarcaResponseDTO nueva =  marcaService.registrar(marcaRequestDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

}
