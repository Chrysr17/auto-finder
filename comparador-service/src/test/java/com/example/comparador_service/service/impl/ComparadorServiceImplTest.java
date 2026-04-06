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
    void compararAutos_deberiaRetornarComparacionGeneralSinOrdenEspecial() {
        AutoDTO auto1 = crearAuto(1L, "Rojo", 22000.0, 2022, "Toyota", "Corolla", "Sedan", "img-1");
        AutoDTO auto2 = crearAuto(2L, "Azul", 18000.0, 2020, "Honda", "Civic", "Sedan", "img-2");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), null);

        assertEquals(2, resultado.getAutosComparados().size());
        assertEquals("general", resultado.getCriterio());
        assertEquals(1L, resultado.getAutosComparados().get(0).getId());
        assertEquals(22000.0, resultado.getAutosComparados().get(0).getPrecio());
        assertEquals(2022, resultado.getAutosComparados().get(0).getAnioFabricacion());
        assertEquals("Rojo", resultado.getAutosComparados().get(0).getColor());
        assertEquals("Sedan", resultado.getAutosComparados().get(0).getCategoriaNombre());
        assertEquals("img-1", resultado.getAutosComparados().get(0).getImagenPortadaUrl());
    }

    @Test
    void compararAutos_deberiaOrdenarPorPrecioYMapearCamposEsperados() {
        AutoDTO auto1 = crearAuto(1L, "Rojo", 22000.0, 2022, "Toyota", "Corolla", "Sedan", "img-1");
        AutoDTO auto2 = crearAuto(2L, "Azul", 18000.0, 2020, "Honda", "Civic", "Hatchback", "img-2");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), "precio");

        assertEquals("precio", resultado.getCriterio());
        assertEquals(2L, resultado.getAutosComparados().get(0).getId());
        assertEquals(18000.0, resultado.getAutosComparados().get(0).getPrecio());
        assertEquals("Azul", resultado.getAutosComparados().get(0).getColor());
        assertEquals("Hatchback", resultado.getAutosComparados().get(0).getCategoriaNombre());
        assertEquals("img-2", resultado.getAutosComparados().get(0).getImagenPortadaUrl());
        assertNull(resultado.getAutosComparados().get(0).getAnioFabricacion());
    }

    @Test
    void compararAutos_deberiaOrdenarPorAnioYMapearCamposEsperados() {
        AutoDTO auto1 = crearAuto(1L, "Rojo", 22000.0, 2022, "Toyota", "Corolla", "Sedan", "img-1");
        AutoDTO auto2 = crearAuto(2L, "Azul", 18000.0, 2020, "Honda", "Civic", "Hatchback", "img-2");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), "anio");

        assertEquals("anio", resultado.getCriterio());
        assertEquals(2L, resultado.getAutosComparados().get(0).getId());
        assertEquals(2020, resultado.getAutosComparados().get(0).getAnioFabricacion());
        assertEquals("Azul", resultado.getAutosComparados().get(0).getColor());
        assertEquals("Hatchback", resultado.getAutosComparados().get(0).getCategoriaNombre());
        assertEquals("img-2", resultado.getAutosComparados().get(0).getImagenPortadaUrl());
        assertNull(resultado.getAutosComparados().get(0).getPrecio());
    }

    @Test
    void compararAutos_deberiaOrdenarPorMarcaIgnorandoMayusculasYMapearCamposEsperados() {
        AutoDTO auto1 = crearAuto(1L, "Rojo", 22000.0, 2022, "toyota", "Corolla", "Sedan", "img-1");
        AutoDTO auto2 = crearAuto(2L, "Azul", 18000.0, 2020, "Audi", "A3", "Hatchback", "img-2");

        when(autoClient.obtenerAuto(1L)).thenReturn(auto1);
        when(autoClient.obtenerAuto(2L)).thenReturn(auto2);

        ComparacionDTO resultado = comparadorService.compararAutos(List.of(1L, 2L), "MARCA");

        assertEquals("marca", resultado.getCriterio());
        assertEquals(2L, resultado.getAutosComparados().get(0).getId());
        assertEquals("Audi", resultado.getAutosComparados().get(0).getMarcaNombre());
        assertEquals(18000.0, resultado.getAutosComparados().get(0).getPrecio());
        assertEquals("Azul", resultado.getAutosComparados().get(0).getColor());
        assertEquals("Hatchback", resultado.getAutosComparados().get(0).getCategoriaNombre());
        assertEquals("img-2", resultado.getAutosComparados().get(0).getImagenPortadaUrl());
        assertNull(resultado.getAutosComparados().get(0).getAnioFabricacion());
        assertNotNull(resultado.getAutosComparados().get(0).getModeloNombre());
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

        assertEquals("criterio no soportado. Valores permitidos: general, precio, anio, marca", exception.getMessage());
        verify(autoClient, never()).obtenerAuto(1L);
    }

    @Test
    void compararAutos_deberiaLanzarExcepcionSiAutoNoExiste() {
        when(autoClient.obtenerAuto(1L)).thenReturn(crearAuto(1L, "Rojo", 22000.0, 2022, "Toyota", "Corolla", "Sedan", "img-1"));
        when(autoClient.obtenerAuto(2L)).thenReturn(null);

        RelatedResourceNotFoundException exception = assertThrows(RelatedResourceNotFoundException.class,
                () -> comparadorService.compararAutos(List.of(1L, 2L), "general"));

        assertEquals("No se encontro el auto con id: 2", exception.getMessage());
    }

    private AutoDTO crearAuto(
            Long id,
            String color,
            Double precio,
            Integer anioFabricacion,
            String marcaNombre,
            String modeloNombre,
            String categoriaNombre,
            String imagenPortadaUrl
    ) {
        return AutoDTO.builder()
                .id(id)
                .color(color)
                .precio(precio)
                .anioFabricacion(anioFabricacion)
                .marcaNombre(marcaNombre)
                .modeloNombre(modeloNombre)
                .categoriaNombre(categoriaNombre)
                .imagenPortadaUrl(imagenPortadaUrl)
                .build();
    }
}
