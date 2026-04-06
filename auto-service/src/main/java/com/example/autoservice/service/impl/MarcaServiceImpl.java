package com.example.autoservice.service.impl;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;
import com.example.autoservice.exception.InvalidCatalogRequestException;
import com.example.autoservice.exception.ResourceNotFoundException;
import com.example.autoservice.mapper.MarcaMapper;
import com.example.autoservice.model.Marca;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.service.MarcaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    @Override
    public List<MarcaResponseDTO> listar() {
        return marcaRepository.findAll()
                .stream()
                .map(marcaMapper::toDTO)
                .toList();
    }

    @Override
    public MarcaResponseDTO buscarPorId(Long id) {
        validarId(id, "marcaId");

        return marcaRepository.findById(id)
                .map(marcaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada"));
    }

    @Override
    public MarcaResponseDTO registrar(MarcaRequestDTO marcaRequestDTO) {
        validarNombre(marcaRequestDTO);

        Marca marca = marcaMapper.toEntity(marcaRequestDTO);
        return marcaMapper.toDTO(marcaRepository.save(marca));
    }

    private void validarId(Long id, String fieldName) {
        if (id == null) {
            throw new InvalidCatalogRequestException(fieldName + " es obligatorio");
        }
        if (id <= 0) {
            throw new InvalidCatalogRequestException(fieldName + " debe ser mayor que 0");
        }
    }

    private void validarNombre(MarcaRequestDTO marcaRequestDTO) {
        if (marcaRequestDTO == null) {
            throw new InvalidCatalogRequestException("La solicitud de marca es obligatoria");
        }
        if (marcaRequestDTO.getNombre() == null || marcaRequestDTO.getNombre().isBlank()) {
            throw new InvalidCatalogRequestException("nombre es obligatorio");
        }
    }
}
