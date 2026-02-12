package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.model.AutoImagen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoImagenMapper {
    AutoImagenResponseDTO toResponseDTO(AutoImagen autoImagen);
    AutoImagen toEntity(AutoImagenRequestDTO dto);
}
