package com.example.autoservice.service.impl;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;
import com.example.autoservice.mapper.MarcaMapper;
import com.example.autoservice.model.Marca;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.service.MarcaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<MarcaResponseDTO> buscarPorId(Long id) {
        return marcaRepository.findById(id)
                .map(marcaMapper::toDTO);
    }

    @Override
    public MarcaResponseDTO registrar(MarcaRequestDTO marcaRequestDTO) {
        Marca marca = marcaMapper.toEntity(marcaRequestDTO);
        return marcaMapper.toDTO(marcaRepository.save(marca));
    }
}
