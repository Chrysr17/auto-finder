package org.example.favoritoservice.service.impl;

import feign.FeignException;
import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDetalleDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.dto.FavoritoListaDTO;
import org.example.favoritoservice.dto.FavoritoMetadataRequestDTO;
import org.example.favoritoservice.dto.FavoritoSenalesDTO;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    private static final String DEFAULT_LIST_NAME = "General";
    private static final int MAX_LIST_NAME_LENGTH = 80;
    private static final int MAX_NOTE_LENGTH = 500;

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
                .fechaActualizacion(favorito.getFechaActualizacion())
                .listaNombre(listaDe(favorito))
                .nota(favorito.getNota())
                .build();
    }

    @Override
    public FavoritoDTO agregarFavorito(String usarname, Long autoId) {
        return agregarFavorito(usarname, autoId, null);
    }

    @Override
    public FavoritoDTO agregarFavorito(String usarname, Long autoId, FavoritoMetadataRequestDTO metadata) {
        validarSolicitud(usarname, autoId);
        String listaNombre = normalizarLista(metadata);
        String nota = normalizarNota(metadata);

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
                .fechaActualizacion(LocalDateTime.now())
                .listaNombre(listaNombre)
                .nota(nota)
                .build();

        return toDTO(favoritoRepository.save(favorito));
    }

    @Override
    public FavoritoDTO actualizarFavorito(String username, Long autoId, FavoritoMetadataRequestDTO metadata) {
        validarSolicitud(username, autoId);
        String listaNombre = normalizarLista(metadata);
        String nota = normalizarNota(metadata);

        Favorito favorito = favoritoRepository.findByUsernameAndAutoId(username, autoId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito no encontrado"));

        favorito.setListaNombre(listaNombre);
        favorito.setNota(nota);
        favorito.setFechaActualizacion(LocalDateTime.now());

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
    public List<FavoritoDTO> listarFavoritosPorLista(String username, String listaNombre) {
        validarUsername(username);
        String listaNormalizada = normalizarLista(listaNombre);

        return favoritoRepository.findByUsernameAndListaNombreIgnoreCaseOrderByFechaCreacionDesc(username, listaNormalizada)
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

    @Override
    public List<FavoritoDetalleDTO> listarFavoritosConDetalleEnriquecido(String username) {
        validarUsername(username);

        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .map(favorito -> FavoritoDetalleDTO.builder()
                        .id(favorito.getId())
                        .autoId(favorito.getAutoId())
                        .fechaCreacion(favorito.getFechaCreacion())
                        .fechaActualizacion(favorito.getFechaActualizacion())
                        .listaNombre(listaDe(favorito))
                        .nota(favorito.getNota())
                        .auto(autoClient.obtenerAuto(favorito.getAutoId()))
                        .build())
                .toList();
    }

    @Override
    public List<FavoritoListaDTO> listarListas(String username) {
        validarUsername(username);

        return favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)
                .stream()
                .collect(Collectors.groupingBy(this::listaDe))
                .entrySet()
                .stream()
                .map(entry -> FavoritoListaDTO.builder()
                        .nombre(entry.getKey())
                        .totalFavoritos(entry.getValue().size())
                        .ultimaActualizacion(entry.getValue()
                                .stream()
                                .map(this::fechaDeOrden)
                                .max(Comparator.naturalOrder())
                                .orElse(null))
                        .build())
                .sorted(Comparator.comparing(FavoritoListaDTO::getNombre, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Override
    public FavoritoSenalesDTO obtenerSenales(String username) {
        validarUsername(username);

        List<Favorito> favoritos = favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username);
        List<AutoDTO> autos = favoritos.stream()
                .map(favorito -> autoClient.obtenerAuto(favorito.getAutoId()))
                .toList();

        return FavoritoSenalesDTO.builder()
                .totalFavoritos(favoritos.size())
                .listas(contar(favoritos, this::listaDe))
                .marcasPrincipales(contar(autos, AutoDTO::getMarcaNombre))
                .categoriasPrincipales(contar(autos, AutoDTO::getCategoriaNombre))
                .autoIds(favoritos.stream().map(Favorito::getAutoId).toList())
                .build();
    }

    private AutoDTO obtenerAutoExistente(Long autoId) {
        try {
            return autoClient.obtenerAuto(autoId);
        } catch (FeignException.NotFound ex) {
            throw new RelatedResourceNotFoundException("No se encontro el auto");
        }
    }

    private String normalizarLista(FavoritoMetadataRequestDTO metadata) {
        return metadata == null ? DEFAULT_LIST_NAME : normalizarLista(metadata.getListaNombre());
    }

    private String normalizarLista(String listaNombre) {
        String normalized = listaNombre == null || listaNombre.isBlank()
                ? DEFAULT_LIST_NAME
                : listaNombre.trim();

        if (normalized.length() > MAX_LIST_NAME_LENGTH) {
            throw new InvalidFavoriteRequestException("listaNombre no puede superar " + MAX_LIST_NAME_LENGTH + " caracteres");
        }

        return normalized;
    }

    private String normalizarNota(FavoritoMetadataRequestDTO metadata) {
        if (metadata == null || metadata.getNota() == null || metadata.getNota().isBlank()) {
            return null;
        }

        String nota = metadata.getNota().trim();
        if (nota.length() > MAX_NOTE_LENGTH) {
            throw new InvalidFavoriteRequestException("nota no puede superar " + MAX_NOTE_LENGTH + " caracteres");
        }

        return nota;
    }

    private LocalDateTime fechaDeOrden(Favorito favorito) {
        return favorito.getFechaActualizacion() != null ? favorito.getFechaActualizacion() : favorito.getFechaCreacion();
    }

    private String listaDe(Favorito favorito) {
        return favorito.getListaNombre() == null || favorito.getListaNombre().isBlank()
                ? DEFAULT_LIST_NAME
                : favorito.getListaNombre();
    }

    private <T> List<FavoritoSenalesDTO.ConteoDTO> contar(List<T> items, Function<T, String> classifier) {
        return items.stream()
                .map(classifier)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER)))
                .map(entry -> FavoritoSenalesDTO.ConteoDTO.builder()
                        .nombre(entry.getKey())
                        .total(entry.getValue())
                        .build())
                .toList();
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
