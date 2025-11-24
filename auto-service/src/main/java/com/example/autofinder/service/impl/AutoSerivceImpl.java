package com.example.autofinder.service.impl;

import com.example.autofinder.dto.AutoDTO;
import com.example.autofinder.dto.AutoMapper;
import com.example.autofinder.model.Auto;
import com.example.autofinder.repository.AutoRepositoy;
import com.example.autofinder.service.AutoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoSerivceImpl implements AutoService {

    private final AutoRepositoy autoRepositoy;
    private final AutoMapper autoMapper;

    public AutoSerivceImpl(AutoRepositoy autoRepositoy, AutoMapper autoMapper) {
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
    public AutoDTO registrar(AutoDTO autoDto) {
        Auto auto = autoMapper.toEntity(autoDto);
        Auto guardado = autoRepositoy.save(auto);
        return autoMapper.toDTO(guardado);
    }

    @Override
    public AutoDTO actualizar(Long id, AutoDTO autoDto) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public List<AutoDTO> buscarPorMarca(String marca) {
        return List.of();
    }

    @Override
    public List<AutoDTO> buscarPorCategoria(String categoria) {
        return List.of();
    }
}
