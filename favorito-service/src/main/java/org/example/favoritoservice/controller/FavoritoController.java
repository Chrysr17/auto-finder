package org.example.favoritoservice.controller;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    private String username(Authentication authentication){
        return authentication.getName();
    }

    @GetMapping("/detalle")
    public ResponseEntity<List<AutoDTO>> listarAutosFavoritos(Authentication authentication){
        List<AutoDTO> autosFavoritos = favoritoService.listarFavoritosConDetalle(authentication.getName());
        return ResponseEntity.ok(autosFavoritos);
    }

    @GetMapping
    public ResponseEntity<List<FavoritoDTO>> listar(Authentication authentication) {
        List<FavoritoDTO> favoritos = favoritoService.listarFavoritos(username(authentication));
        return ResponseEntity.ok(favoritos);
    }

}
