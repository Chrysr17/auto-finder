package com.example.autoservice.service;

import com.example.autoservice.dto.CategoriaRequestDTO;
import com.example.autoservice.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
     List<CategoriaResponseDTO> listar();
     CategoriaResponseDTO buscarPorId(Long id);
     CategoriaResponseDTO registrar(CategoriaRequestDTO categoriaRequestDTO);
     CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO categoriaRequestDTO);
}
