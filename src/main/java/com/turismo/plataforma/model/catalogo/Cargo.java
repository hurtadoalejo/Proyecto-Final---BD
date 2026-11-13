package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Cargo de un empleado. */
@Entity
@Table(name = "Cargo")
@Getter @Setter @NoArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCargo")
    private Long idCargo;

    @Column(name = "nombreCargo", nullable = false, unique = true, length = 40)
    private String nombreCargo;
}
