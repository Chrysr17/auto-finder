package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.service.ComparadorService;

import java.util.List;

public class ComparadorServiceImpl implements ComparadorService {

    private final AutoClient autoClient;

    public ComparadorServiceImpl(AutoClient autoClient) {
        this.autoClient = autoClient;
    }

    @Override
    public ComparacionDTO compararAutos(List<Long> ids, String criterio) {
        return null;
    }
}
