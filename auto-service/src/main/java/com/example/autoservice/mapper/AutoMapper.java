package com.example.autoservice.mapper;

import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.model.Auto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapper {

    AutoResponseDTO toResponseDTO(Auto auto);

    Auto toEntity(AutoRequestDTO dto);
}
