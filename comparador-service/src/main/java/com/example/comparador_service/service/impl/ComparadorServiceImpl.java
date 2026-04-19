package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.AutoComparadoDTO;
import com.example.comparador_service.dto.AutoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.exception.InvalidComparisonRequestException;
import com.example.comparador_service.exception.RelatedResourceNotFoundException;
import com.example.comparador_service.service.ComparadorService;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ComparadorServiceImpl implements ComparadorService {

    private static final String CRITERIO_GENERAL = "general";
    private static final String CRITERIO_PRECIO = "precio";
    private static final String CRITERIO_ANIO_FABRICACION = "aniofabricacion";
    private static final String CRITERIO_MARCA = "marca";
    private static final String CRITERIO_CATEGORIA = "categoria";
    private static final String CRITERIO_MOTOR = "motor";
    private static final String CRITERIO_CABALLOS_FUERZA = "caballosfuerza";
    private static final String CRITERIO_RENDIMIENTO = "rendimiento";
    private static final String CRITERIO_VELOCIDAD_MAXIMA = "velocidadmaxima";
    private static final String CRITERIO_PRECIO_SALIDA_ESTIMADO = "preciosalidaestimado";
    private static final String CRITERIO_PRECIO_REFERENCIA_ACTUAL = "precioreferenciaactual";

    private static final Set<String> CRITERIOS_PERMITIDOS = Set.of(
            CRITERIO_GENERAL,
            CRITERIO_PRECIO,
            CRITERIO_ANIO_FABRICACION,
            CRITERIO_MARCA,
            CRITERIO_CATEGORIA,
            CRITERIO_MOTOR,
            CRITERIO_CABALLOS_FUERZA,
            CRITERIO_RENDIMIENTO,
            CRITERIO_VELOCIDAD_MAXIMA,
            CRITERIO_PRECIO_SALIDA_ESTIMADO,
            CRITERIO_PRECIO_REFERENCIA_ACTUAL
    );

    private static final String MENSAJE_CRITERIOS = "criterio no soportado. Valores permitidos: "
            + "general, precio, anioFabricacion, marca, categoria, motor, caballosFuerza, rendimiento, "
            + "velocidadMaxima, precioSalidaEstimado, precioReferenciaActual";

    private final AutoClient autoClient;

    public ComparadorServiceImpl(AutoClient autoClient) {
        this.autoClient = autoClient;
    }

    @Override
    public ComparacionDTO compararAutos(List<Long> ids, String criterio) {
        validarSolicitud(ids, criterio);

        String criterioNormalizado = normalizarCriterio(criterio);
        List<AutoDTO> autos = new ArrayList<>(ids.stream()
                .map(this::obtenerAutoExistente)
                .toList());

        autos.sort(obtenerComparador(criterioNormalizado));

        List<AutoComparadoDTO> comparados = autos.stream()
                .map(this::mapearAutoComparado)
                .toList();

        List<ComparacionDTO.AtributoComparadoDTO> atributosComparados = construirAtributosComparados(autos, criterioNormalizado);

        return ComparacionDTO.builder()
                .criterio(criterioNormalizado)
                .tipoComparacion(esComparacionAvanzada(criterioNormalizado) ? "avanzada" : "simple")
                .resumen(construirResumen(autos, criterioNormalizado))
                .moneda(resolverMoneda(autos))
                .autosComparados(comparados)
                .atributosComparados(atributosComparados)
                .diferenciasClave(construirDiferenciasClave(atributosComparados))
                .ranking(construirRanking(autos, criterioNormalizado))
                .contextoValor(construirContextoValor(autos, criterioNormalizado))
                .build();
    }

    private AutoComparadoDTO mapearAutoComparado(AutoDTO auto) {
        return AutoComparadoDTO.builder()
                .id(auto.getId())
                .marcaNombre(auto.getMarcaNombre())
                .modeloNombre(auto.getModeloNombre())
                .precio(auto.getPrecio())
                .precioReferenciaActual(auto.getPrecioReferenciaActual())
                .precioSalidaEstimado(auto.getPrecioSalidaEstimado())
                .anioFabricacion(auto.getAnioFabricacion())
                .color(auto.getColor())
                .motor(auto.getMotor())
                .cilindradaCc(auto.getCilindradaCc())
                .caballosFuerza(auto.getCaballosFuerza())
                .torqueNm(auto.getTorqueNm())
                .consumoCiudad(auto.getConsumoCiudad())
                .consumoCarretera(auto.getConsumoCarretera())
                .velocidadMaxima(auto.getVelocidadMaxima())
                .aceleracionCeroACien(auto.getAceleracionCeroACien())
                .tipoCombustible(auto.getTipoCombustible())
                .transmision(auto.getTransmision())
                .traccion(auto.getTraccion())
                .pesoKg(auto.getPesoKg())
                .puertas(auto.getPuertas())
                .moneda(auto.getMoneda())
                .descripcionValor(auto.getDescripcionValor())
                .resumen(auto.getResumen())
                .categoriaNombre(auto.getCategoriaNombre())
                .fortalezas(construirFortalezas(auto))
                .alertas(construirAlertas(auto))
                .imagenPortadaUrl(auto.getImagenPortadaUrl())
                .build();
    }

    private List<String> construirFortalezas(AutoDTO auto) {
        List<String> fortalezas = new ArrayList<>();
        if (auto.getCaballosFuerza() != null && auto.getCaballosFuerza() >= 300) {
            fortalezas.add("Potencia alta para comparacion deportiva");
        }
        if (auto.getVelocidadMaxima() != null && auto.getVelocidadMaxima() >= 240) {
            fortalezas.add("Velocidad maxima destacada");
        }
        if (calcularRendimientoPromedio(auto) != null && calcularRendimientoPromedio(auto) <= 8.0) {
            fortalezas.add("Consumo contenido para su segmento");
        }
        if (auto.getPrecioReferenciaActual() != null && auto.getPrecioSalidaEstimado() != null
                && auto.getPrecioReferenciaActual() > auto.getPrecioSalidaEstimado()) {
            fortalezas.add("Valor actual por encima del precio de salida estimado");
        }
        return fortalezas.isEmpty() ? null : fortalezas;
    }

    private List<String> construirAlertas(AutoDTO auto) {
        List<String> alertas = new ArrayList<>();
        if (auto.getDescripcionValor() != null && !auto.getDescripcionValor().isBlank()) {
            alertas.add(auto.getDescripcionValor());
        }
        Double rendimiento = calcularRendimientoPromedio(auto);
        if (rendimiento != null && rendimiento > 14.0) {
            alertas.add("Consumo elevado frente a otras opciones");
        }
        if (auto.getPuertas() != null && auto.getPuertas() <= 2) {
            alertas.add("Configuracion menos practica para uso familiar");
        }
        return alertas.isEmpty() ? null : alertas;
    }

    private List<ComparacionDTO.AtributoComparadoDTO> construirAtributosComparados(List<AutoDTO> autos, String criterio) {
        Map<String, ComparacionDTO.AtributoComparadoDTO> atributos = new LinkedHashMap<>();

        if (CRITERIO_GENERAL.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_PRECIO, "Precio de lista", "moneda", true, true, null,
                    AutoDTO::getPrecio, this::formatearMoneda, false);
            agregarAtributo(atributos, autos, CRITERIO_PRECIO_REFERENCIA_ACTUAL, "Precio actual aproximado",
                    "moneda", true, true, null, AutoDTO::getPrecioReferenciaActual, this::formatearMoneda, false);
            agregarAtributo(atributos, autos, CRITERIO_CABALLOS_FUERZA, "Caballos de fuerza", "numero", true, true, "hp",
                    AutoDTO::getCaballosFuerza, valor -> formatearNumeroConUnidad(valor, "hp"), true);
            agregarAtributo(atributos, autos, CRITERIO_RENDIMIENTO, "Rendimiento promedio", "decimal", true, true,
                    "L/100 km", this::calcularRendimientoPromedio, valor -> formatearDecimalConUnidad(valor, "L/100 km"), false);
            agregarAtributo(atributos, autos, CRITERIO_VELOCIDAD_MAXIMA, "Velocidad maxima", "numero", true, true,
                    "km/h", AutoDTO::getVelocidadMaxima, valor -> formatearNumeroConUnidad(valor, "km/h"), true);
            agregarAtributo(atributos, autos, CRITERIO_ANIO_FABRICACION, "Anio de fabricacion", "numero", false, true, null,
                    AutoDTO::getAnioFabricacion, this::formatearTexto, true);
            agregarAtributo(atributos, autos, CRITERIO_MOTOR, "Motorizacion", "texto", false, true, null,
                    AutoDTO::getMotor, this::formatearTexto, true);
        } else if (CRITERIO_PRECIO.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_PRECIO, "Precio de lista", "moneda", true, true, null,
                    AutoDTO::getPrecio, this::formatearMoneda, false);
        } else if (CRITERIO_ANIO_FABRICACION.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_ANIO_FABRICACION, "Anio de fabricacion", "numero", true, true, null,
                    AutoDTO::getAnioFabricacion, this::formatearTexto, true);
        } else if (CRITERIO_MARCA.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_MARCA, "Marca", "texto", true, true, null,
                    AutoDTO::getMarcaNombre, this::formatearTexto, true);
        } else if (CRITERIO_CATEGORIA.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_CATEGORIA, "Categoria", "texto", true, true, null,
                    AutoDTO::getCategoriaNombre, this::formatearTexto, true);
        } else if (CRITERIO_MOTOR.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_MOTOR, "Motorizacion", "texto", true, true, null,
                    AutoDTO::getMotor, this::formatearTexto, true);
            agregarAtributo(atributos, autos, "cilindrada", "Cilindrada", "numero", false, true, "cc",
                    AutoDTO::getCilindradaCc, valor -> formatearNumeroConUnidad(valor, "cc"), true);
        } else if (CRITERIO_CABALLOS_FUERZA.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_CABALLOS_FUERZA, "Caballos de fuerza", "numero", true, true, "hp",
                    AutoDTO::getCaballosFuerza, valor -> formatearNumeroConUnidad(valor, "hp"), true);
            agregarAtributo(atributos, autos, "torque", "Torque", "numero", false, true, "Nm",
                    AutoDTO::getTorqueNm, valor -> formatearNumeroConUnidad(valor, "Nm"), true);
        } else if (CRITERIO_RENDIMIENTO.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_RENDIMIENTO, "Rendimiento promedio", "decimal", true, true,
                    "L/100 km", this::calcularRendimientoPromedio, valor -> formatearDecimalConUnidad(valor, "L/100 km"), false);
            agregarAtributo(atributos, autos, "consumoCiudad", "Consumo en ciudad", "decimal", false, true, "L/100 km",
                    AutoDTO::getConsumoCiudad, valor -> formatearDecimalConUnidad(valor, "L/100 km"), false);
            agregarAtributo(atributos, autos, "consumoCarretera", "Consumo en carretera", "decimal", false, true, "L/100 km",
                    AutoDTO::getConsumoCarretera, valor -> formatearDecimalConUnidad(valor, "L/100 km"), false);
        } else if (CRITERIO_VELOCIDAD_MAXIMA.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_VELOCIDAD_MAXIMA, "Velocidad maxima", "numero", true, true,
                    "km/h", AutoDTO::getVelocidadMaxima, valor -> formatearNumeroConUnidad(valor, "km/h"), true);
            agregarAtributo(atributos, autos, "aceleracionCeroACien", "Aceleracion 0-100", "decimal", false, true, "s",
                    AutoDTO::getAceleracionCeroACien, valor -> formatearDecimalConUnidad(valor, "s"), false);
        } else if (CRITERIO_PRECIO_SALIDA_ESTIMADO.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_PRECIO_SALIDA_ESTIMADO, "Precio de salida estimado", "moneda",
                    true, true, null, AutoDTO::getPrecioSalidaEstimado, this::formatearMoneda, false);
        } else if (CRITERIO_PRECIO_REFERENCIA_ACTUAL.equals(criterio)) {
            agregarAtributo(atributos, autos, CRITERIO_PRECIO_REFERENCIA_ACTUAL, "Precio actual aproximado", "moneda",
                    true, true, null, AutoDTO::getPrecioReferenciaActual, this::formatearMoneda, false);
            agregarAtributo(atributos, autos, CRITERIO_PRECIO_SALIDA_ESTIMADO, "Precio de salida estimado", "moneda",
                    false, true, null, AutoDTO::getPrecioSalidaEstimado, this::formatearMoneda, false);
        }

        return new ArrayList<>(atributos.values());
    }

    private <T> void agregarAtributo(
            Map<String, ComparacionDTO.AtributoComparadoDTO> atributos,
            List<AutoDTO> autos,
            String clave,
            String etiqueta,
            String tipoDato,
            boolean destacable,
            boolean ordenable,
            String unidad,
            Function<AutoDTO, T> extractor,
            Function<T, String> formateador,
            boolean mayorEsMejor
    ) {
        List<T> valoresPresentes = autos.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .toList();

        if (valoresPresentes.isEmpty()) {
            return;
        }

        T mejorValor = resolverMejorValor(valoresPresentes, mayorEsMejor);
        long cantidadLideres = valoresPresentes.stream()
                .filter(valor -> Objects.equals(valor, mejorValor))
                .count();

        List<ComparacionDTO.ValorAtributoDTO> valores = autos.stream()
                .map(auto -> {
                    T valor = extractor.apply(auto);
                    boolean lider = valor != null && Objects.equals(valor, mejorValor);
                    return ComparacionDTO.ValorAtributoDTO.builder()
                            .autoId(auto.getId())
                            .marcaNombre(auto.getMarcaNombre())
                            .modeloNombre(auto.getModeloNombre())
                            .valor(valor)
                            .valorFormateado(valor == null ? "Sin dato" : formateador.apply(valor))
                            .lider(lider)
                            .empate(lider && cantidadLideres > 1)
                            .observacion(construirObservacionAtributo(clave, auto, valor, lider))
                            .build();
                })
                .toList();

        atributos.put(clave, ComparacionDTO.AtributoComparadoDTO.builder()
                .clave(clave)
                .etiqueta(etiqueta)
                .tipoDato(tipoDato)
                .destacable(destacable)
                .ordenable(ordenable)
                .unidad(unidad)
                .mejorValor(formateador.apply(mejorValor))
                .valores(valores)
                .build());
    }

    @SuppressWarnings("unchecked")
    private <T> T resolverMejorValor(List<T> valores, boolean mayorEsMejor) {
        Comparator<T> comparator;
        T sample = valores.get(0);

        if (sample instanceof String) {
            comparator = (Comparator<T>) String.CASE_INSENSITIVE_ORDER;
        } else if (sample instanceof Comparable<?>) {
            comparator = (left, right) -> ((Comparable<T>) left).compareTo(right);
        } else {
            throw new IllegalStateException("Tipo de atributo no comparable");
        }

        return mayorEsMejor
                ? valores.stream().max(comparator).orElse(null)
                : valores.stream().min(comparator).orElse(null);
    }

    private String construirObservacionAtributo(String clave, AutoDTO auto, Object valor, boolean lider) {
        if (valor == null) {
            return "No informado";
        }
        if (lider) {
            return switch (clave) {
                case CRITERIO_CABALLOS_FUERZA -> "Entrega la cifra mas alta de potencia";
                case CRITERIO_RENDIMIENTO -> "Registra el consumo mas eficiente del grupo";
                case CRITERIO_VELOCIDAD_MAXIMA -> "Marca la velocidad punta mas alta";
                case CRITERIO_PRECIO, CRITERIO_PRECIO_SALIDA_ESTIMADO, CRITERIO_PRECIO_REFERENCIA_ACTUAL ->
                        "Es la referencia economica mas baja entre los comparados";
                default -> "Encabeza este atributo en la comparacion";
            };
        }
        if (CRITERIO_PRECIO_REFERENCIA_ACTUAL.equals(clave) && auto.getDescripcionValor() != null && !auto.getDescripcionValor().isBlank()) {
            return auto.getDescripcionValor();
        }
        return null;
    }

    private List<ComparacionDTO.DiferenciaClaveDTO> construirDiferenciasClave(List<ComparacionDTO.AtributoComparadoDTO> atributos) {
        return atributos.stream()
                .filter(atributo -> Boolean.TRUE.equals(atributo.getDestacable()))
                .map(this::crearDiferenciaClave)
                .filter(Objects::nonNull)
                .limit(3)
                .toList();
    }

    private ComparacionDTO.DiferenciaClaveDTO crearDiferenciaClave(ComparacionDTO.AtributoComparadoDTO atributo) {
        return atributo.getValores().stream()
                .filter(valor -> Boolean.TRUE.equals(valor.getLider()))
                .findFirst()
                .map(lider -> ComparacionDTO.DiferenciaClaveDTO.builder()
                        .atributo(atributo.getClave())
                        .etiqueta(atributo.getEtiqueta())
                        .descripcion("%s lidera en %s".formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()),
                                atributo.getEtiqueta().toLowerCase(Locale.ROOT)))
                        .autoIdGanador(lider.getAutoId())
                        .valorGanador(lider.getValorFormateado())
                        .impacto(lider.getObservacion())
                        .build())
                .orElse(null);
    }

    private List<ComparacionDTO.RankingComparacionDTO> construirRanking(List<AutoDTO> autos, String criterio) {
        Comparator<AutoDTO> comparador = obtenerComparador(criterio);
        List<AutoDTO> rankingBase = autos.stream()
                .sorted(comparador)
                .toList();

        return construirRankingSegunOrden(rankingBase, criterio);
    }

    private List<ComparacionDTO.RankingComparacionDTO> construirRankingSegunOrden(List<AutoDTO> autosOrdenados, String criterio) {
        List<ComparacionDTO.RankingComparacionDTO> ranking = new ArrayList<>();
        int posicion = 1;
        for (AutoDTO auto : autosOrdenados) {
            ranking.add(ComparacionDTO.RankingComparacionDTO.builder()
                    .posicion(posicion++)
                    .autoId(auto.getId())
                    .marcaNombre(auto.getMarcaNombre())
                    .modeloNombre(auto.getModeloNombre())
                    .criterio(criterio)
                    .motivo(construirMotivoRanking(auto, criterio))
                    .puntaje(calcularPuntaje(auto, criterio))
                    .build());
        }
        return ranking;
    }

    private String construirMotivoRanking(AutoDTO auto, String criterio) {
        return switch (criterio) {
            case CRITERIO_PRECIO -> "Ordenado por precio de lista ascendente";
            case CRITERIO_ANIO_FABRICACION -> "Ordenado por anio de fabricacion ascendente";
            case CRITERIO_MARCA -> "Orden alfabetico por marca";
            case CRITERIO_CATEGORIA -> "Orden alfabetico por categoria";
            case CRITERIO_MOTOR -> "Orden alfabetico por motorizacion";
            case CRITERIO_CABALLOS_FUERZA -> "Ordenado por potencia descendente";
            case CRITERIO_RENDIMIENTO -> "Ordenado por consumo promedio ascendente";
            case CRITERIO_VELOCIDAD_MAXIMA -> "Ordenado por velocidad maxima descendente";
            case CRITERIO_PRECIO_SALIDA_ESTIMADO -> "Ordenado por precio de salida estimado ascendente";
            case CRITERIO_PRECIO_REFERENCIA_ACTUAL -> "Ordenado por precio actual aproximado ascendente";
            default -> "Vista general con datos tecnicos y de valor";
        };
    }

    private Double calcularPuntaje(AutoDTO auto, String criterio) {
        return switch (criterio) {
            case CRITERIO_PRECIO -> auto.getPrecio();
            case CRITERIO_ANIO_FABRICACION -> auto.getAnioFabricacion() == null ? null : auto.getAnioFabricacion().doubleValue();
            case CRITERIO_CABALLOS_FUERZA -> auto.getCaballosFuerza() == null ? null : auto.getCaballosFuerza().doubleValue();
            case CRITERIO_RENDIMIENTO -> calcularRendimientoPromedio(auto);
            case CRITERIO_VELOCIDAD_MAXIMA -> auto.getVelocidadMaxima() == null ? null : auto.getVelocidadMaxima().doubleValue();
            case CRITERIO_PRECIO_SALIDA_ESTIMADO -> auto.getPrecioSalidaEstimado();
            case CRITERIO_PRECIO_REFERENCIA_ACTUAL -> auto.getPrecioReferenciaActual();
            default -> null;
        };
    }

    private ComparacionDTO.ContextoValorDTO construirContextoValor(List<AutoDTO> autos, String criterio) {
        boolean tieneContextoValor = autos.stream().anyMatch(auto ->
                auto.getPrecioSalidaEstimado() != null
                        || auto.getPrecioReferenciaActual() != null
                        || (auto.getDescripcionValor() != null && !auto.getDescripcionValor().isBlank()));

        if (!tieneContextoValor) {
            return null;
        }

        String descripcion = autos.stream()
                .map(AutoDTO::getDescripcionValor)
                .filter(texto -> texto != null && !texto.isBlank())
                .findFirst()
                .orElse("La comparacion distingue entre precio historico de salida y valor actual aproximado.");

        String nota = CRITERIO_PRECIO.equals(criterio)
                ? "El precio de lista puede no reflejar el valor historico o coleccionable."
                : "Usa el precio actual aproximado junto al precio de salida estimado para entender mejor la evolucion de valor.";

        return ComparacionDTO.ContextoValorDTO.builder()
                .descripcionGeneral(descripcion)
                .criterioPrecioPrincipal("precioReferenciaActual")
                .criterioPrecioSecundario("precioSalidaEstimado")
                .nota(nota)
                .build();
    }

    private String construirResumen(List<AutoDTO> autos, String criterio) {
        AutoDTO lider = autos.isEmpty() ? null : autos.get(0);
        if (lider == null) {
            return null;
        }

        return switch (criterio) {
            case CRITERIO_PRECIO -> "%s abre la comparacion con el precio de lista mas bajo."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            case CRITERIO_CABALLOS_FUERZA -> "%s encabeza la comparacion de potencia."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            case CRITERIO_RENDIMIENTO -> "%s ofrece el mejor rendimiento promedio del grupo."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            case CRITERIO_VELOCIDAD_MAXIMA -> "%s lidera por velocidad maxima."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            case CRITERIO_PRECIO_REFERENCIA_ACTUAL -> "%s aparece como la opcion de menor valor actual aproximado."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            case CRITERIO_PRECIO_SALIDA_ESTIMADO -> "%s muestra la referencia de salida estimada mas baja."
                    .formatted(nombreCompleto(lider.getMarcaNombre(), lider.getModeloNombre()));
            default -> "Comparacion enriquecida de %d autos con foco en ficha tecnica y contexto de valor."
                    .formatted(autos.size());
        };
    }

    private String resolverMoneda(List<AutoDTO> autos) {
        return autos.stream()
                .map(AutoDTO::getMoneda)
                .filter(moneda -> moneda != null && !moneda.isBlank())
                .findFirst()
                .orElse(null);
    }

    private boolean esComparacionAvanzada(String criterio) {
        return !Set.of(CRITERIO_GENERAL, CRITERIO_PRECIO, CRITERIO_ANIO_FABRICACION, CRITERIO_MARCA).contains(criterio);
    }

    private Comparator<AutoDTO> obtenerComparador(String criterio) {
        return switch (criterio) {
            case CRITERIO_PRECIO -> Comparator.comparing(AutoDTO::getPrecio,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case CRITERIO_ANIO_FABRICACION -> Comparator.comparing(AutoDTO::getAnioFabricacion,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case CRITERIO_MARCA -> Comparator.comparing(AutoDTO::getMarcaNombre,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case CRITERIO_CATEGORIA -> Comparator.comparing(AutoDTO::getCategoriaNombre,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case CRITERIO_MOTOR -> Comparator.comparing(AutoDTO::getMotor,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case CRITERIO_CABALLOS_FUERZA -> Comparator.comparing(AutoDTO::getCaballosFuerza,
                            Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(AutoDTO::getTorqueNm, Comparator.nullsLast(Comparator.reverseOrder()));
            case CRITERIO_RENDIMIENTO -> Comparator.comparing(this::calcularRendimientoPromedio,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case CRITERIO_VELOCIDAD_MAXIMA -> Comparator.comparing(AutoDTO::getVelocidadMaxima,
                            Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(AutoDTO::getAceleracionCeroACien, Comparator.nullsLast(Comparator.naturalOrder()));
            case CRITERIO_PRECIO_SALIDA_ESTIMADO -> Comparator.comparing(AutoDTO::getPrecioSalidaEstimado,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case CRITERIO_PRECIO_REFERENCIA_ACTUAL -> Comparator.comparing(AutoDTO::getPrecioReferenciaActual,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(AutoDTO::getId, Comparator.nullsLast(Comparator.naturalOrder()));
        };
    }

    private Double calcularRendimientoPromedio(AutoDTO auto) {
        List<Double> consumos = new ArrayList<>();
        if (auto.getConsumoCiudad() != null) {
            consumos.add(auto.getConsumoCiudad());
        }
        if (auto.getConsumoCarretera() != null) {
            consumos.add(auto.getConsumoCarretera());
        }
        if (consumos.isEmpty()) {
            return null;
        }
        return consumos.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private String nombreCompleto(String marca, String modelo) {
        return StreamSupport.join(marca, modelo);
    }

    private String formatearMoneda(Double valor) {
        return valor == null ? "Sin dato" : String.format(Locale.US, "%.2f", valor);
    }

    private String formatearNumeroConUnidad(Number valor, String unidad) {
        return valor == null ? "Sin dato" : "%s %s".formatted(valor, unidad);
    }

    private String formatearDecimalConUnidad(Double valor, String unidad) {
        return valor == null ? "Sin dato" : String.format(Locale.US, "%.1f %s", valor, unidad);
    }

    private String formatearTexto(Object valor) {
        return valor == null ? "Sin dato" : String.valueOf(valor);
    }

    private AutoDTO obtenerAutoExistente(Long autoId) {
        try {
            AutoDTO auto = autoClient.obtenerAuto(autoId);
            if (auto == null || auto.getId() == null) {
                throw new RelatedResourceNotFoundException("No se encontro el auto con id: " + autoId);
            }
            return auto;
        } catch (FeignException.NotFound ex) {
            throw new RelatedResourceNotFoundException("No se encontro el auto con id: " + autoId);
        }
    }

    private void validarSolicitud(List<Long> ids, String criterio) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidComparisonRequestException("Debe enviar al menos un auto para comparar");
        }

        if (ids.size() < 2) {
            throw new InvalidComparisonRequestException("Debe enviar al menos dos autos para comparar");
        }

        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new InvalidComparisonRequestException("Todos los ids deben ser mayores que 0");
        }

        if (ids.stream().distinct().count() != ids.size()) {
            throw new InvalidComparisonRequestException("No se pueden comparar autos duplicados");
        }

        String criterioNormalizado = normalizarCriterio(criterio);
        if (!CRITERIOS_PERMITIDOS.contains(criterioNormalizado)) {
            throw new InvalidComparisonRequestException(MENSAJE_CRITERIOS);
        }
    }

    private String normalizarCriterio(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return CRITERIO_GENERAL;
        }

        String normalizado = criterio.trim().toLowerCase(Locale.ROOT);
        return switch (normalizado) {
            case "anio", "aniofabricacion" -> CRITERIO_ANIO_FABRICACION;
            case "hp", "caballosfuerza" -> CRITERIO_CABALLOS_FUERZA;
            case "velocidadmaxima" -> CRITERIO_VELOCIDAD_MAXIMA;
            case "preciosalidaestimado" -> CRITERIO_PRECIO_SALIDA_ESTIMADO;
            case "precioactualaproximado", "precioreferenciaactual" -> CRITERIO_PRECIO_REFERENCIA_ACTUAL;
            default -> normalizado;
        };
    }

    private static final class StreamSupport {
        private StreamSupport() {
        }

        private static String join(String... values) {
            return Arrays.stream(values)
                    .filter(value -> value != null && !value.isBlank())
                    .collect(Collectors.joining(" "));
        }
    }
}
