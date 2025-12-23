package com.example.autoservice.service;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;

import java.util.List;
import java.util.Optional;

public interface MarcaService {
    List<MarcaResponseDTO> listar();
    Optional<MarcaResponseDTO> buscarPorId(Long id);
    MarcaResponseDTO registrar(MarcaRequestDTO marcaRequestDTO);
}
