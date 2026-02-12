package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.mapper.AutoImagenMapper;
import com.example.autoservice.model.Auto;
import com.example.autoservice.model.AutoImagen;
import com.example.autoservice.repository.AutoImagenRepository;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.service.AutoImagenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoImagenServiceImpl implements AutoImagenService {

    private final AutoRepositoy autoRepositoy;
    private final AutoImagenRepository autoImagenRepository;
    private final AutoImagenMapper autoImagenMapper;

    public AutoImagenServiceImpl(AutoRepositoy autoRepositoy, AutoImagenRepository autoImagenRepository, AutoImagenMapper autoImagenMapper) {
        this.autoRepositoy = autoRepositoy;
        this.autoImagenRepository = autoImagenRepository;
        this.autoImagenMapper = autoImagenMapper;
    }


    @Override
    public AutoResponseDTO agregarImagen(Long autoId, AutoImagenRequestDTO requestDTO) {
        Auto auto = autoRepositoy.findById(autoId)
                .orElseThrow(()-> new RuntimeException("No se encontro el auto: " +  autoId));
        if (autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())){
            throw new RuntimeException("Ya existe una imagen con el orden: " +  requestDTO.getOrden());
        }
        AutoImagen imagen = autoImagenMapper.toEntity(requestDTO);
        return null;
    }

    @Override
    public List<AutoImagenResponseDTO> listarImagenes(Long autoId) {
        return List.of();
    }

    @Override
    public AutoImagenResponseDTO editarImagen(Long imagenid, AutoImagenRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void eliminarImagen(Long imagenid) {

    }

    @Override
    public void establecerComoPortada(Long autoId, Long imagenid) {

    }

    @Override
    public String obtenerPortada(long autoId) {
        return "";
    }
}
