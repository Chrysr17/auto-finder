package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.service.AutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    private final AutoService autoService;

    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    @GetMapping
    public ResponseEntity<List<AutoResponseDTO>> listar(){
        List<AutoResponseDTO> autos = autoService.listarTodos();
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> buscarPorid(@PathVariable Long id){
        return autoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AutoResponseDTO> registrar(@RequestBody AutoRequestDTO autoRequestDTO){
        AutoResponseDTO nuevo = autoService.registrar(autoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutoResponseDTO> actualizar(@PathVariable Long id, @RequestBody AutoRequestDTO autoRequestDTO){
        AutoResponseDTO actualizado = autoService.actualizar(id, autoRequestDTO);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/marca/{nombre}")
    public ResponseEntity<List<AutoResponseDTO>> buscarPorMarca(@PathVariable String marca){
        List<AutoResponseDTO> autos = autoService.buscarPorMarca(marca);
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<AutoResponseDTO>> buscarPorNombre(@PathVariable String categoria){
        List<AutoResponseDTO> autos = autoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(autos);
    }

}
