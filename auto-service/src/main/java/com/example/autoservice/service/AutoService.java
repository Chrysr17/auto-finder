package com.example.autoservice.service;

import com.example.autoservice.dto.AutoBusquedaResponseDTO;
import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;

import java.util.List;

public interface AutoService {
    List<AutoResponseDTO> listarTodos();
    AutoBusquedaResponseDTO buscarConFiltros(AutoFiltroRequestDTO filtro);
    AutoResponseDTO buscarPorId(Long id);
    AutoResponseDTO registrar(AutoRequestDTO autoRequestDTO);
    AutoResponseDTO actualizar(Long id, AutoRequestDTO autoRequestDTO);
    void eliminar(Long id);
    List<AutoResponseDTO> buscarPorMarca(String marca);
    List<AutoResponseDTO> buscarPorCategoria(String categoria);
}
