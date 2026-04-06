package org.example.favoritoservice.service.impl;

import feign.FeignException;
import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.exception.DuplicateFavoriteException;
import org.example.favoritoservice.exception.InvalidFavoriteRequestException;
import org.example.favoritoservice.exception.RelatedResourceNotFoundException;
import org.example.favoritoservice.exception.ResourceNotFoundException;
import org.example.favoritoservice.model.Favorito;
import org.example.favoritoservice.repository.FavoritoRepository;
import org.example.favoritoservice.service.FavoritoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        validarSolicitud(usarname, autoId);

        AutoDTO auto = obtenerAutoExistente(autoId);
        if(auto == null || auto.getId() == null) {
            throw new RelatedResourceNotFoundException("No se encontro el auto");
        }

        if (favoritoRepository.existsByUsernameAndAutoId(usarname, autoId)){
            throw new DuplicateFavoriteException("Ya está en favoritos");
        }

        Favorito favorito = Favorito.builder()
                .username(usarname)
                .autoId(autoId)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return toDTO(favoritoRepository.save(favorito));
    }

    @Override
    @Transactional
    public void eliminarFavorito(String username, Long autoId) {
        validarSolicitud(username, autoId);

        Favorito favorito = favoritoRepository.findByUsernameAndAutoId(username, autoId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito no encontrado"));

        favoritoRepository.delete(favorito);
    }

    @Override
    public List<FavoritoDTO> listarFavoritos(String username) {
        validarUsername(username);

        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AutoDTO> listarFavoritosConDetalle(String username) {
        validarUsername(username);

        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .map(f -> autoClient.obtenerAuto(f.getAutoId()))
                .toList();
    }

    private AutoDTO obtenerAutoExistente(Long autoId) {
        try {
            return autoClient.obtenerAuto(autoId);
        } catch (FeignException.NotFound ex) {
            throw new RelatedResourceNotFoundException("No se encontro el auto");
        }
    }

    private void validarSolicitud(String username, Long autoId) {
        validarUsername(username);

        if (autoId == null) {
            throw new InvalidFavoriteRequestException("autoId es obligatorio");
        }

        if (autoId <= 0) {
            throw new InvalidFavoriteRequestException("autoId debe ser mayor que 0");
        }
    }

    private void validarUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidFavoriteRequestException("username es obligatorio");
        }
    }

}
