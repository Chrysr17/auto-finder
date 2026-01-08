package org.example.favoritoservice.service.impl;

import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.model.Favorito;
import org.example.favoritoservice.repository.FavoritoRepository;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if(auto == null || auto.getId() == null) {
            throw new RuntimeException("No se encontro el auto");
        }

        if (favoritoRepository.existsByUsernameAndAutoId(usarname, autoId)){
            throw new RuntimeException("Ya está en favoritos");
        }

        Favorito favorito = Favorito.builder()
                .username(usarname)
                .autoId(autoId)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return toDTO(favoritoRepository.save(favorito));
    }

    @Override
    public void eliminarFavorito(String username, Long autoId) {
        favoritoRepository.deleteByUsernameAndAutoId(username, autoId);
    }

    @Override
    public List<FavoritoDTO> listarFavoritos(String username) {
        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AutoDTO> listarFavoritosConDetalle(String username) {
        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .map(f -> autoClient.obtenerAuto(f.getAutoId()))
                .toList();
    }

}
