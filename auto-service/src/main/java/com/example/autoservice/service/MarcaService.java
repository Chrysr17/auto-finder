package com.example.autoservice.service;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;

import java.util.List;

public interface MarcaService {
    List<MarcaResponseDTO> listar();
    MarcaResponseDTO buscarPorId(Long id);
    MarcaResponseDTO registrar(MarcaRequestDTO marcaRequestDTO);
}
