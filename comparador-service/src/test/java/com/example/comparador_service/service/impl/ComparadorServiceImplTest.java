package com.example.comparador_service.service.impl;

import com.example.comparador_service.client.AutoClient;
import com.example.comparador_service.dto.AutoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.exception.InvalidComparisonRequestException;
import com.example.comparador_service.exception.RelatedResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComparadorServiceImplTest {

    @Mock
    private AutoClient autoClient;

    @InjectMocks
    private ComparadorServiceImpl comparadorService;

    @Test
    void compararAutos_deberiaRetornarComparacionGeneralEnriquecida() {
        AutoDTO auto1 = crearAutoBase(1L, "Toyota", "Corolla", "Sedan", 22000.0, 26000.0, 24000.0,
                2022, 180, 10.0, 7.0, 230, "2.0L", "USD", "El valor actual refleja alta demanda", "Balanceado");
        AutoDTO auto2 = crearAutoBase(2L, "Honda", "Civic", "Sedan", 18000.0, 21000.0, 20000.0,
                2020, 160, 9.0, 6.0, 210, "1.5 Turbo", "USD", null, "Eficiente");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), null);

        assertEquals("general", resultado.getCriterio());
        assertEquals("simple", resultado.getTipoComparacion());
        assertEquals("USD", resultado.getMoneda());
        assertEquals(2, resultado.getAutosComparados().size());
        assertEquals(1L, resultado.getAutosComparados().get(0).getId());
        assertEquals(180, resultado.getAutosComparados().get(0).getCaballosFuerza());
        assertNotNull(resultado.getAutosComparados().get(0).getFortalezas());
        assertNotNull(resultado.getAtributosComparados());
        assertEquals("precio", resultado.getAtributosComparados().get(0).getClave());
        assertNotNull(resultado.getDiferenciasClave());
        assertNotNull(resultado.getContextoValor());
        assertNotNull(resultado.getRanking());
        assertEquals(1, resultado.getRanking().get(0).getPosicion());
    }

    @Test
    void compararAutos_deberiaOrdenarPorHpYGenerarRankingAvanzado() {
        AutoDTO auto1 = crearAutoBase(1L, "Toyota", "Supra", "Coupe", 55000.0, 62000.0, 58000.0,
                2021, 382, 13.0, 9.0, 250, "3.0 Turbo", "USD", null, "Deportivo");
        AutoDTO auto2 = crearAutoBase(2L, "Audi", "TT", "Coupe", 50000.0, 54000.0, 52000.0,
                2020, 228, 11.0, 8.0, 250, "2.0 TFSI", "USD", null, "Compacto");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), "hp");

        assertEquals("hp", resultado.getCriterio());
        assertEquals("avanzada", resultado.getTipoComparacion());
        assertEquals(1L, resultado.getAutosComparados().get(0).getId());
        assertEquals(382, resultado.getAutosComparados().get(0).getCaballosFuerza());
        assertEquals("hp", resultado.getAtributosComparados().get(0).getClave());
        assertEquals(1L, resultado.getDiferenciasClave().get(0).getAutoIdGanador());
        assertEquals(1L, resultado.getRanking().get(0).getAutoId());
        assertEquals("Ordenado por potencia descendente", resultado.getRanking().get(0).getMotivo());
    }

    @Test
    void compararAutos_deberiaOrdenarPorPrecioActualAproximado() {
        AutoDTO auto1 = crearAutoBase(1L, "Porsche", "944", "Coupe", 18000.0, 45000.0, 28000.0,
                1987, 163, 12.0, 8.0, 220, "2.5L", "USD", "El precio actual depende del estado original", "Clasico");
        AutoDTO auto2 = crearAutoBase(2L, "Mazda", "MX-5", "Convertible", 17000.0, 32000.0, 24000.0,
                1990, 116, 9.0, 7.0, 195, "1.6L", "USD", null, "Ligero");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), "precioActualAproximado");

        assertEquals("precioactualaproximado", resultado.getCriterio());
        assertEquals("avanzada", resultado.getTipoComparacion());
        assertEquals(2L, resultado.getAutosComparados().get(0).getId());
        assertEquals(32000.0, resultado.getAutosComparados().get(0).getPrecioReferenciaActual());
        assertEquals("precioactualaproximado", resultado.getAtributosComparados().get(0).getClave());
        assertEquals("precioSalidaEstimado", resultado.getContextoValor().getCriterioPrecioSecundario());
        assertEquals(2L, resultado.getRanking().get(0).getAutoId());
    }

    @Test
    void compararAutos_deberiaLanzarExcepcionSiNoHaySuficientesIds() {
        InvalidComparisonRequestException exception = assertThrows(InvalidComparisonRequestException.class,
                () -> comparadorService.compararAutos(List.of(1L), "general"));

        assertEquals("Debe enviar al menos dos autos para comparar", exception.getMessage());
        verify(autoClient, never()).obtenerAuto(1L);
    }

    @Test
    void compararAutos_deberiaLanzarExcepcionSiHayIdsDuplicados() {
        InvalidComparisonRequestException exception = assertThrows(InvalidComparisonRequestException.class,
                () -> comparadorService.compararAutos(List.of(1L, 1L), "general"));

        assertEquals("No se pueden comparar autos duplicados", exception.getMessage());
        verify(autoClient, never()).obtenerAuto(1L);
    }

    @Test
    void compararAutos_deberiaLanzarExcepcionSiCriterioNoEsSoportado() {
        InvalidComparisonRequestException exception = assertThrows(InvalidComparisonRequestException.class,
                () -> comparadorService.compararAutos(List.of(1L, 2L), "potencia"));

        assertEquals("criterio no soportado. Valores permitidos: general, precio, anio, marca, categoria, motor, hp, rendimiento, velocidadMaxima, precioSalidaEstimado, precioActualAproximado",
                exception.getMessage());
        verify(autoClient, never()).obtenerAuto(1L);
    }

    @Test
    void compararAutos_deberiaLanzarExcepcionSiAutoNoExiste() {
        when(autoClient.obtenerAuto(1L)).thenReturn(crearAutoBase(1L, "Toyota", "Corolla", "Sedan",
                22000.0, 26000.0, 24000.0, 2022, 180, 10.0, 7.0, 230, "2.0L", "USD", null, null));
        when(autoClient.obtenerAuto(2L)).thenReturn(null);

        RelatedResourceNotFoundException exception = assertThrows(RelatedResourceNotFoundException.class,
                () -> comparadorService.compararAutos(List.of(1L, 2L), "general"));

        assertEquals("No se encontro el auto con id: 2", exception.getMessage());
    }

    private AutoDTO crearAutoBase(
            Long id,
            String marcaNombre,
            String modeloNombre,
            String categoriaNombre,
            Double precio,
            Double precioReferenciaActual,
            Double precioSalidaEstimado,
            Integer anioFabricacion,
            Integer caballosFuerza,
            Double consumoCiudad,
            Double consumoCarretera,
            Integer velocidadMaxima,
            String motor,
            String moneda,
            String descripcionValor,
            String resumen
    ) {
        return AutoDTO.builder()
                .id(id)
                .color("Rojo")
                .precio(precio)
                .precioReferenciaActual(precioReferenciaActual)
                .precioSalidaEstimado(precioSalidaEstimado)
                .anioFabricacion(anioFabricacion)
                .motor(motor)
                .cilindradaCc(2000)
                .caballosFuerza(caballosFuerza)
                .torqueNm(caballosFuerza == null ? null : caballosFuerza * 2)
                .consumoCiudad(consumoCiudad)
                .consumoCarretera(consumoCarretera)
                .velocidadMaxima(velocidadMaxima)
                .aceleracionCeroACien(6.5)
                .tipoCombustible("Gasolina")
                .transmision("Manual")
                .traccion("Trasera")
                .pesoKg(1400)
                .puertas(2)
                .moneda(moneda)
                .descripcionValor(descripcionValor)
                .resumen(resumen)
                .marcaNombre(marcaNombre)
                .modeloNombre(modeloNombre)
                .categoriaNombre(categoriaNombre)
                .imagenPortadaUrl("img-" + id)
                .build();
    }
}
