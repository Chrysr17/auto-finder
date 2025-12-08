package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.AutoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.service.ComparadorService;
import org.springframework.stereotype.Service;

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

        return ComparacionDTO.builder()
                .autosComparados(autos)
                .criterio(criterio)
                .build();
    }
}
