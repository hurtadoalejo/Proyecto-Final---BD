package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Rol del usuario en el sistema. */
@Entity
@Table(name = "Rol")
@Getter @Setter @NoArgsConstructor
public class Rol {

    /** Valores que el sistema usa en sus reglas (deben existir en la tabla). */
    public static final String ADMIN = "ADMIN";
    public static final String EMPLEADO = "EMPLEADO";
    public static final String CLIENTE = "CLIENTE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRol")
    private Long idRol;

    @Column(name = "nombreRol", nullable = false, unique = true, length = 40)
    private String nombreRol;
}
