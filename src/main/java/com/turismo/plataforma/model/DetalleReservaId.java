package com.turismo.plataforma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/** Llave compuesta de DetalleReserva: (idDetalle, idReserva). */
@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class DetalleReservaId implements Serializable {

    /** Número del pasajero dentro de la reserva. */
    @Column(name = "idDetalle")
    private Long idDetalle;

    @Column(name = "idReserva")
    private Long idReserva;
}
