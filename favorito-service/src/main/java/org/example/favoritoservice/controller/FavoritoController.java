package org.example.favoritoservice.controller;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDetalleDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.dto.FavoritoListaDTO;
import org.example.favoritoservice.dto.FavoritoMetadataRequestDTO;
import org.example.favoritoservice.dto.FavoritoSenalesDTO;
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

    @GetMapping("/detalle-enriquecido")
    public ResponseEntity<List<FavoritoDetalleDTO>> listarAutosFavoritosEnriquecidos(Authentication authentication){
        return ResponseEntity.ok(favoritoService.listarFavoritosConDetalleEnriquecido(username(authentication)));
    }

    @GetMapping("/listas")
    public ResponseEntity<List<FavoritoListaDTO>> listarListas(Authentication authentication) {
        return ResponseEntity.ok(favoritoService.listarListas(username(authentication)));
    }

    @GetMapping("/senales")
    public ResponseEntity<FavoritoSenalesDTO> obtenerSenales(Authentication authentication) {
        return ResponseEntity.ok(favoritoService.obtenerSenales(username(authentication)));
    }

    @GetMapping
    public ResponseEntity<List<FavoritoDTO>> listar(
            @RequestParam(required = false) String listaNombre,
            Authentication authentication
    ) {
        List<FavoritoDTO> favoritos = listaNombre == null || listaNombre.isBlank()
                ? favoritoService.listarFavoritos(username(authentication))
                : favoritoService.listarFavoritosPorLista(username(authentication), listaNombre);
        return ResponseEntity.ok(favoritos);
    }

    @PostMapping("/{autoId}")
    public ResponseEntity<FavoritoDTO> agregar(
            @PathVariable Long autoId,
            @RequestBody(required = false) FavoritoMetadataRequestDTO metadata,
            Authentication authentication
    ){
        FavoritoDTO favorito = favoritoService.agregarFavorito(username(authentication), autoId, metadata);
        return ResponseEntity.status(HttpStatus.CREATED).body(favorito);
    }

    @PatchMapping("/{autoId}")
    public ResponseEntity<FavoritoDTO> actualizar(
            @PathVariable Long autoId,
            @RequestBody(required = false) FavoritoMetadataRequestDTO metadata,
            Authentication authentication
    ){
        return ResponseEntity.ok(favoritoService.actualizarFavorito(username(authentication), autoId, metadata));
    }

    @DeleteMapping("/{autoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long autoId, Authentication authentication){
        favoritoService.eliminarFavorito(username(authentication), autoId);
        return ResponseEntity.noContent().build();
    }

}
