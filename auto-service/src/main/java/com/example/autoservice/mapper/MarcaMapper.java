package com.example.autoservice.mapper;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;
import com.example.autoservice.model.Marca;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    MarcaResponseDTO toDTO(Marca marca);

    Marca toEntity(MarcaRequestDTO dto);
}