package com.example.autoservice.mapper;

import com.example.autoservice.dto.MarcaDTO;
import com.example.autoservice.model.Marca;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    MarcaDTO toDTO(Marca marca);

    Marca toEntity(MarcaDTO dto);
}