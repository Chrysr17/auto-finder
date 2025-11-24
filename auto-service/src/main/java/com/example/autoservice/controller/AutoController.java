package com.example.autoservice.controller;

import com.example.autoservice.dto.AutoDTO;
import com.example.autoservice.service.AutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
