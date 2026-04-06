package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.AutoComparadoDTO;
import com.example.comparador_service.dto.AutoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.exception.InvalidComparisonRequestException;
import com.example.comparador_service.exception.RelatedResourceNotFoundException;
import feign.FeignException;
import com.example.comparador_service.service.ComparadorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class ComparadorServiceImpl implements ComparadorService {

    private static final Set<String> CRITERIOS_PERMITIDOS = Set.of("general", "precio", "anio", "marca");

    private final AutoClient autoClient;

    public ComparadorServiceImpl(AutoClient autoClient) {
        this.autoClient = autoClient;
    }

    @Override
    public ComparacionDTO compararAutos(List<Long> ids, String criterio) {
        validarSolicitud(ids, criterio);

        String criterioNormalizado = normalizarCriterio(criterio);

        List<AutoDTO> autos = new ArrayList<>(ids.stream()
                .map(this::obtenerAutoExistente)
                .toList());

        // 1) ordenar
        switch (criterioNormalizado) {
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

                    if ("general".equals(criterioNormalizado)) {
                        builder
                                .precio(a.getPrecio())
                                .anioFabricacion(a.getAnioFabricacion())
                                .color(a.getColor())
                                .categoriaNombre(a.getCategoriaNombre())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("precio".equals(criterioNormalizado)) {
                        builder
                                .precio(a.getPrecio())
                                .categoriaNombre(a.getCategoriaNombre())
                                .color(a.getColor())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("anio".equals(criterioNormalizado)) {
                        builder
                                .anioFabricacion(a.getAnioFabricacion())
                                .categoriaNombre(a.getCategoriaNombre())
                                .color(a.getColor())
                                .imagenPortadaUrl(a.getImagenPortadaUrl());
                    } else if ("marca".equals(criterioNormalizado)) {
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
                .criterio(criterioNormalizado)
                .build();
    }

    private AutoDTO obtenerAutoExistente(Long autoId) {
        try {
            AutoDTO auto = autoClient.obtenerAuto(autoId);
            if (auto == null || auto.getId() == null) {
                throw new RelatedResourceNotFoundException("No se encontro el auto con id: " + autoId);
            }
            return auto;
        } catch (FeignException.NotFound ex) {
            throw new RelatedResourceNotFoundException("No se encontro el auto con id: " + autoId);
        }
    }

    private void validarSolicitud(List<Long> ids, String criterio) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidComparisonRequestException("Debe enviar al menos un auto para comparar");
        }

        if (ids.size() < 2) {
            throw new InvalidComparisonRequestException("Debe enviar al menos dos autos para comparar");
        }

        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new InvalidComparisonRequestException("Todos los ids deben ser mayores que 0");
        }

        if (ids.stream().distinct().count() != ids.size()) {
            throw new InvalidComparisonRequestException("No se pueden comparar autos duplicados");
        }

        String criterioNormalizado = normalizarCriterio(criterio);
        if (!CRITERIOS_PERMITIDOS.contains(criterioNormalizado)) {
            throw new InvalidComparisonRequestException(
                    "criterio no soportado. Valores permitidos: general, precio, anio, marca"
            );
        }
    }

    private String normalizarCriterio(String criterio) {
        return (criterio == null || criterio.isBlank()) ? "general" : criterio.toLowerCase();
    }
}
