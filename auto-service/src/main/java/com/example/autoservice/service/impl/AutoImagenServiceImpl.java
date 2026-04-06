package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.exception.InvalidCatalogRequestException;
import com.example.autoservice.exception.ResourceConflictException;
import com.example.autoservice.exception.ResourceNotFoundException;
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
        validarAutoId(autoId);
        validarImagenRequest(requestDTO);

        Auto auto = autoRepositoy.findById(autoId)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado con id: " +  autoId));
        if (autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())){
            throw new ResourceConflictException("Ya existe una imagen con el orden: " +  requestDTO.getOrden());
        }
        AutoImagen imagen = autoImagenMapper.toEntity(requestDTO);
        imagen.setAuto(auto);
        AutoImagen guardada = autoImagenRepository.save(imagen);
        return autoImagenMapper.toResponseDTO(guardada);
    }

    @Override
    public List<AutoImagenResponseDTO> listarImagenes(Long autoId) {
        validarAutoId(autoId);

        if (!autoRepositoy.existsById(autoId)){
            throw new ResourceNotFoundException("Auto no encontrado con id: " +  autoId);
        }
        return autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)
                .stream()
                .map(autoImagenMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AutoImagenResponseDTO editarImagen(Long imagenid, AutoImagenRequestDTO requestDTO) {
        validarImagenId(imagenid);
        validarImagenRequest(requestDTO);

        AutoImagen imagen = autoImagenRepository.findById(imagenid)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imagenid));

        Long autoId = imagen.getAuto().getId();

        if(!imagen.getOrden().equals(requestDTO.getOrden())
                && autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())){
            throw new ResourceConflictException("Ya existe una imagen con el orden: " + requestDTO.getOrden());
        }

        imagen.setUrl(requestDTO.getUrl());
        imagen.setOrden((requestDTO.getOrden()));

        AutoImagen actualizada = autoImagenRepository.save(imagen);
        return autoImagenMapper.toResponseDTO(actualizada);
    }

    @Override
    public void eliminarImagen(Long imagenid) {
        validarImagenId(imagenid);

        autoImagenRepository.findById(imagenid)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imagenid));
        autoImagenRepository.deleteById(imagenid);
    }

    @Override
    public void establecerComoPortada(Long autoId, Long imagenid) {
        validarAutoId(autoId);
        validarImagenId(imagenid);

        Auto auto = autoRepositoy.findById(autoId)
                .orElseThrow(() -> new ResourceNotFoundException("Auto no encontrado con id: " + autoId));

        AutoImagen nuevaPortada = autoImagenRepository.findById(imagenid)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imagenid));

        if (!nuevaPortada.getAuto().getId().equals(auto.getId())){
            throw new InvalidCatalogRequestException("La imagen no pertenece a este auto");
        }

        if (nuevaPortada.getOrden() != null && nuevaPortada.getOrden() == 1){
            return;
        }

        List<AutoImagen> imagenes = autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId);

        AutoImagen portadaActual = imagenes.stream()
                .filter(img -> img.getOrden() !=null && img.getOrden() == 1)
                .findFirst()
                .orElse(null);

        Integer ordenOriginalNueva = nuevaPortada.getOrden();

        nuevaPortada.setOrden(1);
        autoImagenRepository.save(nuevaPortada);

        if(portadaActual != null){

            int ordenParaPortadaAnterior = (ordenOriginalNueva != null)
                    ? ordenOriginalNueva
                    : Math.max(2, imagenes.size() + 1);

            portadaActual.setOrden(ordenParaPortadaAnterior);
            autoImagenRepository.save(portadaActual);
        }

    }

    @Override
    public String obtenerPortada(Long autoId) {
        validarAutoId(autoId);

        if (!autoRepositoy.existsById(autoId)){
            throw new ResourceNotFoundException("Auto no encontrado con id: " + autoId);
        }
        return autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)
                .stream()
                .filter(img -> img.getOrden() != null && img.getOrden() == 1)
                .map(AutoImagen::getUrl)
                .findFirst()
                .orElse(null);
    }

    private void validarAutoId(Long autoId) {
        if (autoId == null) {
            throw new InvalidCatalogRequestException("autoId es obligatorio");
        }
        if (autoId <= 0) {
            throw new InvalidCatalogRequestException("autoId debe ser mayor que 0");
        }
    }

    private void validarImagenId(Long imagenId) {
        if (imagenId == null) {
            throw new InvalidCatalogRequestException("imagenId es obligatorio");
        }
        if (imagenId <= 0) {
            throw new InvalidCatalogRequestException("imagenId debe ser mayor que 0");
        }
    }

    private void validarImagenRequest(AutoImagenRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new InvalidCatalogRequestException("La solicitud de imagen es obligatoria");
        }
        if (requestDTO.getUrl() == null || requestDTO.getUrl().isBlank()) {
            throw new InvalidCatalogRequestException("url es obligatoria");
        }
        if (requestDTO.getOrden() == null) {
            throw new InvalidCatalogRequestException("orden es obligatorio");
        }
        if (requestDTO.getOrden() <= 0) {
            throw new InvalidCatalogRequestException("orden debe ser mayor que 0");
        }
    }
}
