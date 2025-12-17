package com.example.autoservice.service.impl;

import com.example.autoservice.dto.CategoriaDTO;
import com.example.autoservice.service.CategoriaService;

import java.util.List;
import java.util.Optional;

public class CategoriaServiceImpl implements CategoriaService {
    @Override
    public List<CategoriaDTO> listar() {
        return List.of();
    }

    @Override
    public Optional<CategoriaDTO> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public CategoriaDTO registrar(CategoriaDTO categoriaDTO) {
        return null;
    }
}
