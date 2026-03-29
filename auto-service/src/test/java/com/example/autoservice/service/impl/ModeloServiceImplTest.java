package com.example.autoservice.service.impl;

import com.example.autoservice.dto.ModeloRequestDTO;
import com.example.autoservice.dto.ModeloResponseDTO;
import com.example.autoservice.mapper.ModeloMapper;
import com.example.autoservice.model.Marca;
import com.example.autoservice.model.Modelo;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModeloServiceImplTest {

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModeloMapper modeloMapper;

    @InjectMocks
    private ModeloServiceImpl modeloService;

    @Test
    void listar_deberiaRetornarModelosMapeados() {
        Marca marca = Marca.builder().id(1L).nombre("Toyota").build();
        Modelo modelo = Modelo.builder()
                .id(1L)
                .nombre("Corolla")
                .marca(marca)
                .build();
        ModeloResponseDTO responseDTO = ModeloResponseDTO.builder()
                .id(1L)
                .nombre("Corolla")
                .marcaId(1L)
                .marcaNombre("Toyota")
                .build();

        when(modeloRepository.findAll()).thenReturn(List.of(modelo));
        when(modeloMapper.toDTO(modelo)).thenReturn(responseDTO);

        List<ModeloResponseDTO> resultado = modeloService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Corolla", resultado.get(0).getNombre());
        assertEquals("Toyota", resultado.get(0).getMarcaNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalConDtoSiExiste() {
        Long modeloId = 1L;
        Marca marca = Marca.builder().id(2L).nombre("Honda").build();
        Modelo modelo = Modelo.builder().id(modeloId).nombre("Civic").marca(marca).build();
        ModeloResponseDTO responseDTO = ModeloResponseDTO.builder()
                .id(modeloId)
                .nombre("Civic")
                .marcaId(2L)
                .marcaNombre("Honda")
                .build();

        when(modeloRepository.findById(modeloId)).thenReturn(Optional.of(modelo));
        when(modeloMapper.toDTO(modelo)).thenReturn(responseDTO);

        Optional<ModeloResponseDTO> resultado = modeloService.buscarPorId(modeloId);

        assertTrue(resultado.isPresent());
        assertEquals(modeloId, resultado.get().getId());
        assertEquals("Civic", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalVacioSiNoExiste() {
        Long modeloId = 99L;

        when(modeloRepository.findById(modeloId)).thenReturn(Optional.empty());

        Optional<ModeloResponseDTO> resultado = modeloService.buscarPorId(modeloId);

        assertFalse(resultado.isPresent());
    }

    @Test
    void registrar_deberiaGuardarModeloConMarcaYRetornarDto() {
        ModeloRequestDTO requestDTO = ModeloRequestDTO.builder()
                .nombre("Mazda 3")
                .marcaId(10L)
                .build();
        Modelo modelo = Modelo.builder()
                .nombre("Mazda 3")
                .build();
        Marca marca = Marca.builder().id(10L).nombre("Mazda").build();
        Modelo guardado = Modelo.builder()
                .id(1L)
                .nombre("Mazda 3")
                .marca(marca)
                .build();
        ModeloResponseDTO responseDTO = ModeloResponseDTO.builder()
                .id(1L)
                .nombre("Mazda 3")
                .marcaId(10L)
                .marcaNombre("Mazda")
                .build();

        when(modeloMapper.toEntity(requestDTO)).thenReturn(modelo);
        when(marcaRepository.findById(10L)).thenReturn(Optional.of(marca));
        when(modeloRepository.save(modelo)).thenReturn(guardado);
        when(modeloMapper.toDTO(guardado)).thenReturn(responseDTO);

        ModeloResponseDTO resultado = modeloService.registrar(requestDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Mazda 3", resultado.getNombre());
        assertEquals(marca, modelo.getMarca());
        verify(modeloRepository).save(modelo);
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiMarcaNoExiste() {
        ModeloRequestDTO requestDTO = ModeloRequestDTO.builder()
                .nombre("A3")
                .marcaId(99L)
                .build();
        Modelo modelo = Modelo.builder().nombre("A3").build();

        when(modeloMapper.toEntity(requestDTO)).thenReturn(modelo);
        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> modeloService.registrar(requestDTO));

        assertEquals("Marca no encontrada", exception.getMessage());
        verify(modeloRepository, never()).save(any(Modelo.class));
    }
}
