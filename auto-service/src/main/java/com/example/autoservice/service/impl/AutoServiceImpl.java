package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoDTO;
import com.example.autoservice.mapper.AutoMapper;
import com.example.autoservice.model.Auto;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.service.AutoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepositoy autoRepositoy;
    private final AutoMapper autoMapper;

    public AutoServiceImpl(AutoRepositoy autoRepositoy, AutoMapper autoMapper) {
        this.autoRepositoy = autoRepositoy;
        this.autoMapper = autoMapper;
    }

    @Override
    public List<AutoDTO> listarTodos() {
        return autoRepositoy.findAll()
                .stream()
                .map(autoMapper::toDTO)
                .toList();
    }

    @Override
    public AutoDTO buscarPorId(Long id) {
        return autoRepositoy.findById(id)
                .map(autoMapper::toDTO)
                .orElse(null);
    }

    @Override
    public AutoDTO registrar(AutoDTO autoDTO) {
        Auto auto = autoMapper.toEntity(autoDTO);
        Auto guardado = autoRepositoy.save(auto);
        return autoMapper.toDTO(guardado);
    }

    @Override
    public AutoDTO actualizar(Long id, AutoDTO autoDTO) {

        Auto auto = autoRepositoy.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        Auto actualizado = autoMapper.toEntity(autoDTO);
        actualizado.setId(id);

        Auto guardado = autoRepositoy.save(actualizado);

        return autoMapper.toDTO(guardado);
    }

    @Override
    public void eliminar(Long id) {
        autoRepositoy.deleteById(id);
    }

    @Override
    public List<AutoDTO> buscarPorMarca(String marca) {
        return autoRepositoy.findByMarcaNombre(marca)
                .stream()
                .map(autoMapper::toDTO)
                .toList();
    }

    @Override
    public List<AutoDTO> buscarPorCategoria(String categoria) {
        return autoRepositoy.findByCategoriaNombre(categoria)
                .stream()
                .map(autoMapper::toDTO)
                .toList();
    }
}
