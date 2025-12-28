package com.example.autoservice.controller;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.service.ModeloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @RequestMapping
    private ResponseEntity<List<ModeloResponseDTO>> listar(){
        List<ModeloResponseDTO> modelos = modeloService.listar();
        return ResponseEntity.ok(modelos);
    }

    @GetMapping("/{id}")
    private ResponseEntity<ModeloResponseDTO> buscarPorId(@PathVariable Long id){
        return modeloService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<ModeloResponseDTO> registrar(@RequestBody ModeloRequestDTO modeloRequestDTO){
        ModeloResponseDTO nuevo =  modeloService.registrar(modeloRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

}
