package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ruta")
@Getter @Setter @NoArgsConstructor
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long idRuta;

    @NotBlank @Size(max = 150)
    @Column(name = "direccion_origen", nullable = false, length = 150)
    private String direccionOrigen;

    @NotBlank @Size(max = 150)
    @Column(name = "direccion_destino", nullable = false, length = 150)
    private String direccionDestino;

    @NotNull @Positive
    @Column(name = "distancia", nullable = false, precision = 10, scale = 2)
    private BigDecimal distancia;

    @NotNull @Positive
    @Column(name = "duracion_estimada", nullable = false)
    private Integer duracionEstimada;

    @NotNull @PositiveOrZero
    @Column(name = "precio_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioBase;
}