package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.CategoriaDTO;
import com.example.autoservice.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<CategoriaDTO> categoria(CategoriaDTO dto) {
        return null;
    }

}
