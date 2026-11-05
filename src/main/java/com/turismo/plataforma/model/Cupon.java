package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupon")
@Getter @Setter @NoArgsConstructor
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cupon")
    private Long idCupon;

    @NotBlank @Size(max = 100)
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull @DecimalMin("0.01") @DecimalMax("100.00")
    @Column(name = "porcentaje_descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;
}