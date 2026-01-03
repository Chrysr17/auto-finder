package com.example.comparador_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparacionDTO {

    private String criterio;
    private List<AutoDTO> autosComparados;


}
