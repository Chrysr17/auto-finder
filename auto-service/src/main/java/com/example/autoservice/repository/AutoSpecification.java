package com.example.autoservice.repository;

import com.example.autoservice.dto.AutoFiltroRequestDTO;
import com.example.autoservice.model.Auto;
import org.springframework.data.jpa.domain.Specification;

public final class AutoSpecification {

    private AutoSpecification() {
    }

    public static Specification<Auto> withFilters(AutoFiltroRequestDTO filtro) {
        return Specification.where(hasMarca(filtro.getMarcaId()))
                .and(hasModelo(filtro.getModeloId()))
                .and(hasCategoria(filtro.getCategoriaId()))
                .and(precioMayorOIgual(filtro.getPrecioMin()))
                .and(precioMenorOIgual(filtro.getPrecioMax()))
                .and(precioReferenciaActualMayorOIgual(filtro.getPrecioReferenciaActualMin()))
                .and(precioReferenciaActualMenorOIgual(filtro.getPrecioReferenciaActualMax()))
                .and(precioSalidaEstimadoMayorOIgual(filtro.getPrecioSalidaEstimadoMin()))
                .and(precioSalidaEstimadoMenorOIgual(filtro.getPrecioSalidaEstimadoMax()))
                .and(anioMayorOIgual(filtro.getAnioMin()))
                .and(anioMenorOIgual(filtro.getAnioMax()))
                .and(caballosFuerzaMayorOIgual(filtro.getCaballosFuerzaMin()))
                .and(caballosFuerzaMenorOIgual(filtro.getCaballosFuerzaMax()))
                .and(velocidadMaximaMayorOIgual(filtro.getVelocidadMaximaMin()))
                .and(velocidadMaximaMenorOIgual(filtro.getVelocidadMaximaMax()))
                .and(colorContiene(filtro.getColor()))
                .and(motorContiene(filtro.getMotor()))
                .and(tipoCombustibleEs(filtro.getTipoCombustible()));
    }

    private static Specification<Auto> hasMarca(Long marcaId) {
        return (root, query, cb) ->
                marcaId == null ? null : cb.equal(root.get("marca").get("id"), marcaId);
    }

    private static Specification<Auto> hasModelo(Long modeloId) {
        return (root, query, cb) ->
                modeloId == null ? null : cb.equal(root.get("modelo").get("id"), modeloId);
    }

    private static Specification<Auto> hasCategoria(Long categoriaId) {
        return (root, query, cb) ->
                categoriaId == null ? null : cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    private static Specification<Auto> precioMayorOIgual(Double precioMin) {
        return (root, query, cb) ->
                precioMin == null ? null : cb.greaterThanOrEqualTo(root.get("precio"), precioMin);
    }

    private static Specification<Auto> precioMenorOIgual(Double precioMax) {
        return (root, query, cb) ->
                precioMax == null ? null : cb.lessThanOrEqualTo(root.get("precio"), precioMax);
    }

    private static Specification<Auto> precioReferenciaActualMayorOIgual(Double precioReferenciaActualMin) {
        return (root, query, cb) ->
                precioReferenciaActualMin == null ? null : cb.greaterThanOrEqualTo(root.get("precioReferenciaActual"), precioReferenciaActualMin);
    }

    private static Specification<Auto> precioReferenciaActualMenorOIgual(Double precioReferenciaActualMax) {
        return (root, query, cb) ->
                precioReferenciaActualMax == null ? null : cb.lessThanOrEqualTo(root.get("precioReferenciaActual"), precioReferenciaActualMax);
    }

    private static Specification<Auto> precioSalidaEstimadoMayorOIgual(Double precioSalidaEstimadoMin) {
        return (root, query, cb) ->
                precioSalidaEstimadoMin == null ? null : cb.greaterThanOrEqualTo(root.get("precioSalidaEstimado"), precioSalidaEstimadoMin);
    }

    private static Specification<Auto> precioSalidaEstimadoMenorOIgual(Double precioSalidaEstimadoMax) {
        return (root, query, cb) ->
                precioSalidaEstimadoMax == null ? null : cb.lessThanOrEqualTo(root.get("precioSalidaEstimado"), precioSalidaEstimadoMax);
    }

    private static Specification<Auto> anioMayorOIgual(Integer anioMin) {
        return (root, query, cb) ->
                anioMin == null ? null : cb.greaterThanOrEqualTo(root.get("anioFabricacion"), anioMin);
    }

    private static Specification<Auto> anioMenorOIgual(Integer anioMax) {
        return (root, query, cb) ->
                anioMax == null ? null : cb.lessThanOrEqualTo(root.get("anioFabricacion"), anioMax);
    }

    private static Specification<Auto> caballosFuerzaMayorOIgual(Integer caballosFuerzaMin) {
        return (root, query, cb) ->
                caballosFuerzaMin == null ? null : cb.greaterThanOrEqualTo(root.get("caballosFuerza"), caballosFuerzaMin);
    }

    private static Specification<Auto> caballosFuerzaMenorOIgual(Integer caballosFuerzaMax) {
        return (root, query, cb) ->
                caballosFuerzaMax == null ? null : cb.lessThanOrEqualTo(root.get("caballosFuerza"), caballosFuerzaMax);
    }

    private static Specification<Auto> velocidadMaximaMayorOIgual(Integer velocidadMaximaMin) {
        return (root, query, cb) ->
                velocidadMaximaMin == null ? null : cb.greaterThanOrEqualTo(root.get("velocidadMaxima"), velocidadMaximaMin);
    }

    private static Specification<Auto> velocidadMaximaMenorOIgual(Integer velocidadMaximaMax) {
        return (root, query, cb) ->
                velocidadMaximaMax == null ? null : cb.lessThanOrEqualTo(root.get("velocidadMaxima"), velocidadMaximaMax);
    }

    private static Specification<Auto> colorContiene(String color) {
        return (root, query, cb) ->
                color == null || color.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("color")), "%" + color.toLowerCase() + "%");
    }

    private static Specification<Auto> motorContiene(String motor) {
        return (root, query, cb) ->
                motor == null || motor.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("motor")), "%" + motor.toLowerCase() + "%");
    }

    private static Specification<Auto> tipoCombustibleEs(String tipoCombustible) {
        return (root, query, cb) ->
                tipoCombustible == null || tipoCombustible.isBlank()
                        ? null
                        : cb.equal(cb.lower(root.get("tipoCombustible")), tipoCombustible.toLowerCase());
    }
}
