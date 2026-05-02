package org.example.favoritoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoSenalesDTO {
    private long totalFavoritos;
    private List<ConteoDTO> listas;
    private List<ConteoDTO> marcasPrincipales;
    private List<ConteoDTO> categoriasPrincipales;
    private List<Long> autoIds;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConteoDTO {
        private String nombre;
        private long total;
    }
}
