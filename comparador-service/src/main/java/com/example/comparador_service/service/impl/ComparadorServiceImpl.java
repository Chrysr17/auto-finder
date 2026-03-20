package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.AutoComparadoDTO;
import com.example.comparador_service.dto.AutoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.service.ComparadorService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComparadorServiceImpl implements ComparadorService {

    private final AutoClient autoClient;

    public ComparadorServiceImpl(AutoClient autoClient) {
        this.autoClient = autoClient;
    }

    @Override
    public ComparacionDTO compararAutos(List<Long> ids, String criterio) {

        List<AutoDTO> autos = ids.stream()
                .map(autoClient::obtenerAuto)
                .collect(Collectors.toList());

        String c = (criterio == null || criterio.isBlank()) ? "general" : criterio.toLowerCase();

        // 1) ordenar
        switch (c) {
            case "precio" -> autos.sort(Comparator.comparing(AutoDTO::getPrecio,
                    Comparator.nullsLast(Comparator.naturalOrder())));
            case "anio" -> autos.sort(Comparator.comparing(AutoDTO::getAnioFabricacion,
                    Comparator.nullsLast(Comparator.naturalOrder())));
            case "marca" -> autos.sort(Comparator.comparing(AutoDTO::getMarcaNombre,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
            default -> { /* sin orden especial */ }
        }

        // 2) mapear a DTO resumido (solo lo que quieres devolver)
        List<AutoComparadoDTO> comparados = autos.stream()
                .map(a -> {
                    AutoComparadoDTO.AutoComparadoDTOBuilder builder = AutoComparadoDTO.builder()
                            .id(a.getId())
                            .marcaNombre(a.getMarcaNombre())
                            .modeloNombre(a.getModeloNombre());

                    if ("general".equals(c)) {
                        builder
                                .precio(a.getPrecio())
                                .anioFabricacion(a.getAnioFabricacion())
                                .color(a.getColor())
                                .categoriaNombre(a.getCategoriaNombre())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("precio".equals(c)) {
                        builder
                                .precio(a.getPrecio())
                                .categoriaNombre(a.getCategoriaNombre())
                                .color(a.getColor())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("anio".equals(c)) {
                        builder
                                .anioFabricacion(a.getAnioFabricacion())
                                .categoriaNombre(a.getCategoriaNombre())
                                .color(a.getColor())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("marca".equals(c)) {
                        builder
                                .precio(a.getPrecio())
                                .color(a.getColor())
                                .categoriaNombre(a.getCategoriaNombre())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    }

                    return builder.build();
                })
                .toList();

        return ComparacionDTO.builder()
                .autosComparados(comparados)
                .criterio(criterio)
                .build();
    }
}
