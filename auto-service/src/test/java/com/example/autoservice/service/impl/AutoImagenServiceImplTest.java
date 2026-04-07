package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoImagenRequestDTO;
import com.example.autoservice.dto.AutoImagenResponseDTO;
import com.example.autoservice.exception.InvalidCatalogRequestException;
import com.example.autoservice.exception.ResourceConflictException;
import com.example.autoservice.exception.ResourceNotFoundException;
import com.example.autoservice.mapper.AutoImagenMapper;
import com.example.autoservice.model.Auto;
import com.example.autoservice.model.AutoImagen;
import com.example.autoservice.repository.AutoImagenRepository;
import com.example.autoservice.repository.AutoRepositoy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutoImagenServiceImplTest {

    @Mock
    private AutoRepositoy autoRepositoy;

    @Mock
    private AutoImagenRepository autoImagenRepository;

    @Mock
    private AutoImagenMapper autoImagenMapper;

    @InjectMocks
    private AutoImagenServiceImpl autoImagenService;

    @Test
    void agregarImagen_deberiaGuardarYRetornarDto() {
        Long autoId = 1L;
        Auto auto = Auto.builder().id(autoId).build();
        AutoImagenRequestDTO requestDTO = AutoImagenRequestDTO.builder()
                .url("https://cdn.test/auto-1.jpg")
                .orden(2)
                .build();
        AutoImagen imagen = AutoImagen.builder()
                .url(requestDTO.getUrl())
                .orden(requestDTO.getOrden())
                .build();
        AutoImagen guardada = AutoImagen.builder()
                .id(10L)
                .url(requestDTO.getUrl())
                .orden(requestDTO.getOrden())
                .auto(auto)
                .build();
        AutoImagenResponseDTO responseDTO = AutoImagenResponseDTO.builder()
                .id(10L)
                .url(requestDTO.getUrl())
                .orden(requestDTO.getOrden())
                .build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(auto));
        when(autoImagenRepository.existsByAutoIdAndOrden(autoId, requestDTO.getOrden())).thenReturn(false);
        when(autoImagenMapper.toEntity(requestDTO)).thenReturn(imagen);
        when(autoImagenRepository.save(imagen)).thenReturn(guardada);
        when(autoImagenMapper.toResponseDTO(guardada)).thenReturn(responseDTO);

        AutoImagenResponseDTO resultado = autoImagenService.agregarImagen(autoId, requestDTO);

        assertEquals(10L, resultado.getId());
        assertEquals("https://cdn.test/auto-1.jpg", resultado.getUrl());
        assertEquals(2, resultado.getOrden());
        assertEquals(auto, imagen.getAuto());
        verify(autoImagenRepository).save(imagen);
    }

    @Test
    void agregarImagen_deberiaLanzarExcepcionSiAutoNoExiste() {
        Long autoId = 99L;
        AutoImagenRequestDTO requestDTO = AutoImagenRequestDTO.builder()
                .url("https://cdn.test/no-existe.jpg")
                .orden(1)
                .build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> autoImagenService.agregarImagen(autoId, requestDTO));

        assertEquals("Auto no encontrado con id: 99", exception.getMessage());
        verify(autoImagenRepository, never()).save(any(AutoImagen.class));
    }

    @Test
    void agregarImagen_deberiaLanzarConflictoSiOrdenYaExiste() {
        Long autoId = 1L;
        Auto auto = Auto.builder().id(autoId).build();
        AutoImagenRequestDTO requestDTO = AutoImagenRequestDTO.builder()
                .url("https://cdn.test/auto-1.jpg")
                .orden(1)
                .build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(auto));
        when(autoImagenRepository.existsByAutoIdAndOrden(autoId, 1)).thenReturn(true);

        ResourceConflictException exception = assertThrows(ResourceConflictException.class,
                () -> autoImagenService.agregarImagen(autoId, requestDTO));

        assertEquals("Ya existe una imagen con el orden: 1", exception.getMessage());
        verify(autoImagenRepository, never()).save(any(AutoImagen.class));
    }

    @Test
    void listarImagenes_deberiaRetornarDtosOrdenados() {
        Long autoId = 1L;
        AutoImagen imagen1 = AutoImagen.builder().id(1L).url("url-1").orden(1).build();
        AutoImagen imagen2 = AutoImagen.builder().id(2L).url("url-2").orden(2).build();
        AutoImagenResponseDTO response1 = AutoImagenResponseDTO.builder().id(1L).url("url-1").orden(1).build();
        AutoImagenResponseDTO response2 = AutoImagenResponseDTO.builder().id(2L).url("url-2").orden(2).build();

        when(autoRepositoy.existsById(autoId)).thenReturn(true);
        when(autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)).thenReturn(List.of(imagen1, imagen2));
        when(autoImagenMapper.toResponseDTO(imagen1)).thenReturn(response1);
        when(autoImagenMapper.toResponseDTO(imagen2)).thenReturn(response2);

        List<AutoImagenResponseDTO> resultado = autoImagenService.listarImagenes(autoId);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getOrden());
        assertEquals("url-1", resultado.get(0).getUrl());
        assertEquals(2, resultado.get(1).getOrden());
    }

    @Test
    void editarImagen_deberiaActualizarYRetornarDto() {
        Long imagenId = 5L;
        Auto auto = Auto.builder().id(1L).build();
        AutoImagen existente = AutoImagen.builder()
                .id(imagenId)
                .url("anterior.jpg")
                .orden(2)
                .auto(auto)
                .build();
        AutoImagenRequestDTO requestDTO = AutoImagenRequestDTO.builder()
                .url("nueva.jpg")
                .orden(3)
                .build();
        AutoImagenResponseDTO responseDTO = AutoImagenResponseDTO.builder()
                .id(imagenId)
                .url("nueva.jpg")
                .orden(3)
                .build();

        when(autoImagenRepository.findById(imagenId)).thenReturn(Optional.of(existente));
        when(autoImagenRepository.existsByAutoIdAndOrden(auto.getId(), 3)).thenReturn(false);
        when(autoImagenRepository.save(existente)).thenReturn(existente);
        when(autoImagenMapper.toResponseDTO(existente)).thenReturn(responseDTO);

        AutoImagenResponseDTO resultado = autoImagenService.editarImagen(imagenId, requestDTO);

        assertEquals("nueva.jpg", resultado.getUrl());
        assertEquals(3, resultado.getOrden());
        assertEquals("nueva.jpg", existente.getUrl());
        assertEquals(3, existente.getOrden());
    }

    @Test
    void eliminarImagen_deberiaEliminarPorId() {
        Long imagenId = 7L;
        AutoImagen imagen = AutoImagen.builder().id(imagenId).build();

        when(autoImagenRepository.findById(imagenId)).thenReturn(Optional.of(imagen));

        autoImagenService.eliminarImagen(imagenId);

        verify(autoImagenRepository).deleteById(imagenId);
    }

    @Test
    void establecerComoPortada_deberiaIntercambiarOrdenEntreImagenes() {
        Long autoId = 1L;
        Long imagenId = 20L;
        Auto auto = Auto.builder().id(autoId).build();
        AutoImagen portadaActual = AutoImagen.builder().id(10L).url("portada.jpg").orden(1).auto(auto).build();
        AutoImagen nuevaPortada = AutoImagen.builder().id(imagenId).url("lateral.jpg").orden(3).auto(auto).build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(auto));
        when(autoImagenRepository.findById(imagenId)).thenReturn(Optional.of(nuevaPortada));
        when(autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)).thenReturn(List.of(portadaActual, nuevaPortada));
        when(autoImagenRepository.save(any(AutoImagen.class))).thenAnswer(invocation -> invocation.getArgument(0));

        autoImagenService.establecerComoPortada(autoId, imagenId);

        assertEquals(1, nuevaPortada.getOrden());
        assertEquals(3, portadaActual.getOrden());
        verify(autoImagenRepository).save(nuevaPortada);
        verify(autoImagenRepository).save(portadaActual);
    }

    @Test
    void establecerComoPortada_deberiaLanzarExcepcionSiImagenNoPerteneceAlAuto() {
        Long autoId = 1L;
        Long imagenId = 20L;
        Auto auto = Auto.builder().id(autoId).build();
        Auto otroAuto = Auto.builder().id(2L).build();
        AutoImagen imagen = AutoImagen.builder().id(imagenId).orden(2).auto(otroAuto).build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(auto));
        when(autoImagenRepository.findById(imagenId)).thenReturn(Optional.of(imagen));

        InvalidCatalogRequestException exception = assertThrows(InvalidCatalogRequestException.class,
                () -> autoImagenService.establecerComoPortada(autoId, imagenId));

        assertEquals("La imagen no pertenece a este auto", exception.getMessage());
        verify(autoImagenRepository, never()).save(any(AutoImagen.class));
    }

    @Test
    void obtenerPortada_deberiaRetornarUrlDeImagenConOrdenUno() {
        Long autoId = 1L;
        Auto auto = Auto.builder().id(autoId).build();
        AutoImagen imagenSecundaria = AutoImagen.builder().id(1L).url("secundaria.jpg").orden(2).auto(auto).build();
        AutoImagen portada = AutoImagen.builder().id(2L).url("portada.jpg").orden(1).auto(auto).build();

        when(autoRepositoy.existsById(autoId)).thenReturn(true);
        when(autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)).thenReturn(List.of(imagenSecundaria, portada));

        String resultado = autoImagenService.obtenerPortada(autoId);

        assertEquals("portada.jpg", resultado);
    }

    @Test
    void obtenerPortada_deberiaRetornarNullSiNoExistePortada() {
        Long autoId = 1L;
        Auto auto = Auto.builder().id(autoId).build();
        AutoImagen imagen = AutoImagen.builder().id(1L).url("secundaria.jpg").orden(2).auto(auto).build();

        when(autoRepositoy.existsById(autoId)).thenReturn(true);
        when(autoImagenRepository.findByAutoIdOrderByOrdenAsc(autoId)).thenReturn(List.of(imagen));

        String resultado = autoImagenService.obtenerPortada(autoId);

        assertNull(resultado);
    }

    @Test
    void editarImagen_deberiaLanzarExcepcionSiOrdenEsInvalido() {
        InvalidCatalogRequestException exception = assertThrows(InvalidCatalogRequestException.class,
                () -> autoImagenService.editarImagen(1L, AutoImagenRequestDTO.builder().url("nueva.jpg").orden(0).build()));

        assertEquals("orden debe ser mayor que 0", exception.getMessage());
        verify(autoImagenRepository, never()).findById(any());
    }
}
