package com.example.comparador_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComparacionDTO {

    private String criterio;
    private String tipoComparacion;
    private String resumen;
    private String moneda;
    private List<AutoComparadoDTO> autosComparados;
    private List<AtributoComparadoDTO> atributosComparados;
    private List<DiferenciaClaveDTO> diferenciasClave;
    private List<RankingComparacionDTO> ranking;
    private ContextoValorDTO contextoValor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AtributoComparadoDTO {
        private String clave;
        private String etiqueta;
        private String tipoDato;
        private Boolean destacable;
        private Boolean ordenable;
        private String unidad;
        private String mejorValor;
        private List<ValorAtributoDTO> valores;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValorAtributoDTO {
        private Long autoId;
        private String marcaNombre;
        private String modeloNombre;
        private Object valor;
        private String valorFormateado;
        private Boolean lider;
        private Boolean empate;
        private String observacion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DiferenciaClaveDTO {
        private String atributo;
        private String etiqueta;
        private String descripcion;
        private Long autoIdGanador;
        private String valorGanador;
        private String impacto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RankingComparacionDTO {
        private Integer posicion;
        private Long autoId;
        private String marcaNombre;
        private String modeloNombre;
        private String criterio;
        private String motivo;
        private Double puntaje;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContextoValorDTO {
        private String descripcionGeneral;
        private String criterioPrecioPrincipal;
        private String criterioPrecioSecundario;
        private String nota;
    }

}
