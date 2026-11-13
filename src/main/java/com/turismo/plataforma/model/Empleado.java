package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.Cargo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Empleado")
@PrimaryKeyJoinColumn(name = "idUsuario")
@Getter @Setter @NoArgsConstructor
public class Empleado extends Usuario {

    @Column(name = "salario", nullable = false, precision = 12, scale = 2)
    private BigDecimal salario;

    @Column(name = "fechaContratacion", nullable = false)
    private LocalDate fechaContratacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCargo", nullable = false)
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idSede", nullable = false)
    private Sede sede;
}
