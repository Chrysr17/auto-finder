package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.dto.AutoRequestDTO;
import com.example.autoservice.dto.AutoResponseDTO;
import com.example.autoservice.exception.InvalidAutoRequestException;
import com.example.autoservice.exception.InvalidSearchFilterException;
import com.example.autoservice.exception.RelatedResourceNotFoundException;
import com.example.autoservice.exception.ResourceNotFoundException;
import com.example.autoservice.mapper.AutoMapper;
import com.example.autoservice.model.Auto;
import com.example.autoservice.model.Categoria;
import com.example.autoservice.model.Marca;
import com.example.autoservice.model.Modelo;
import com.example.autoservice.projection.AutoListadoProjection;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutoServiceImplTest {

    @Mock
    private AutoRepositoy autoRepositoy;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private AutoMapper autoMapper;

    @InjectMocks
    private AutoServiceImpl autoService;

    @Test
    void listarTodos_deberiaRetornarAutosConPortadaMapeados() {
        AutoListadoProjection projection = crearProjection(1L, "Rojo", 25000.0, 2023,
                10L, "Toyota", 20L, "Corolla", 30L, "Sedan", "portada.jpg");
        AutoResponseDTO responseDTO = AutoResponseDTO.builder()
                .id(1L)
                .color("Rojo")
                .precio(25000.0)
                .anioFabricacion(2023)
                .marcaNombre("Toyota")
                .modeloNombre("Corolla")
                .categoriaNombre("Sedan")
                .imagenPortadaUrl("portada.jpg")
                .build();

        when(autoRepositoy.listarAutosConPortada()).thenReturn(List.of(projection));
        when(autoMapper.toResponseDTO(projection)).thenReturn(responseDTO);

        List<AutoResponseDTO> resultado = autoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Rojo", resultado.get(0).getColor());
        assertEquals("portada.jpg", resultado.get(0).getImagenPortadaUrl());
    }

    @Test
    void buscarPorId_deberiaRetornarOptionalConDtoSiExiste() {
        Long autoId = 1L;
        Auto auto = Auto.builder().id(autoId).color("Azul").build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder().id(autoId).color("Azul").build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(auto));
        when(autoMapper.toResponseDTO(auto)).thenReturn(responseDTO);

        AutoResponseDTO resultado = autoService.buscarPorId(autoId);

        assertEquals(autoId, resultado.getId());
        assertEquals("Azul", resultado.getColor());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionSiNoExiste() {
        Long autoId = 99L;

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> autoService.buscarPorId(autoId));

        assertEquals("Auto no encontrado", exception.getMessage());
    }

    @Test
    void buscarConFiltros_deberiaRetornarResultadoPaginadoYOrdenado() {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .marcaId(10L)
                .precioMin(15000.0)
                .precioMax(25000.0)
                .anioMin(2020)
                .anioMax(2024)
                .color("Azul")
                .page(1)
                .size(5)
                .sortBy("marca")
                .direction("desc")
                .build();
        Auto auto = Auto.builder().id(1L).color("Azul").precio(22000.0).anioFabricacion(2023).build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder()
                .id(1L)
                .color("Azul")
                .precio(22000.0)
                .anioFabricacion(2023)
                .marcaNombre("Toyota")
                .build();
        Page<Auto> autosPage = new PageImpl<>(
                List.of(auto),
                PageRequest.of(1, 5, org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "marca.nombre")),
                11
        );

        when(autoRepositoy.findAll(any(Specification.class), any(Pageable.class))).thenReturn(autosPage);
        when(autoMapper.toResponseDTO(auto)).thenReturn(responseDTO);

        var resultado = autoService.buscarConFiltros(filtro);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(autoRepositoy).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(org.springframework.data.domain.Sort.Direction.DESC,
                pageable.getSort().getOrderFor("marca.nombre").getDirection());
        assertEquals(1, resultado.getContent().size());
        assertEquals(11, resultado.getTotalElements());
        assertEquals(3, resultado.getTotalPages());
        assertEquals("Azul", resultado.getContent().get(0).getColor());
        assertFalse(resultado.isFirst());
        assertFalse(resultado.isLast());
    }

    @Test
    void buscarConFiltros_deberiaLanzarExcepcionSiPrecioMinEsMayorQuePrecioMax() {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .precioMin(30000.0)
                .precioMax(20000.0)
                .build();

        InvalidSearchFilterException exception = assertThrows(InvalidSearchFilterException.class,
                () -> autoService.buscarConFiltros(filtro));

        assertEquals("precioMin no puede ser mayor que precioMax", exception.getMessage());
        verify(autoRepositoy, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buscarConFiltros_deberiaLanzarExcepcionSiPageEsNegativo() {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .page(-1)
                .build();

        InvalidSearchFilterException exception = assertThrows(InvalidSearchFilterException.class,
                () -> autoService.buscarConFiltros(filtro));

        assertEquals("page no puede ser menor que 0", exception.getMessage());
        verify(autoRepositoy, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buscarConFiltros_deberiaLanzarExcepcionSiSortByNoEsSoportado() {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .sortBy("kilometraje")
                .build();

        InvalidSearchFilterException exception = assertThrows(InvalidSearchFilterException.class,
                () -> autoService.buscarConFiltros(filtro));

        assertEquals("sortBy no soportado. Valores permitidos: precio, anioFabricacion, color, marca",
                exception.getMessage());
        verify(autoRepositoy, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void buscarConFiltros_deberiaLanzarExcepcionSiDirectionNoEsSoportado() {
        AutoFiltroRequestDTO filtro = AutoFiltroRequestDTO.builder()
                .direction("up")
                .build();

        InvalidSearchFilterException exception = assertThrows(InvalidSearchFilterException.class,
                () -> autoService.buscarConFiltros(filtro));

        assertEquals("direction no soportado. Valores permitidos: asc, desc", exception.getMessage());
        verify(autoRepositoy, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void registrar_deberiaGuardarAutoConRelacionesYRetornarDto() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marcaId(10L)
                .modeloId(20L)
                .categoriaId(30L)
                .build();
        Auto auto = Auto.builder()
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .build();
        Marca marca = Marca.builder().id(10L).nombre("Mazda").build();
        Modelo modelo = Modelo.builder().id(20L).nombre("CX-5").build();
        Categoria categoria = Categoria.builder().id(30L).nombre("SUV").build();
        Auto guardado = Auto.builder()
                .id(1L)
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marca(marca)
                .modelo(modelo)
                .categoria(categoria)
                .build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder()
                .id(1L)
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marcaNombre("Mazda")
                .modeloNombre("CX-5")
                .categoriaNombre("SUV")
                .build();

        when(autoMapper.toEntity(requestDTO)).thenReturn(auto);
        when(marcaRepository.findById(10L)).thenReturn(Optional.of(marca));
        when(modeloRepository.findById(20L)).thenReturn(Optional.of(modelo));
        when(categoriaRepository.findById(30L)).thenReturn(Optional.of(categoria));
        when(autoRepositoy.save(auto)).thenReturn(guardado);
        when(autoMapper.toResponseDTO(guardado)).thenReturn(responseDTO);

        AutoResponseDTO resultado = autoService.registrar(requestDTO);

        assertEquals(1L, resultado.getId());
        assertEquals("Negro", resultado.getColor());
        assertEquals(marca, auto.getMarca());
        assertEquals(modelo, auto.getModelo());
        assertEquals(categoria, auto.getCategoria());
        verify(autoRepositoy).save(auto);
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiMarcaNoExiste() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marcaId(10L)
                .modeloId(20L)
                .categoriaId(30L)
                .build();
        Auto auto = Auto.builder().build();

        when(autoMapper.toEntity(requestDTO)).thenReturn(auto);
        when(marcaRepository.findById(10L)).thenReturn(Optional.empty());

        RelatedResourceNotFoundException exception = assertThrows(RelatedResourceNotFoundException.class,
                () -> autoService.registrar(requestDTO));

        assertEquals("Marca no existe", exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiColorEsInvalido() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("   ")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marcaId(10L)
                .modeloId(20L)
                .categoriaId(30L)
                .build();

        InvalidAutoRequestException exception = assertThrows(InvalidAutoRequestException.class,
                () -> autoService.registrar(requestDTO));

        assertEquals("color es obligatorio", exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiPrecioNoEsPositivo() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Negro")
                .precio(0.0)
                .anioFabricacion(2024)
                .marcaId(10L)
                .modeloId(20L)
                .categoriaId(30L)
                .build();

        InvalidAutoRequestException exception = assertThrows(InvalidAutoRequestException.class,
                () -> autoService.registrar(requestDTO));

        assertEquals("precio debe ser mayor que 0", exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiAnioFabricacionEstaFueraDeRango() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(Year.now().getValue() + 2)
                .marcaId(10L)
                .modeloId(20L)
                .categoriaId(30L)
                .build();

        InvalidAutoRequestException exception = assertThrows(InvalidAutoRequestException.class,
                () -> autoService.registrar(requestDTO));

        assertEquals("anioFabricacion debe estar entre 1886 y " + (Year.now().getValue() + 1), exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void registrar_deberiaLanzarExcepcionSiCategoriaIdEsNulo() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Negro")
                .precio(32000.0)
                .anioFabricacion(2024)
                .marcaId(10L)
                .modeloId(20L)
                .build();

        InvalidAutoRequestException exception = assertThrows(InvalidAutoRequestException.class,
                () -> autoService.registrar(requestDTO));

        assertEquals("categoriaId es obligatorio", exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void actualizar_deberiaModificarCamposYRelacionesIndicadas() {
        Long autoId = 1L;
        Marca marcaAnterior = Marca.builder().id(10L).nombre("Toyota").build();
        Modelo modeloAnterior = Modelo.builder().id(20L).nombre("Corolla").build();
        Categoria categoriaAnterior = Categoria.builder().id(30L).nombre("Sedan").build();
        Auto existente = Auto.builder()
                .id(autoId)
                .color("Blanco")
                .precio(18000.0)
                .anioFabricacion(2021)
                .marca(marcaAnterior)
                .modelo(modeloAnterior)
                .categoria(categoriaAnterior)
                .build();
        Marca nuevaMarca = Marca.builder().id(11L).nombre("Honda").build();
        Modelo nuevoModelo = Modelo.builder().id(21L).nombre("Civic").build();
        Categoria nuevaCategoria = Categoria.builder().id(31L).nombre("Hatchback").build();
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Gris")
                .precio(19500.0)
                .anioFabricacion(2022)
                .marcaId(11L)
                .modeloId(21L)
                .categoriaId(31L)
                .build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder()
                .id(autoId)
                .color("Gris")
                .precio(19500.0)
                .anioFabricacion(2022)
                .marcaNombre("Honda")
                .modeloNombre("Civic")
                .categoriaNombre("Hatchback")
                .build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.of(existente));
        when(marcaRepository.findById(11L)).thenReturn(Optional.of(nuevaMarca));
        when(modeloRepository.findById(21L)).thenReturn(Optional.of(nuevoModelo));
        when(categoriaRepository.findById(31L)).thenReturn(Optional.of(nuevaCategoria));
        when(autoRepositoy.save(existente)).thenReturn(existente);
        when(autoMapper.toResponseDTO(existente)).thenReturn(responseDTO);

        AutoResponseDTO resultado = autoService.actualizar(autoId, requestDTO);

        assertEquals("Gris", resultado.getColor());
        assertEquals(19500.0, resultado.getPrecio());
        assertEquals(2022, resultado.getAnioFabricacion());
        assertEquals(nuevaMarca, existente.getMarca());
        assertEquals(nuevoModelo, existente.getModelo());
        assertEquals(nuevaCategoria, existente.getCategoria());
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiAutoNoExiste() {
        Long autoId = 99L;
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Rojo")
                .precio(10000.0)
                .anioFabricacion(2021)
                .build();

        when(autoRepositoy.findById(autoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> autoService.actualizar(autoId, requestDTO));

        assertEquals("Auto no encontrado", exception.getMessage());
        verify(autoRepositoy, never()).save(any(Auto.class));
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiPrecioEsNulo() {
        AutoRequestDTO requestDTO = AutoRequestDTO.builder()
                .color("Rojo")
                .anioFabricacion(2021)
                .build();

        InvalidAutoRequestException exception = assertThrows(InvalidAutoRequestException.class,
                () -> autoService.actualizar(1L, requestDTO));

        assertEquals("precio es obligatorio", exception.getMessage());
        verify(autoRepositoy, never()).findById(1L);
    }

    @Test
    void eliminar_deberiaEliminarPorId() {
        Long autoId = 8L;

        when(autoRepositoy.existsById(autoId)).thenReturn(true);

        autoService.eliminar(autoId);

        verify(autoRepositoy).deleteById(autoId);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiAutoNoExiste() {
        Long autoId = 8L;

        when(autoRepositoy.existsById(autoId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> autoService.eliminar(autoId));

        assertEquals("Auto no encontrado", exception.getMessage());
        verify(autoRepositoy, never()).deleteById(autoId);
    }

    @Test
    void buscarPorMarca_deberiaRetornarAutosMapeados() {
        Auto auto = Auto.builder().id(1L).color("Rojo").build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder().id(1L).color("Rojo").build();

        when(autoRepositoy.findByMarcaNombre("Toyota")).thenReturn(List.of(auto));
        when(autoMapper.toResponseDTO(auto)).thenReturn(responseDTO);

        List<AutoResponseDTO> resultado = autoService.buscarPorMarca("Toyota");

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Rojo", resultado.get(0).getColor());
    }

    @Test
    void buscarPorCategoria_deberiaRetornarAutosMapeados() {
        Auto auto = Auto.builder().id(2L).color("Azul").build();
        AutoResponseDTO responseDTO = AutoResponseDTO.builder().id(2L).color("Azul").build();

        when(autoRepositoy.findByCategoriaNombre("SUV")).thenReturn(List.of(auto));
        when(autoMapper.toResponseDTO(auto)).thenReturn(responseDTO);

        List<AutoResponseDTO> resultado = autoService.buscarPorCategoria("SUV");

        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getId());
        assertEquals("Azul", resultado.get(0).getColor());
    }

    private AutoListadoProjection crearProjection(
            Long id,
            String color,
            Double precio,
            Integer anioFabricacion,
            Long marcaId,
            String marcaNombre,
            Long modeloId,
            String modeloNombre,
            Long categoriaId,
            String categoriaNombre,
            String imagenPortadaUrl
    ) {
        return new AutoListadoProjection() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getColor() {
                return color;
            }

            @Override
            public Double getPrecio() {
                return precio;
            }

            @Override
            public Integer getAnioFabricacion() {
                return anioFabricacion;
            }

            @Override
            public Long getMarcaId() {
                return marcaId;
            }

            @Override
            public String getMarcaNombre() {
                return marcaNombre;
            }

            @Override
            public Long getModeloId() {
                return modeloId;
            }

            @Override
            public String getModeloNombre() {
                return modeloNombre;
            }

            @Override
            public Long getCategoriaId() {
                return categoriaId;
            }

            @Override
            public String getCategoriaNombre() {
                return categoriaNombre;
            }

            @Override
            public String getImagenPortadaUrl() {
                return imagenPortadaUrl;
            }
        };
    }
}
