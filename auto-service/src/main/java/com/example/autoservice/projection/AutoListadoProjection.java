package com.example.autoservice.projection;

public interface AutoListadoProjection {
    Long getId();
    String getColor();
    Double getPrecio();
    Integer getAnioFabricacion();

    Long getMarcaId();
    String getMarcaNombre();

    Long getModeloId();
    String getModeloNombre();

    Long getCategoriaId();
    String getCategoriaNombre();

    String getImagenPortadaUrl();
}
