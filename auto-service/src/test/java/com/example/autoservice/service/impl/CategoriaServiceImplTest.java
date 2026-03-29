package com.example.autoservice.service.impl;

import com.example.autoservice.dto.CategoriaRequestDTO;
import com.example.autoservice.dto.CategoriaResponseDTO;
import com.example.autoservice.mapper.CategoriaMapper;
import com.example.autoservice.model.Categoria;
import com.example.autoservice.repository.CategoriaRepository;
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
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void listar_deberiaRetornarCategoriasMapeadas() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("SUV")
                .descripcion("Vehiculos deportivos utilitarios")
                .build();
        CategoriaResponseDTO responseDTO = CategoriaResponseDTO.builder()
                .id(1L)
                .nombre("SUV")
                .descripcion("Vehiculos deportivos utilitarios")
                .build();

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(categoriaMapper.toResponseDTO(categoria)).thenReturn(responseDTO);

        List<CategoriaResponseDTO> resultado = categoriaService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("SUV", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalConDtoSiExiste() {
        Long categoriaId = 1L;
        Categoria categoria = Categoria.builder().id(categoriaId).nombre("Sedan").build();
        CategoriaResponseDTO responseDTO = CategoriaResponseDTO.builder().id(categoriaId).nombre("Sedan").build();

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toResponseDTO(categoria)).thenReturn(responseDTO);

        Optional<CategoriaResponseDTO> resultado = categoriaService.buscarPorId(categoriaId);

        assertTrue(resultado.isPresent());
        assertEquals(categoriaId, resultado.get().getId());
        assertEquals("Sedan", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalVacioSiNoExiste() {
        Long categoriaId = 99L;

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        Optional<CategoriaResponseDTO> resultado = categoriaService.buscarPorId(categoriaId);

        assertFalse(resultado.isPresent());
    }

    @Test
    void registrar_deberiaGuardarYRetornarDto() {
        CategoriaRequestDTO requestDTO = CategoriaRequestDTO.builder()
                .nombre("Hatchback")
                .descripcion("Autos compactos")
                .build();
        Categoria categoria = Categoria.builder()
                .nombre("Hatchback")
                .descripcion("Autos compactos")
                .build();
        Categoria guardada = Categoria.builder()
                .id(1L)
                .nombre("Hatchback")
                .descripcion("Autos compactos")
                .build();
        CategoriaResponseDTO responseDTO = CategoriaResponseDTO.builder()
                .id(1L)
                .nombre("Hatchback")
                .descripcion("Autos compactos")
                .build();

        when(categoriaMapper.toEntity(requestDTO)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(guardada);
        when(categoriaMapper.toResponseDTO(guardada)).thenReturn(responseDTO);

        CategoriaResponseDTO resultado = categoriaService.registrar(requestDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Hatchback", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void actualizar_deberiaModificarYRetornarDto() {
        Long categoriaId = 1L;
        Categoria existente = Categoria.builder()
                .id(categoriaId)
                .nombre("SUV")
                .descripcion("Descripcion anterior")
                .build();
        CategoriaRequestDTO requestDTO = CategoriaRequestDTO.builder()
                .nombre("Pickup")
                .descripcion("Vehiculos de carga")
                .build();
        CategoriaResponseDTO responseDTO = CategoriaResponseDTO.builder()
                .id(categoriaId)
                .nombre("Pickup")
                .descripcion("Vehiculos de carga")
                .build();

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(existente)).thenReturn(existente);
        when(categoriaMapper.toResponseDTO(existente)).thenReturn(responseDTO);

        CategoriaResponseDTO resultado = categoriaService.actualizar(categoriaId, requestDTO);

        assertEquals("Pickup", resultado.getNombre());
        assertEquals("Vehiculos de carga", resultado.getDescripcion());
        assertEquals("Pickup", existente.getNombre());
        assertEquals("Vehiculos de carga", existente.getDescripcion());
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiCategoriaNoExiste() {
        Long categoriaId = 99L;
        CategoriaRequestDTO requestDTO = CategoriaRequestDTO.builder()
                .nombre("Convertible")
                .descripcion("Autos descapotables")
                .build();

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoriaService.actualizar(categoriaId, requestDTO));

        assertEquals("Categoria no encontrada", exception.getMessage());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }
}
