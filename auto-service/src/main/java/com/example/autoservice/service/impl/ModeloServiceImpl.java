package com.example.autoservice.service.impl;

import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.mapper.ModeloMapper;
import com.example.autoservice.model.Modelo;
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
    public List<ModeloResponseDTO> listar() {
        return modeloRepository.findAll()
                .stream()
                .map(modeloMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<ModeloResponseDTO> buscarPorId(Long id) {
        return modeloRepository.findById(id)
                .map(modeloMapper::toDTO);
    }

    @Override
    public ModeloResponseDTO registrar(ModeloResponseDTO modeloDTO) {
        Modelo modelo = modeloMapper.toEntity(modeloDTO);
        modelo.setMarca(marcaRepository.findById(modeloDTO.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada")));

        return modeloMapper.toDTO(modeloRepository.save(modelo));
    }
}
