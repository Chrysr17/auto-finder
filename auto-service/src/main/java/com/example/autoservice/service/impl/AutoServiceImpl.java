package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoBusquedaResponseDTO;
import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.exception.InvalidAutoRequestException;
import com.example.autoservice.exception.InvalidSearchFilterException;
import com.example.autoservice.exception.RelatedResourceNotFoundException;
import com.example.autoservice.exception.ResourceNotFoundException;
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

import java.time.Year;
import java.util.List;
@Service
public class AutoServiceImpl implements AutoService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MIN_FABRICATION_YEAR = 1886;

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
        validateFilters(filtro);

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
    public AutoResponseDTO buscarPorId(Long id) {
        Auto auto = autoRepositoy.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado"));
        return autoMapper.toResponseDTO(auto);
    }

    @Override
    public AutoResponseDTO registrar(AutoRequestDTO dto) {
        validateCreateRequest(dto);

        Auto auto = autoMapper.toEntity(dto);

        auto.setMarca(marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RelatedResourceNotFoundException("Marca no existe")));

        auto.setModelo(modeloRepository.findById(dto.getModeloId())
                .orElseThrow(() -> new RelatedResourceNotFoundException("Modelo no existe")));

        auto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RelatedResourceNotFoundException("Categoria no existe")));

        return autoMapper.toResponseDTO(autoRepositoy.save(auto));
    }

    @Override
    public AutoResponseDTO actualizar(Long id, AutoRequestDTO dto) {
        validateUpdateRequest(dto);

        Auto existente = autoRepositoy.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado"));

        existente.setColor(dto.getColor());
        existente.setPrecio(dto.getPrecio());
        existente.setAnioFabricacion(dto.getAnioFabricacion());

        if (dto.getMarcaId() != null) {
            existente.setMarca(marcaRepository.findById(dto.getMarcaId())
                    .orElseThrow(() -> new RelatedResourceNotFoundException("Marca no existe")));
        }

        if (dto.getModeloId() != null) {
            existente.setModelo(modeloRepository.findById(dto.getModeloId())
                    .orElseThrow(() -> new RelatedResourceNotFoundException("Modelo no existe")));
        }

        if (dto.getCategoriaId() != null) {
            existente.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RelatedResourceNotFoundException("Categoria no existe")));
        }

        return autoMapper.toResponseDTO(autoRepositoy.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!autoRepositoy.existsById(id)) {
            throw new ResourceNotFoundException("Auto no encontrado");
        }
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

    private void validateFilters(AutoFiltroRequestDTO filtro) {
        if (filtro.getPrecioMin() != null && filtro.getPrecioMax() != null
                && filtro.getPrecioMin() > filtro.getPrecioMax()) {
            throw new InvalidSearchFilterException("precioMin no puede ser mayor que precioMax");
        }

        if (filtro.getAnioMin() != null && filtro.getAnioMax() != null
                && filtro.getAnioMin() > filtro.getAnioMax()) {
            throw new InvalidSearchFilterException("anioMin no puede ser mayor que anioMax");
        }

        if (filtro.getPage() != null && filtro.getPage() < 0) {
            throw new InvalidSearchFilterException("page no puede ser menor que 0");
        }

        if (filtro.getSize() != null && filtro.getSize() <= 0) {
            throw new InvalidSearchFilterException("size debe ser mayor que 0");
        }

        if (filtro.getSize() != null && filtro.getSize() > MAX_PAGE_SIZE) {
            throw new InvalidSearchFilterException("size no puede ser mayor que " + MAX_PAGE_SIZE);
        }

        if (filtro.getSortBy() != null && !isSupportedSortBy(filtro.getSortBy())) {
            throw new InvalidSearchFilterException("sortBy no soportado. Valores permitidos: precio, anioFabricacion, color, marca");
        }

        if (filtro.getDirection() != null
                && !("asc".equalsIgnoreCase(filtro.getDirection()) || "desc".equalsIgnoreCase(filtro.getDirection()))) {
            throw new InvalidSearchFilterException("direction no soportado. Valores permitidos: asc, desc");
        }
    }

    private boolean isSupportedSortBy(String sortBy) {
        return "precio".equalsIgnoreCase(sortBy)
                || "anioFabricacion".equalsIgnoreCase(sortBy)
                || "color".equalsIgnoreCase(sortBy)
                || "marca".equalsIgnoreCase(sortBy);
    }

    private void validateCreateRequest(AutoRequestDTO dto) {
        validateBaseFields(dto);

        if (dto.getMarcaId() == null) {
            throw new InvalidAutoRequestException("marcaId es obligatorio");
        }

        if (dto.getModeloId() == null) {
            throw new InvalidAutoRequestException("modeloId es obligatorio");
        }

        if (dto.getCategoriaId() == null) {
            throw new InvalidAutoRequestException("categoriaId es obligatorio");
        }
    }

    private void validateUpdateRequest(AutoRequestDTO dto) {
        validateBaseFields(dto);
    }

    private void validateBaseFields(AutoRequestDTO dto) {
        if (dto.getColor() == null || dto.getColor().isBlank()) {
            throw new InvalidAutoRequestException("color es obligatorio");
        }

        if (dto.getPrecio() == null) {
            throw new InvalidAutoRequestException("precio es obligatorio");
        }

        if (dto.getPrecio() <= 0) {
            throw new InvalidAutoRequestException("precio debe ser mayor que 0");
        }

        if (dto.getAnioFabricacion() == null) {
            throw new InvalidAutoRequestException("anioFabricacion es obligatorio");
        }

        int maxSupportedYear = Year.now().getValue() + 1;
        if (dto.getAnioFabricacion() < MIN_FABRICATION_YEAR || dto.getAnioFabricacion() > maxSupportedYear) {
            throw new InvalidAutoRequestException(
                    "anioFabricacion debe estar entre " + MIN_FABRICATION_YEAR + " y " + maxSupportedYear
            );
        }
    }
}
