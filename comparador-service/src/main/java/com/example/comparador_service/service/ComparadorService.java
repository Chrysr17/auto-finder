package com.example.comparador_service.service;

import com.example.comparador_service.dto.ComparacionDTO;

import java.util.List;

public interface ComparadorService {
    ComparacionDTO compararAutos(List<Long> ids, String criterio);
}
