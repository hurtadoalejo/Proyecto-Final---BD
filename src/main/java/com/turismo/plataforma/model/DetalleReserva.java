package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Un pasajero en uno de los viajes de la reserva (ida y regreso = dos detalles por pasajero). */
@Entity
@Table(name = "DetalleReserva")
@Getter @Setter @NoArgsConstructor
public class DetalleReserva {

    @EmbeddedId
    private DetalleReservaId id;

    @MapsId("idReserva")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idReserva")
    private Reserva reserva;

    /** Salida en la que viaja este pasajero (con su asiento). FK a ReservaViaje en la BD. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProgramacionViaje", nullable = false)
    private ProgramacionViaje programacionViaje;

    @Embedded
    private NombreCompleto nombre = new NombreCompleto();

    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;

    @Column(name = "numeroAsiento", nullable = false, length = 10)
    private String numeroAsiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTipoIdentificacion", nullable = false)
    private TipoIdentificacion tipoIdentificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPlan", nullable = false)
    private Plan plan;
}
