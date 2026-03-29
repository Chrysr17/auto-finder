package org.example.favoritoservice.service.impl;

import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.model.Favorito;
import org.example.favoritoservice.repository.FavoritoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceImplTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private AutoClient autoClient;

    @InjectMocks
    private FavoritoServiceImpl favoritoService;

    @Test
    void agregarFavorito_deberiaGuardarYRetornarDto() {
        String username = "christian";
        Long autoId = 10L;

        when(autoClient.obtenerAuto(autoId)).thenReturn(AutoDTO.builder().id(autoId).build());
        when(favoritoRepository.existsByUsernameAndAutoId(username, autoId)).thenReturn(false);
        when(favoritoRepository.save(any(Favorito.class))).thenAnswer(invocation -> {
            Favorito favorito = invocation.getArgument(0);
            favorito.setId(1L);
            return favorito;
        });

        FavoritoDTO resultado = favoritoService.agregarFavorito(username, autoId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(autoId, resultado.getAutoId());
        assertNotNull(resultado.getFechaCreacion());
        verify(favoritoRepository).save(any(Favorito.class));
    }

    @Test
    void agregarFavorito_deberiaLanzarExcepcionSiAutoNoExiste() {
        String username = "christian";
        Long autoId = 99L;

        when(autoClient.obtenerAuto(autoId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritoService.agregarFavorito(username, autoId));

        assertEquals("No se encontro el auto", exception.getMessage());
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void agregarFavorito_deberiaLanzarExcepcionSiYaExiste() {
        String username = "christian";
        Long autoId = 10L;

        when(autoClient.obtenerAuto(autoId)).thenReturn(AutoDTO.builder().id(autoId).build());
        when(favoritoRepository.existsByUsernameAndAutoId(username, autoId)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritoService.agregarFavorito(username, autoId));

        assertEquals("Ya está en favoritos", exception.getMessage());
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void listarFavoritos_deberiaRetornarDtosOrdenados() {
        String username = "christian";
        LocalDateTime fecha = LocalDateTime.of(2026, 3, 28, 10, 30);

        when(favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)).thenReturn(List.of(
                Favorito.builder().id(1L).username(username).autoId(20L).fechaCreacion(fecha).build()
        ));

        List<FavoritoDTO> resultado = favoritoService.listarFavoritos(username);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(20L, resultado.get(0).getAutoId());
        assertEquals(fecha, resultado.get(0).getFechaCreacion());
    }

    @Test
    void listarFavoritosConDetalle_deberiaRetornarAutosDelCliente() {
        String username = "christian";
        Long autoId = 20L;
        AutoDTO autoDTO = AutoDTO.builder().id(autoId).marcaNombre("Toyota").modeloNombre("Corolla").build();

        when(favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)).thenReturn(List.of(
                Favorito.builder().id(1L).username(username).autoId(autoId).fechaCreacion(LocalDateTime.now()).build()
        ));
        when(autoClient.obtenerAuto(autoId)).thenReturn(autoDTO);

        List<AutoDTO> resultado = favoritoService.listarFavoritosConDetalle(username);

        assertEquals(1, resultado.size());
        assertEquals(autoId, resultado.get(0).getId());
        assertEquals("Toyota", resultado.get(0).getMarcaNombre());
    }
}
