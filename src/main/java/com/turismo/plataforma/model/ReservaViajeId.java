package com.turismo.plataforma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/** Llave compuesta de ReservaViaje: (idReserva, idProgramacionViaje). */
@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ReservaViajeId implements Serializable {

    @Column(name = "idReserva")
    private Long idReserva;

    @Column(name = "idProgramacionViaje")
    private Long idProgramacionViaje;
}
