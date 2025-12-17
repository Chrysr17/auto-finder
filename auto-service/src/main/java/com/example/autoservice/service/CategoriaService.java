package com.example.autoservice.service;

import com.example.autoservice.dto.CategoriaDTO;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
     List<CategoriaDTO> listar();
     Optional<CategoriaDTO> buscarPorId(Long id);
     CategoriaDTO registrar(CategoriaDTO categoriaDTO);
}
