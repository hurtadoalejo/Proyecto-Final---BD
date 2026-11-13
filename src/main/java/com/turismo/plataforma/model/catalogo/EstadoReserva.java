package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Estado de una reserva. */
@Entity
@Table(name = "EstadoReserva")
@Getter @Setter @NoArgsConstructor
public class EstadoReserva {

    /** Valores que el sistema usa en sus reglas (deben existir en la tabla). */
    public static final String PENDIENTE = "PENDIENTE";
    public static final String CONFIRMADA = "CONFIRMADA";
    public static final String CANCELADA = "CANCELADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoReserva")
    private Long idEstadoReserva;

    @Column(name = "nombreEstadoReserva", nullable = false, unique = true, length = 20)
    private String nombreEstadoReserva;
}
