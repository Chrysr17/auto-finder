package com.example.autoservice.repository;

import com.example.autoservice.model.Auto;
import com.example.autoservice.projection.AutoListadoProjection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepositoy extends JpaRepository<Auto, Long>, JpaSpecificationExecutor<Auto> {
    List<Auto> findByMarcaNombre(String marca);
    List<Auto> findByCategoriaNombre(String categoria);

    @Query("""
        SELECT
            a.id as id,
            a.color as color,
            a.precio as precio,
            a.precioReferenciaActual as precioReferenciaActual,
            a.precioSalidaEstimado as precioSalidaEstimado,
            a.anioFabricacion as anioFabricacion,
            a.motor as motor,
            a.caballosFuerza as caballosFuerza,
            a.velocidadMaxima as velocidadMaxima,
            a.tipoCombustible as tipoCombustible,

            m.id as marcaId,
            m.nombre as marcaNombre,

            mo.id as modeloId,
            mo.nombre as modeloNombre,

            c.id as categoriaId,
            c.nombre as categoriaNombre,

            ai.url as imagenPortadaUrl
        FROM Auto a
        JOIN a.marca m
        JOIN a.modelo mo
        JOIN a.categoria c
        LEFT JOIN a.imagenes ai ON ai.orden = 1
    """)
    List<AutoListadoProjection> listarAutosConPortada();
}
