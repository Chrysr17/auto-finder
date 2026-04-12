package com.example.autoservice.projection;

public interface AutoListadoProjection {
    Long getId();
    String getColor();
    Double getPrecio();
    Double getPrecioReferenciaActual();
    Double getPrecioSalidaEstimado();
    Integer getAnioFabricacion();
    String getMotor();
    Integer getCaballosFuerza();
    Integer getVelocidadMaxima();
    String getTipoCombustible();

    Long getMarcaId();
    String getMarcaNombre();

    Long getModeloId();
    String getModeloNombre();

    Long getCategoriaId();
    String getCategoriaNombre();

    String getImagenPortadaUrl();
}
