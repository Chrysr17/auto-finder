package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoBusquedaResponseDTO;
import com.example.autoservice.dto.AutoFiltroRequestDTO;
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

    @GetMapping("/buscar")
    public ResponseEntity<AutoBusquedaResponseDTO> buscar(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Integer anioMin,
            @RequestParam(required = false) Integer anioMax,
            @RequestParam(required = false) String color,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "precio") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .marcaId(marcaId)
                .modeloId(modeloId)
                .categoriaId(categoriaId)
                .precioMin(precioMin)
                .precioMax(precioMax)
                .anioMin(anioMin)
                .anioMax(anioMax)
                .color(color)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        return ResponseEntity.ok(autoService.buscarConFiltros(filtro));
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

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<AutoResponseDTO>> buscarPorMarca(@PathVariable String marca){
        List<AutoResponseDTO> autos = autoService.buscarPorMarca(marca);
        return ResponseEntity.ok(autos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<AutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria){
        List<AutoResponseDTO> autos = autoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(autos);
    }

}
