package com.example.autoservice.service;

import com.example.autoservice.dto.MarcaDTO;

import java.util.List;
import java.util.Optional;

public interface MarcaService {
    List<MarcaDTO> listar();
    Optional<MarcaDTO> buscarPorId(Long id);
    MarcaDTO registrar(MarcaDTO marcaDTO);
}
