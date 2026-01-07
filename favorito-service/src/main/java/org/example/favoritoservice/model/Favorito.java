package org.example.favoritoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorito", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "auto_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "auto_id", nullable = false)
    private Long autoId;

    @Column(name = "fecha_creacion" , nullable = false)
    private LocalDateTime fechaCreacion;

}
