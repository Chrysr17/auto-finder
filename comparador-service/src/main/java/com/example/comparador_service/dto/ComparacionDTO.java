package com.example.comparador_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Respuesta enriquecida de comparación entre autos con ranking, diferencias clave y contexto de valor.")
public class ComparacionDTO {

    @Schema(description = "Criterio de comparación normalizado que se usó realmente en la respuesta.",
            example = "caballosfuerza")
    private String criterio;

    @Schema(description = "Nivel de comparación aplicado por el backend.", example = "avanzada")
    private String tipoComparacion;

    @Schema(description = "Resumen textual de la comparación generada.", example = "Toyota Supra encabeza la comparacion de potencia.")
    private String resumen;

    @Schema(description = "Moneda común detectada para los valores monetarios cuando aplica.", example = "USD")
    private String moneda;

    @ArraySchema(schema = @Schema(implementation = AutoComparadoDTO.class),
            arraySchema = @Schema(description = "Autos comparados con su ficha resumida y highlights."))
    private List<AutoComparadoDTO> autosComparados;

    @ArraySchema(schema = @Schema(implementation = AtributoComparadoDTO.class),
            arraySchema = @Schema(description = "Matriz de atributos comparados, incluyendo valor líder y observaciones por auto."))
    private List<AtributoComparadoDTO> atributosComparados;

    @ArraySchema(schema = @Schema(implementation = DiferenciaClaveDTO.class),
            arraySchema = @Schema(description = "Principales diferencias destacadas detectadas en la comparación."))
    private List<DiferenciaClaveDTO> diferenciasClave;

    @ArraySchema(schema = @Schema(implementation = RankingComparacionDTO.class),
            arraySchema = @Schema(description = "Ranking calculado según el criterio seleccionado."))
    private List<RankingComparacionDTO> ranking;

    @Schema(description = "Explicación adicional sobre el contexto histórico o de valor de la comparación.")
    private ContextoValorDTO contextoValor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Atributo individual incluido en la matriz comparativa.")
    public static class AtributoComparadoDTO {
        @Schema(description = "Clave técnica del atributo en la comparación.", example = "precioReferenciaActual")
        private String clave;

        @Schema(description = "Etiqueta legible para mostrar el atributo.", example = "Precio actual aproximado")
        private String etiqueta;

        @Schema(description = "Tipo de dato del atributo comparado.", example = "moneda")
        private String tipoDato;

        @Schema(description = "Indica si el atributo puede destacarse como diferencia importante.", example = "true")
        private Boolean destacable;

        @Schema(description = "Indica si el atributo participa en ordenamiento o ranking.", example = "true")
        private Boolean ordenable;

        @Schema(description = "Unidad del atributo cuando aplica.", example = "km/h")
        private String unidad;

        @Schema(description = "Valor formateado que lidera el atributo.", example = "280 km/h")
        private String mejorValor;

        @ArraySchema(schema = @Schema(implementation = ValorAtributoDTO.class),
                arraySchema = @Schema(description = "Valores del atributo para cada auto comparado."))
        private List<ValorAtributoDTO> valores;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Valor de un atributo comparado para un auto específico.")
    public static class ValorAtributoDTO {
        @Schema(description = "Id del auto al que pertenece este valor.", example = "1")
        private Long autoId;

        @Schema(description = "Marca del auto.", example = "Toyota")
        private String marcaNombre;

        @Schema(description = "Modelo del auto.", example = "Supra")
        private String modeloNombre;

        @Schema(description = "Valor crudo del atributo. Puede ser numérico o textual.")
        private Object valor;

        @Schema(description = "Valor ya formateado para presentación.", example = "382 hp")
        private String valorFormateado;

        @Schema(description = "Indica si este auto lidera el atributo.", example = "true")
        private Boolean lider;

        @Schema(description = "Indica si comparte liderazgo con otro auto.", example = "false")
        private Boolean empate;

        @Schema(description = "Comentario corto generado para este valor.", example = "Entrega la cifra mas alta de potencia")
        private String observacion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Diferencia relevante que resume una ventaja o contraste importante.")
    public static class DiferenciaClaveDTO {
        @Schema(description = "Clave del atributo destacado.", example = "caballosfuerza")
        private String atributo;

        @Schema(description = "Etiqueta legible del atributo destacado.", example = "Caballos de fuerza")
        private String etiqueta;

        @Schema(description = "Descripción resumida de la diferencia.", example = "Toyota Supra lidera en caballos de fuerza")
        private String descripcion;

        @Schema(description = "Id del auto ganador en este atributo.", example = "1")
        private Long autoIdGanador;

        @Schema(description = "Valor ganador formateado.", example = "382 hp")
        private String valorGanador;

        @Schema(description = "Impacto o lectura de esa diferencia.", example = "Entrega la cifra mas alta de potencia")
        private String impacto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Posición de un auto dentro del ranking calculado para el criterio actual.")
    public static class RankingComparacionDTO {
        @Schema(description = "Posición ordinal dentro del ranking.", example = "1")
        private Integer posicion;

        @Schema(description = "Id del auto rankeado.", example = "1")
        private Long autoId;

        @Schema(description = "Marca del auto rankeado.", example = "Toyota")
        private String marcaNombre;

        @Schema(description = "Modelo del auto rankeado.", example = "Supra")
        private String modeloNombre;

        @Schema(description = "Criterio usado para este ranking.", example = "caballosfuerza")
        private String criterio;

        @Schema(description = "Explicación del orden aplicado.", example = "Ordenado por potencia descendente")
        private String motivo;

        @Schema(description = "Puntaje o valor base usado para posicionar el auto cuando aplica.", example = "382")
        private Double puntaje;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Contexto adicional para interpretar valores monetarios o evolución histórica.")
    public static class ContextoValorDTO {
        @Schema(description = "Descripción general del contexto de valor.", example = "La comparacion distingue entre precio historico de salida y valor actual aproximado.")
        private String descripcionGeneral;

        @Schema(description = "Criterio principal de precio sugerido para interpretar la comparación.", example = "precioReferenciaActual")
        private String criterioPrecioPrincipal;

        @Schema(description = "Criterio secundario de apoyo para interpretar la comparación.", example = "precioSalidaEstimado")
        private String criterioPrecioSecundario;

        @Schema(description = "Nota orientativa para entender mejor el valor de mercado o coleccionable.",
                example = "Usa el precio actual aproximado junto al precio de salida estimado para entender mejor la evolucion de valor.")
        private String nota;
    }

}
