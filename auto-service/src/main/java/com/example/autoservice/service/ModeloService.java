package com.example.autoservice.service;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;

import java.util.List;

public interface ModeloService {
    List<ModeloResponseDTO> listar();
    ModeloResponseDTO buscarPorId(Long id);
    ModeloResponseDTO registrar(ModeloRequestDTO modeloRequestDTO);
}
