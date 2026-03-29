package com.example.autoservice.service.impl;

import com.example.autoservice.dto.MarcaRequestDTO;
import com.example.autoservice.dto.MarcaResponseDTO;
import com.example.autoservice.mapper.MarcaMapper;
import com.example.autoservice.model.Marca;
import com.example.autoservice.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarcaServiceImplTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private MarcaMapper marcaMapper;

    @InjectMocks
    private MarcaServiceImpl marcaService;

    @Test
    void listar_deberiaRetornarMarcasMapeadas() {
        Marca marca = Marca.builder()
                .id(1L)
                .nombre("Toyota")
                .build();
        MarcaResponseDTO responseDTO = MarcaResponseDTO.builder()
                .id(1L)
                .nombre("Toyota")
                .build();

        when(marcaRepository.findAll()).thenReturn(List.of(marca));
        when(marcaMapper.toDTO(marca)).thenReturn(responseDTO);

        List<MarcaResponseDTO> resultado = marcaService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Toyota", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalConDtoSiExiste() {
        Long marcaId = 1L;
        Marca marca = Marca.builder().id(marcaId).nombre("Honda").build();
        MarcaResponseDTO responseDTO = MarcaResponseDTO.builder().id(marcaId).nombre("Honda").build();

        when(marcaRepository.findById(marcaId)).thenReturn(Optional.of(marca));
        when(marcaMapper.toDTO(marca)).thenReturn(responseDTO);

        Optional<MarcaResponseDTO> resultado = marcaService.buscarPorId(marcaId);

        assertTrue(resultado.isPresent());
        assertEquals(marcaId, resultado.get().getId());
        assertEquals("Honda", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalVacioSiNoExiste() {
        Long marcaId = 99L;

        when(marcaRepository.findById(marcaId)).thenReturn(Optional.empty());

        Optional<MarcaResponseDTO> resultado = marcaService.buscarPorId(marcaId);

        assertFalse(resultado.isPresent());
    }

    @Test
    void registrar_deberiaGuardarYRetornarDto() {
        MarcaRequestDTO requestDTO = MarcaRequestDTO.builder()
                .nombre("Mazda")
                .build();
        Marca marca = Marca.builder()
                .nombre("Mazda")
                .build();
        Marca guardada = Marca.builder()
                .id(1L)
                .nombre("Mazda")
                .build();
        MarcaResponseDTO responseDTO = MarcaResponseDTO.builder()
                .id(1L)
                .nombre("Mazda")
                .build();

        when(marcaMapper.toEntity(requestDTO)).thenReturn(marca);
        when(marcaRepository.save(marca)).thenReturn(guardada);
        when(marcaMapper.toDTO(guardada)).thenReturn(responseDTO);

        MarcaResponseDTO resultado = marcaService.registrar(requestDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Mazda", resultado.getNombre());
        verify(marcaRepository).save(marca);
    }
}
