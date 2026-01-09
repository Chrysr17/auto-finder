package org.example.favoritoservice.controller;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{autoId}")
    public ResponseEntity<FavoritoDTO> agregar(@PathVariable Long autoId, Authentication authentication){
        FavoritoDTO favorito = favoritoService.agregarFavorito(username(authentication), autoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(favorito);
    }

    @DeleteMapping("/{autoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long autoId, Authentication authentication){
        favoritoService.eliminarFavorito(username(authentication), autoId);
        return ResponseEntity.ok().build();
    }

}
