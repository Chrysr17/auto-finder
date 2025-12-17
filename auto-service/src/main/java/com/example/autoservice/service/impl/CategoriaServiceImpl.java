package com.example.autoservice.service.impl;

import com.example.autoservice.dto.CategoriaDTO;
import com.example.autoservice.mapper.CategoriaMapper;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDTO)
                .toList();
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
