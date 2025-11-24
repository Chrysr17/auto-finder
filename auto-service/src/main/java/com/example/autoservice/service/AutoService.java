package com.example.autoservice.service;

import com.example.autoservice.dto.AutoDTO;

import java.util.List;

public interface AutoService {
    List<AutoDTO> listarTodos();
    AutoDTO buscarPorId(Long id);
    AutoDTO registrar(AutoDTO autoDTO);
    AutoDTO actualizar(Long id, AutoDTO autoDTO);
    void eliminar(Long id);
    List<AutoDTO> buscarPorMarca(String marca);
    List<AutoDTO> buscarPorCategoria(String categoria);
}
