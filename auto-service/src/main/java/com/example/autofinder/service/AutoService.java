package com.example.autofinder.service;

import com.example.autofinder.model.Auto;

import java.util.List;
import java.util.Optional;

public interface AutoService {
    List<Auto> listarTodos();
    Optional<Auto> buscarPorId(Long id);
    Auto registrar(Auto auto);
    Auto actualizar(Long id, Auto auto);
    void eliminar(Long id);
    List<Auto> buscarPorMarca(String marca);
    List<Auto> buscarPorCategoria(String categoria);
}
