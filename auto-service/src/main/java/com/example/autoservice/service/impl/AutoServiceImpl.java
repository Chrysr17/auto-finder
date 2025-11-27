package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoDTO;
import com.example.autoservice.model.Auto;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import com.example.autoservice.service.AutoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepositoy autoRepositoy;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    public AutoServiceImpl(AutoRepositoy autoRepositoy, CategoriaRepository categoriaRepository, MarcaRepository marcaRepository, ModeloRepository modeloRepository) {
        this.autoRepositoy = autoRepositoy;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
    }

    private Auto toEntity(AutoDTO dto) {
        Auto auto = new Auto();
        auto.setId(dto.getId());
        auto.setColor(dto.getColor());
        auto.setPrecio(dto.getPrecio());
        auto.setAnioFabricacion(dto.getAnioFabricacion());

        // Relaciones (solo por ID)
        if (dto.getMarcaId() != null) {
            auto.setMarca(marcaRepository.findById(dto.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no existe")));
        }

        if (dto.getModeloId() != null) {
            auto.setModelo(modeloRepository.findById(dto.getModeloId())
                    .orElseThrow(() -> new RuntimeException("Modelo no existe")));
        }

        if (dto.getCategoriaId() != null) {
            auto.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria no existe")));
        }

        return auto;
    }

    private AutoDTO toDTO(Auto auto) {
        return AutoDTO.builder()
                .id(auto.getId())
                .color(auto.getColor())
                .precio(auto.getPrecio())
                .anioFabricacion(auto.getAnioFabricacion())

                .marcaId(auto.getMarca() != null ? auto.getMarca().getId() : null)
                .modeloId(auto.getModelo() != null ? auto.getModelo().getId() : null)
                .categoriaId(auto.getCategoria() != null ? auto.getCategoria().getId() : null)

                .marcaNombre(auto.getMarca() != null ? auto.getMarca().getNombre() : null)
                .modeloNombre(auto.getModelo() != null ? auto.getModelo().getNombre() : null)
                .categoriaNombre(auto.getCategoria() != null ? auto.getCategoria().getNombre() : null)

                .build();
    }

    @Override
    public List<AutoDTO> listarTodos() {
        return autoRepositoy.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<AutoDTO> buscarPorId(Long id) {
        return autoRepositoy.findById(id).map(this::toDTO);
    }

    @Override
    public AutoDTO registrar(AutoDTO autoDTO) {
        Auto auto = toEntity(autoDTO);
        Auto guardado = autoRepositoy.save(auto);
        return toDTO(guardado);
    }

    @Override
    public AutoDTO actualizar(Long id, AutoDTO autoDTO) {
        return autoRepositoy.findById(id)
                .map(autoExistente ->{
                    Auto auto = toEntity(autoDTO);
                    auto.setId(id);
                    return toDTO(autoRepositoy.save(auto));
                })
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        autoRepositoy.deleteById(id);
    }

    @Override
    public List<AutoDTO> buscarPorMarca(String marca) {
        return autoRepositoy.findByMarcaNombre(marca)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<AutoDTO> buscarPorCategoria(String categoria) {
        return autoRepositoy.findByCategoriaNombre(categoria)
                .stream().map(this::toDTO).toList();
    }
}
