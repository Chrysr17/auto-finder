package com.example.autoservice.service;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;

import java.util.List;

public interface AutoImagenService {
    AutoImagenResponseDTO agregarImagen(Long autoId, AutoImagenRequestDTO requestDTO);
    List<AutoImagenResponseDTO> listarImagenes(Long autoId);
    AutoImagenResponseDTO editarImagen(Long imagenid, AutoImagenRequestDTO requestDTO);
    void eliminarImagen(Long imagenid);
    void establecerComoPortada(Long autoId, Long imagenid);
    String obtenerPortada(long autoId);
}
