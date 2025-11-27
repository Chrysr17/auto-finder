package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoDTO;
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
    public ResponseEntity<List<AutoDTO>> listar(){
        List<AutoDTO> autos = autoService.listarTodos();
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoDTO> buscarPorid(@PathVariable Long id){
        return autoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AutoDTO> registrar(@RequestBody AutoDTO autoDTO){
        AutoDTO nuevo = autoService.registrar(autoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutoDTO> actualizar(@PathVariable Long id, @RequestBody AutoDTO autoDTO){
        AutoDTO actualizado = autoService.actualizar(id, autoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/marca/{nombre}")
    public ResponseEntity<List<AutoDTO>> buscarPorMarca(@PathVariable String marca){
        List<AutoDTO> autos = autoService.buscarPorMarca(marca);
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<AutoDTO>> buscarPorNombre(@PathVariable String categoria){
        List<AutoDTO> autos = autoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(autos);
    }

}
