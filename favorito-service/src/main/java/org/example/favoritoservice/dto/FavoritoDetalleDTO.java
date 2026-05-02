package org.example.favoritoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoDetalleDTO {
    private Long id;
    private Long autoId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String listaNombre;
    private String nota;
    private AutoDTO auto;
}
