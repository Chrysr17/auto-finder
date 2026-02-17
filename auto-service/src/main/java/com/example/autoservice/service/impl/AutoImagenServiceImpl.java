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
        Auto auto = autoRepositoy.findById(autoId)
                .orElseThrow(()-> new RuntimeException("Auto no encontrado con id: " + autoId));

        AutoImagen nuevaPortada = autoImagenRepository.findById(imagenid)
                .orElseThrow(()-> new RuntimeException("Imagen no encontrada con id: " + imagenid));

        if (!nuevaPortada.getAuto().getId().equals(auto.getId())){
            throw new RuntimeException("La imagen no pertenece a este auto");
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

        if (!autoRepositoy.existsById(autoId)){
            throw new RuntimeException("Auto no encontrado con id: " + autoId);
        }
        return autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)
                .stream()
                .filter(img -> img.getOrden() != null && img.getOrden() == 1)
                .map(AutoImagen::getUrl)
                .findFirst()
                .orElse(null);
    }
}
