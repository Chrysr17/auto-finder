package org.example.favoritoservice.service.impl;

import org.example.favoritoservice.client.AutoClient;
import org.example.favoritoservice.dto.AutoDTO;
import org.example.favoritoservice.dto.FavoritoDTO;
import org.example.favoritoservice.dto.FavoritoMetadataRequestDTO;
import org.example.favoritoservice.exception.DuplicateFavoriteException;
import org.example.favoritoservice.exception.InvalidFavoriteRequestException;
import org.example.favoritoservice.exception.RelatedResourceNotFoundException;
import org.example.favoritoservice.exception.ResourceNotFoundException;
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
        assertEquals("General", resultado.getListaNombre());
        verify(favoritoRepository).save(any(Favorito.class));
    }

    @Test
    void agregarFavorito_deberiaGuardarListaYNotaSiSeEnviaMetadata() {
        String username = "christian";
        Long autoId = 10L;
        FavoritoMetadataRequestDTO metadata = FavoritoMetadataRequestDTO.builder()
                .listaNombre("Clasicos japoneses")
                .nota("Revisar version manual")
                .build();

        when(autoClient.obtenerAuto(autoId)).thenReturn(AutoDTO.builder().id(autoId).build());
        when(favoritoRepository.existsByUsernameAndAutoId(username, autoId)).thenReturn(false);
        when(favoritoRepository.save(any(Favorito.class))).thenAnswer(invocation -> {
            Favorito favorito = invocation.getArgument(0);
            favorito.setId(1L);
            return favorito;
        });

        FavoritoDTO resultado = favoritoService.agregarFavorito(username, autoId, metadata);

        assertEquals("Clasicos japoneses", resultado.getListaNombre());
        assertEquals("Revisar version manual", resultado.getNota());
        assertNotNull(resultado.getFechaActualizacion());
    }

    @Test
    void agregarFavorito_deberiaLanzarExcepcionSiAutoNoExiste() {
        String username = "christian";
        Long autoId = 99L;

        when(autoClient.obtenerAuto(autoId)).thenReturn(null);

        RelatedResourceNotFoundException exception = assertThrows(RelatedResourceNotFoundException.class,
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

        DuplicateFavoriteException exception = assertThrows(DuplicateFavoriteException.class,
                () -> favoritoService.agregarFavorito(username, autoId));

        assertEquals("Ya está en favoritos", exception.getMessage());
        verify(favoritoRepository, never()).save(any(Favorito.class));
    }

    @Test
    void agregarFavorito_deberiaLanzarExcepcionSiAutoIdEsInvalido() {
        InvalidFavoriteRequestException exception = assertThrows(InvalidFavoriteRequestException.class,
                () -> favoritoService.agregarFavorito("christian", 0L));

        assertEquals("autoId debe ser mayor que 0", exception.getMessage());
        verify(autoClient, never()).obtenerAuto(any());
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
        assertEquals("General", resultado.get(0).getListaNombre());
    }

    @Test
    void listarFavoritosPorLista_deberiaRetornarSoloListaSolicitada() {
        String username = "christian";
        LocalDateTime fecha = LocalDateTime.of(2026, 3, 28, 10, 30);

        when(favoritoRepository.findByUsernameAndListaNombreIgnoreCaseOrderByFechaCreacionDesc(username, "Track"))
                .thenReturn(List.of(
                        Favorito.builder()
                                .id(1L)
                                .username(username)
                                .autoId(20L)
                                .listaNombre("Track")
                                .fechaCreacion(fecha)
                                .build()
                ));

        List<FavoritoDTO> resultado = favoritoService.listarFavoritosPorLista(username, "Track");

        assertEquals(1, resultado.size());
        assertEquals("Track", resultado.get(0).getListaNombre());
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

    @Test
    void actualizarFavorito_deberiaModificarListaYNota() {
        String username = "christian";
        Long autoId = 20L;
        Favorito favorito = Favorito.builder()
                .id(1L)
                .username(username)
                .autoId(autoId)
                .listaNombre("General")
                .fechaCreacion(LocalDateTime.now())
                .build();
        FavoritoMetadataRequestDTO metadata = FavoritoMetadataRequestDTO.builder()
                .listaNombre("Compra futura")
                .nota("Comparar contra Supra")
                .build();

        when(favoritoRepository.findByUsernameAndAutoId(username, autoId)).thenReturn(java.util.Optional.of(favorito));
        when(favoritoRepository.save(favorito)).thenReturn(favorito);

        FavoritoDTO resultado = favoritoService.actualizarFavorito(username, autoId, metadata);

        assertEquals("Compra futura", resultado.getListaNombre());
        assertEquals("Comparar contra Supra", resultado.getNota());
        assertNotNull(resultado.getFechaActualizacion());
    }

    @Test
    void listarListas_deberiaAgruparFavoritosPorLista() {
        String username = "christian";
        LocalDateTime fecha = LocalDateTime.of(2026, 3, 28, 10, 30);

        when(favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)).thenReturn(List.of(
                Favorito.builder().id(1L).username(username).autoId(20L).listaNombre("Track").fechaCreacion(fecha).build(),
                Favorito.builder().id(2L).username(username).autoId(21L).listaNombre("Track").fechaCreacion(fecha.plusDays(1)).build(),
                Favorito.builder().id(3L).username(username).autoId(22L).listaNombre("Daily").fechaCreacion(fecha).build()
        ));

        var resultado = favoritoService.listarListas(username);

        assertEquals(2, resultado.size());
        assertEquals("Daily", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getTotalFavoritos());
        assertEquals("Track", resultado.get(1).getNombre());
        assertEquals(2, resultado.get(1).getTotalFavoritos());
    }

    @Test
    void obtenerSenales_deberiaAgruparPorListaMarcaYCategoria() {
        String username = "christian";

        when(favoritoRepository.findByUsernameOrderByFechaCreacionDesc(username)).thenReturn(List.of(
                Favorito.builder().id(1L).username(username).autoId(20L).listaNombre("Track").fechaCreacion(LocalDateTime.now()).build(),
                Favorito.builder().id(2L).username(username).autoId(21L).listaNombre("Track").fechaCreacion(LocalDateTime.now()).build(),
                Favorito.builder().id(3L).username(username).autoId(22L).listaNombre("Daily").fechaCreacion(LocalDateTime.now()).build()
        ));
        when(autoClient.obtenerAuto(20L)).thenReturn(AutoDTO.builder().id(20L).marcaNombre("Toyota").categoriaNombre("Coupe").build());
        when(autoClient.obtenerAuto(21L)).thenReturn(AutoDTO.builder().id(21L).marcaNombre("Toyota").categoriaNombre("Sedan").build());
        when(autoClient.obtenerAuto(22L)).thenReturn(AutoDTO.builder().id(22L).marcaNombre("Honda").categoriaNombre("Sedan").build());

        var resultado = favoritoService.obtenerSenales(username);

        assertEquals(3, resultado.getTotalFavoritos());
        assertEquals("Track", resultado.getListas().get(0).getNombre());
        assertEquals(2, resultado.getListas().get(0).getTotal());
        assertEquals("Toyota", resultado.getMarcasPrincipales().get(0).getNombre());
        assertEquals(2, resultado.getMarcasPrincipales().get(0).getTotal());
        assertEquals("Sedan", resultado.getCategoriasPrincipales().get(0).getNombre());
        assertEquals(2, resultado.getCategoriasPrincipales().get(0).getTotal());
        assertEquals(List.of(20L, 21L, 22L), resultado.getAutoIds());
    }

    @Test
    void eliminarFavorito_deberiaEliminarCuandoExiste() {
        String username = "christian";
        Long autoId = 20L;
        Favorito favorito = Favorito.builder().id(1L).username(username).autoId(autoId).fechaCreacion(LocalDateTime.now()).build();

        when(favoritoRepository.findByUsernameAndAutoId(username, autoId)).thenReturn(java.util.Optional.of(favorito));

        favoritoService.eliminarFavorito(username, autoId);

        verify(favoritoRepository).delete(favorito);
    }

    @Test
    void eliminarFavorito_deberiaLanzarExcepcionSiNoExiste() {
        String username = "christian";
        Long autoId = 20L;

        when(favoritoRepository.findByUsernameAndAutoId(username, autoId)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> favoritoService.eliminarFavorito(username, autoId));

        assertEquals("Favorito no encontrado", exception.getMessage());
        verify(favoritoRepository, never()).delete(any(Favorito.class));
    }
}
