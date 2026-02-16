package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
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
    public AutoImagenResponseDTO agregarImagen(Long autoId, AutoImagenRequestDTO requestDTO) {
        Auto auto = autoRepositoy.findById(autoId)
                .orElseThrow(()-> new RuntimeException("No se encontro el auto: " +  autoId));
        if (autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())){
            throw new RuntimeException("Ya existe una imagen con el orden: " +  requestDTO.getOrden());
        }
        AutoImagen imagen = autoImagenMapper.toEntity(requestDTO);
        imagen.setAuto(auto);
        AutoImagen guardada = autoImagenRepository.save(imagen);
        return autoImagenMapper.toResponseDTO(guardada);
    }

    @Override
    public List<AutoImagenResponseDTO> listarImagenes(Long autoId) {

        if (!autoRepositoy.existsById(autoId)){
            throw new RuntimeException("No se encontro el auto con id: " +  autoId);
        }
        return autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)
                .stream()
                .map(autoImagenMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AutoImagenResponseDTO editarImagen(Long imagenid, AutoImagenRequestDTO requestDTO) {

        AutoImagen imagen = autoImagenRepository.findById(imagenid)
                .orElseThrow(()-> new RuntimeException("Imagen no encontrada con id: " + imagenid));

        Long autoId = imagen.getAuto().getId();

        if(!imagen.getOrden().equals(requestDTO.getOrden())
                && autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())){
            throw new RuntimeException("Ya existe una imagen con el orden" + requestDTO.getOrden());
        }

        imagen.setUrl(requestDTO.getUrl());
        imagen.setOrden((requestDTO.getOrden()));

        AutoImagen actualizada = autoImagenRepository.save(imagen);
        return autoImagenMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminarImagen(Long imagenid) {
        AutoImagen imagen = autoImagenRepository.findById(imagenid)
                .orElseThrow(()->new RuntimeException("Imagen no encotrada"));
        autoImagenRepository.deleteById(imagenid);
    }

    @Override
    public void establecerComoPortada(Long autoId, Long imagenid) {

    }

    @Override
    public String obtenerPortada(long autoId) {
        return "";
    }
}
