package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter @Setter @NoArgsConstructor
public class Empleado extends Usuario {

    @NotNull @PositiveOrZero
    @Column(name = "salario", nullable = false, precision = 12, scale = 2)
    private BigDecimal salario;

    @NotBlank @Size(max = 60)
    @Column(name = "cargo", nullable = false, length = 60)
    private String cargo;

    @NotNull
    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sede", nullable = false)
    private Sede sede;
}