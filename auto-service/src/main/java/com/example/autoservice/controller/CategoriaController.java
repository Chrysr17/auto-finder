package com.example.autoservice.controller;

import com.example.autoservice.dto.CategoriaDTO;
import com.example.autoservice.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class CategoriaController {

    private final CategoriaService categoriaRepository;

    public CategoriaController(CategoriaService categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> categoria(CategoriaDTO dto) {
        List<CategoriaDTO> categorias = categoriaRepository.listar();
        return ResponseEntity.ok(categorias);
    }

}
