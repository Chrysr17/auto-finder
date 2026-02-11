package com.example.autoservice.service;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.dto.AutoResponseDTO;

import java.util.List;

public interface AutoImagenService {
    AutoResponseDTO agregarImagen(Long autoId, AutoImagenRequestDTO requestDTO);
    List<AutoImagenResponseDTO> listarImagenes(Long autoId);
    AutoImagenResponseDTO editarImagen(Long imagenid, AutoImagenRequestDTO requestDTO);
    void eliminarImagen(Long imagenid);
    void establecerComoPortada(Long autoId, Long imagenid);
    String obtenerPortada(long autoId);
}
