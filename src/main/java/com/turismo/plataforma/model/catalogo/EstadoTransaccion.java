package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Estado de un pago. */
@Entity
@Table(name = "EstadoTransaccion")
@Getter @Setter @NoArgsConstructor
public class EstadoTransaccion {

    /** Valores que el sistema usa en sus reglas (deben existir en la tabla). */
    public static final String PENDIENTE = "PENDIENTE";
    public static final String APROBADA = "APROBADA";
    public static final String RECHAZADA = "RECHAZADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoTransaccion")
    private Long idEstadoTransaccion;

    @Column(name = "nombreEstadoTransaccion", nullable = false, unique = true, length = 20)
    private String nombreEstadoTransaccion;
}
