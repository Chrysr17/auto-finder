package com.example.autoservice.service.impl;

import com.example.autoservice.dto.AutoDTO;
import com.example.autoservice.repository.AutoRepositoy;
import com.example.autoservice.repository.CategoriaRepository;
import com.example.autoservice.repository.MarcaRepository;
import com.example.autoservice.repository.ModeloRepository;
import com.example.autoservice.service.AutoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepositoy autoRepositoy;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    public AutoServiceImpl(AutoRepositoy autoRepositoy, CategoriaRepository categoriaRepository, MarcaRepository marcaRepository, ModeloRepository modeloRepository) {
        this.autoRepositoy = autoRepositoy;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
    }


    @Override
    public List<AutoDTO> listarTodos() {
        return List.of();
    }

    @Override
    public AutoDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public AutoDTO registrar(AutoDTO autoDTO) {
        return null;
    }

    @Override
    public AutoDTO actualizar(Long id, AutoDTO autoDTO) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public List<AutoDTO> buscarPorMarca(String marca) {
        return List.of();
    }

    @Override
    public List<AutoDTO> buscarPorCategoria(String categoria) {
        return List.of();
    }
}
