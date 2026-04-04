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
                .and(anioMayorOIgual(filtro.getAnioMin()))
                .and(anioMenorOIgual(filtro.getAnioMax()))
                .and(colorContiene(filtro.getColor()));
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

    private static Specification<Auto> anioMayorOIgual(Integer anioMin) {
        return (root, query, cb) ->
                anioMin == null ? null : cb.greaterThanOrEqualTo(root.get("anioFabricacion"), anioMin);
    }

    private static Specification<Auto> anioMenorOIgual(Integer anioMax) {
        return (root, query, cb) ->
                anioMax == null ? null : cb.lessThanOrEqualTo(root.get("anioFabricacion"), anioMax);
    }

    private static Specification<Auto> colorContiene(String color) {
        return (root, query, cb) ->
                color == null || color.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("color")), "%" + color.toLowerCase() + "%");
    }
}
