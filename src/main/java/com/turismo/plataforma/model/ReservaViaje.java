package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.TipoTrayecto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Relación N:M Reserva - ProgramacionViaje, con su tipo de trayecto (IDA / VUELTA). */
@Entity
@Table(name = "ReservaViaje")
@Getter @Setter @NoArgsConstructor
public class ReservaViaje {

    @EmbeddedId
    private ReservaViajeId id = new ReservaViajeId();

    @MapsId("idReserva")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idReserva")
    private Reserva reserva;

    @MapsId("idProgramacionViaje")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProgramacionViaje")
    private ProgramacionViaje programacionViaje;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTipoTrayecto", nullable = false)
    private TipoTrayecto tipoTrayecto;
}
