package com.example.autoservice.service.impl;

import com.example.autoservice.dto.CategoriaRequestDTO;
import com.example.autoservice.dto.CategoriaResponseDTO;
import com.example.autoservice.exception.InvalidCatalogRequestException;
import com.example.autoservice.exception.ResourceNotFoundException;
import com.example.autoservice.mapper.CategoriaMapper;
import com.example.autoservice.model.Categoria;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public CategoriaResponseDTO buscarPorId(Long id) {
        validarId(id, "categoriaId");

        return categoriaRepository.findById(id)
                .map(categoriaMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));
    }

    @Override
    public CategoriaResponseDTO registrar(CategoriaRequestDTO categoriaRequestDTO) {
        validarCategoriaRequest(categoriaRequestDTO);

        Categoria categoria = categoriaMapper.toEntity(categoriaRequestDTO);
        return categoriaMapper.toResponseDTO(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        validarId(id, "categoriaId");
        validarCategoriaRequest(categoriaRequestDTO);

        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada"));

        existente.setNombre(categoriaRequestDTO.getNombre());
        existente.setDescripcion(categoriaRequestDTO.getDescripcion());

        return categoriaMapper.toResponseDTO(categoriaRepository.save(existente));
    }

    private void validarCategoriaRequest(CategoriaRequestDTO categoriaRequestDTO) {
        if (categoriaRequestDTO == null) {
            throw new InvalidCatalogRequestException("La solicitud de categoria es obligatoria");
        }
        if (categoriaRequestDTO.getNombre() == null || categoriaRequestDTO.getNombre().isBlank()) {
            throw new InvalidCatalogRequestException("nombre es obligatorio");
        }
        if (categoriaRequestDTO.getDescripcion() == null || categoriaRequestDTO.getDescripcion().isBlank()) {
            throw new InvalidCatalogRequestException("descripcion es obligatoria");
        }
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
