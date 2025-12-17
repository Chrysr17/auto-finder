package com.example.autoservice.service;

import com.example.autoservice.dto.ModeloDTO;

import java.util.List;
import java.util.Optional;

public interface ModeloService {
    List<ModeloDTO> listar();
    Optional<ModeloDTO> buscarPorId(Long id);
    ModeloDTO registrar(ModeloDTO modeloDTO);
}
