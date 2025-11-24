package com.example.autoservice.repository;

import com.example.autoservice.model.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepositoy extends JpaRepository<Auto, Long> {
    List<Auto> findByMarcaNombre(String marca);
    List<Auto> findByCategoriaNombre(String categoria);
}
