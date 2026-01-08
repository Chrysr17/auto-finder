package org.example.favoritoservice.service;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;

import java.util.List;

public interface FavoritoService {

    FavoritoDTO agregarFavorito(String usarname, Long autoId);
    void eliminarFavorito(String username, Long autoId);
    List<FavoritoDTO> listarFavoritos(String username);
    List<AutoDTO> listarFavoritosConDetalle(String username);
}
