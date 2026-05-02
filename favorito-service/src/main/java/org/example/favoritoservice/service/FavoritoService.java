package org.example.favoritoservice.service;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDetalleDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.dto.FavoritoListaDTO;
import org.example.favoritoservice.dto.FavoritoMetadataRequestDTO;
import org.example.favoritoservice.dto.FavoritoSenalesDTO;

import java.util.List;

public interface FavoritoService {

    FavoritoDTO agregarFavorito(String usarname, Long autoId);
    FavoritoDTO agregarFavorito(String username, Long autoId, FavoritoMetadataRequestDTO metadata);
    FavoritoDTO actualizarFavorito(String username, Long autoId, FavoritoMetadataRequestDTO metadata);
    void eliminarFavorito(String username, Long autoId);
    List<FavoritoDTO> listarFavoritos(String username);
    List<FavoritoDTO> listarFavoritosPorLista(String username, String listaNombre);
    List<AutoDTO> listarFavoritosConDetalle(String username);
    List<FavoritoDetalleDTO> listarFavoritosConDetalleEnriquecido(String username);
    List<FavoritoListaDTO> listarListas(String username);
    FavoritoSenalesDTO obtenerSenales(String username);
}
