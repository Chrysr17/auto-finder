package com.example.autoservice.service;

import com.example.autoservice.dto.CategoriaRequestDTO;
import com.example.autoservice.dto.CategoriaResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
     List<CategoriaResponseDTO> listar();
     Optional<CategoriaResponseDTO> buscarPorId(Long id);
     CategoriaResponseDTO registrar(CategoriaRequestDTO categoriaRequestDTO);
     CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO categoriaRequestDTO);
}
