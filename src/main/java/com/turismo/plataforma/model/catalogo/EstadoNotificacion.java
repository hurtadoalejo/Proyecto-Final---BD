package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Estado de envío de una notificación. */
@Entity
@Table(name = "EstadoNotificacion")
@Getter @Setter @NoArgsConstructor
public class EstadoNotificacion {

    /** Valores que el sistema usa en sus reglas (deben existir en la tabla). */
    public static final String PENDIENTE = "PENDIENTE";
    public static final String ENVIADO = "ENVIADO";
    public static final String FALLIDO = "FALLIDO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoNotificacion")
    private Long idEstadoNotificacion;

    @Column(name = "nombreEstadoNotificacion", nullable = false, unique = true, length = 20)
    private String nombreEstadoNotificacion;
}
