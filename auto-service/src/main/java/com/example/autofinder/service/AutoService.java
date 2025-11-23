package com.example.autofinder.service;

import com.example.autofinder.dto.AutoDTO;

import java.util.List;

public interface AutoService {
    List<AutoDTO> listarTodos();
    AutoDTO buscarPorId(Long id);
    AutoDTO registrar(AutoDTO autoDto);
    AutoDTO actualizar(Long id, AutoDTO autoDto);
    void eliminar(Long id);
    List<AutoDTO> buscarPorMarca(String marca);
    List<AutoDTO> buscarPorCategoria(String categoria);
}
