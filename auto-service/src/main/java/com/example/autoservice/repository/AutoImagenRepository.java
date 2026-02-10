package com.example.autoservice.repository;

import com.example.autoservice.model.AutoImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoImagenRepository extends JpaRepository<AutoImagen,Long> {
    List<AutoImagen> findByAutoIdOrderByOrdenAsc(Long autoId);
    boolean existsByAutoIdAndOrden(Long autoId,Integer orden);
}
