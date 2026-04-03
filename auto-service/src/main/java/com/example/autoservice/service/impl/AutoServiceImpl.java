package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoBusquedaResponseDTO;
import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.mapper.AutoMapper;
import com.example.autoservice.model.Auto;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.repository.AutoSpecification;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import com.example.autoservice.service.AutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AutoServiceImpl implements AutoService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final AutoRepositoy autoRepositoy;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutoMapper autoMapper;

    public AutoServiceImpl(AutoRepositoy autoRepositoy,
                           MarcaRepository marcaRepository,
                           ModeloRepository modeloRepository,
                           CategoriaRepository categoriaRepository,
                           AutoMapper autoMapper) {
        this.autoRepositoy = autoRepositoy;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.categoriaRepository = categoriaRepository;
        this.autoMapper = autoMapper;
    }

    @Override
    public List<AutoResponseDTO> listarTodos() {
        return autoRepositoy.listarAutosConPortada()
                .stream()
                .map(autoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AutoBusquedaResponseDTO buscarConFiltros(AutoFiltroRequestDTO filtro) {
        Pageable pageable = PageRequest.of(
                normalizePage(filtro.getPage()),
                normalizeSize(filtro.getSize()),
                buildSort(filtro.getSortBy(), filtro.getDirection())
        );

        Page<AutoResponseDTO> page = autoRepositoy.findAll(AutoSpecification.withFilters(filtro), pageable)
                .map(autoMapper::toResponseDTO);

        return AutoBusquedaResponseDTO.builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Override
    public Optional<AutoResponseDTO> buscarPorId(Long id) {
        return autoRepositoy.findById(id)
                .map(autoMapper::toResponseDTO);
    }

    @Override
    public AutoResponseDTO registrar(AutoRequestDTO dto) {
        Auto auto = autoMapper.toEntity(dto);

        auto.setMarca(marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no existe")));

        auto.setModelo(modeloRepository.findById(dto.getModeloId())
                .orElseThrow(() -> new RuntimeException("Modelo no existe")));

        auto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no existe")));

        return autoMapper.toResponseDTO(autoRepositoy.save(auto));
    }

    @Override
    public AutoResponseDTO actualizar(Long id, AutoRequestDTO dto) {
        Auto existente = autoRepositoy.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        existente.setColor(dto.getColor());
        existente.setPrecio(dto.getPrecio());
        existente.setAnioFabricacion(dto.getAnioFabricacion());

        if (dto.getMarcaId() != null) {
            existente.setMarca(marcaRepository.findById(dto.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no existe")));
        }

        if (dto.getModeloId() != null) {
            existente.setModelo(modeloRepository.findById(dto.getModeloId())
                    .orElseThrow(() -> new RuntimeException("Modelo no existe")));
        }

        if (dto.getCategoriaId() != null) {
            existente.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no existe")));
        }

        return autoMapper.toResponseDTO(autoRepositoy.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        autoRepositoy.deleteById(id);
    }

    @Override
    public List<AutoResponseDTO> buscarPorMarca(String marca) {
        return autoRepositoy.findByMarcaNombre(marca)
                .stream()
                .map(autoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<AutoResponseDTO> buscarPorCategoria(String categoria) {
        return autoRepositoy.findByCategoriaNombre(categoria)
                .stream()
                .map(autoMapper::toResponseDTO)
                .toList();
    }

    private int normalizePage(Integer page) {
        return page == null || page < 0 ? DEFAULT_PAGE : page;
    }

    private int normalizeSize(Integer size) {
        return size == null || size <= 0 ? DEFAULT_SIZE : size;
    }

    private Sort buildSort(String sortBy, String direction) {
        String sortField = resolveSortField(sortBy);
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortField);
    }

    private String resolveSortField(String sortBy) {
        if ("anioFabricacion".equalsIgnoreCase(sortBy)) {
            return "anioFabricacion";
        }
        if ("color".equalsIgnoreCase(sortBy)) {
            return "color";
        }
        if ("marca".equalsIgnoreCase(sortBy)) {
            return "marca.nombre";
        }
        return "precio";
    }
}
