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
public class FavoritoListaDTO {
    private String nombre;
    private long totalFavoritos;
    private LocalDateTime ultimaActualizacion;
}
