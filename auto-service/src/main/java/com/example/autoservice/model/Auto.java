package com.example.autoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "auto")
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precio;
    private Double precioReferenciaActual;
    private Double precioSalidaEstimado;
    private String color;
    private String motor;

    @Column(name = "cilindrada_cc")
    private Integer cilindradaCc;

    @Column(name = "caballos_fuerza")
    private Integer caballosFuerza;

    @Column(name = "torque_nm")
    private Integer torqueNm;

    private Double consumoCiudad;
    private Double consumoCarretera;
    private Integer velocidadMaxima;

    @Column(name = "aceleracion_0_100")
    private Double aceleracionCeroACien;

    private String tipoCombustible;
    private String transmision;
    private String traccion;

    @Column(name = "peso_kg")
    private Integer pesoKg;

    private Integer puertas;
    private String moneda;

    @Column(columnDefinition = "TEXT")
    private String descripcionValor;

    @Column(columnDefinition = "TEXT")
    private String resumen;

    @Column(name = "año_fabricacion")
    private Integer anioFabricacion;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "auto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AutoImagen> imagenes = new ArrayList<>() ;

}
