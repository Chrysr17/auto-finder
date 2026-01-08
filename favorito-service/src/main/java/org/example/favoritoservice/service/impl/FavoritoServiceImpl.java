package org.example.favoritoservice.service.impl;

import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.repository.FavoritoRepository;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoServiceImpl(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    @Override
    public FavoritoDTO agregarFavorito(FavoritoDTO favoritoDTO) {
        return null;
    }

    @Override
    public void eliminarFavorito(String username, Long autoId) {

    }

    @Override
    public List<FavoritoDTO> listarFavoritos(String username) {
        return List.of();
    }

    @Override
    public List<AutoDTO> listarFavoritosConDetalle(String username) {
        return List.of();
    }

}
