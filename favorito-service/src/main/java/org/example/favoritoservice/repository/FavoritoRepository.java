package org.example.favoritoservice.repository;

import org.example.favoritoservice.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsernameOrderByFechaCreacionDesc(String username);

    Optional<Favorito> findByUsernameAndAutoId(String username, Long autoId);

    boolean existsByUsernameAndAutoId(String username, Long autoId);

    void deleteByUsernameAndAutoId(String username, Long autoId);
}
