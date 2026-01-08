package org.example.favoritoservice.service.impl;

import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.model.Favorito;
import org.example.favoritoservice.repository.FavoritoRepository;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final AutoClient autoClient;

    public FavoritoServiceImpl(FavoritoRepository favoritoRepository, AutoClient autoClient) {
        this.favoritoRepository = favoritoRepository;
        this.autoClient = autoClient;
    }

    private FavoritoDTO toDTO(Favorito favorito){
        return FavoritoDTO.builder()
                .id(favorito.getId())
                .autoId(favorito.getAutoId())
                .fechaCreacion(favorito.getFechaCreacion())
                .build();
    }

    @Override
    public FavoritoDTO agregarFavorito(String usarname, Long autoId) {

        AutoDTO auto = autoClient.obtenerAuto(autoId);
        if(auto == null ){

        }

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
