package com.example.autoservice.service;

import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AutoService {
    List<AutoResponseDTO> listarTodos();
    Optional<AutoResponseDTO> buscarPorId(Long id);
    AutoResponseDTO registrar(AutoRequestDTO autoRequestDTO);
    AutoResponseDTO actualizar(Long id, AutoRequestDTO autoRequestDTO);
    void eliminar(Long id);
    List<AutoResponseDTO> buscarPorMarca(String marca);
    List<AutoResponseDTO> buscarPorCategoria(String categoria);
}
