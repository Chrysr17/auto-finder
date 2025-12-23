package com.example.autoservice.service;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ModeloService {
    List<ModeloResponseDTO> listar();
    Optional<ModeloResponseDTO> buscarPorId(Long id);
    ModeloResponseDTO registrar(ModeloRequestDTO modeloRequestDTO);
}
