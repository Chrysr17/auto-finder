package com.example.autoservice.service;

import com.example.autoservice.dto.AutoBusquedaResponseDTO;
import com.example.autoservice.dto.AutoCreateRequestDTO;
import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.dto.AutoUpdateRequestDTO;

import java.util.List;

public interface AutoService {
    List<AutoResponseDTO> listarTodos();
    AutoBusquedaResponseDTO buscarConFiltros(AutoFiltroRequestDTO filtro);
    AutoResponseDTO buscarPorId(Long id);
    AutoResponseDTO registrar(AutoCreateRequestDTO autoRequestDTO);
    AutoResponseDTO actualizar(Long id, AutoUpdateRequestDTO autoRequestDTO);
    void eliminar(Long id);
    List<AutoResponseDTO> buscarPorMarca(String marca);
    List<AutoResponseDTO> buscarPorCategoria(String categoria);
}
