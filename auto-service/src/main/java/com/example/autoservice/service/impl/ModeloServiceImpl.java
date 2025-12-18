package com.example.autoservice.service.impl;

import com.example.autoservice.dto.ModeloDTO;
import com.example.autoservice.mapper.ModeloMapper;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import com.example.autoservice.service.ModeloService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloServiceImpl implements ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloMapper modeloMapper;

    public ModeloServiceImpl(ModeloRepository modeloRepository, MarcaRepository marcaRepository, ModeloMapper modeloMapper) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.modeloMapper = modeloMapper;
    }

    @Override
    public List<ModeloDTO> listar() {
        return List.of();
    }

    @Override
    public Optional<ModeloDTO> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public ModeloDTO registrar(ModeloDTO modeloDTO) {
        return null;
    }
}
