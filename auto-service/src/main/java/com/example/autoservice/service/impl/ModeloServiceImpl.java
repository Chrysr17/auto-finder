package com.example.autoservice.service.impl;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.exception.InvalidCatalogRequestException;
import com.example.autoservice.exception.RelatedResourceNotFoundException;
import com.example.autoservice.exception.ResourceNotFoundException;
import com.example.autoservice.mapper.ModeloMapper;
import com.example.autoservice.model.Modelo;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import com.example.autoservice.service.ModeloService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ModeloResponseDTO buscarPorId(Long id) {
        validarId(id, "modeloId");

        return modeloRepository.findById(id)
                .map(modeloMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Modelo no encontrado"));
    }

    @Override
    public ModeloResponseDTO registrar(ModeloRequestDTO modeloRequestDTO) {
        validarModeloRequest(modeloRequestDTO);

        Modelo modelo = modeloMapper.toEntity(modeloRequestDTO);
        modelo.setMarca(marcaRepository.findById(modeloRequestDTO.getMarcaId())
                .orElseThrow(() -> new RelatedResourceNotFoundException("Marca no encontrada")));

        return modeloMapper.toDTO(modeloRepository.save(modelo));
    }

    private void validarModeloRequest(ModeloRequestDTO modeloRequestDTO) {
        if (modeloRequestDTO == null) {
            throw new InvalidCatalogRequestException("La solicitud de modelo es obligatoria");
        }
        if (modeloRequestDTO.getNombre() == null || modeloRequestDTO.getNombre().isBlank()) {
            throw new InvalidCatalogRequestException("nombre es obligatorio");
        }
        validarId(modeloRequestDTO.getMarcaId(), "marcaId");
    }

    private void validarId(Long id, String fieldName) {
        if (id == null) {
            throw new InvalidCatalogRequestException(fieldName + " es obligatorio");
        }
        if (id <= 0) {
            throw new InvalidCatalogRequestException(fieldName + " debe ser mayor que 0");
        }
    }
}
