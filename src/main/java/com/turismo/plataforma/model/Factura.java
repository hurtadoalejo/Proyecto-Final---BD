package com.turismo.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Entidad débil de Transaccion: comparte la PK (que es la de la reserva). */
@Entity
@Table(name = "Factura")
@Getter @Setter @NoArgsConstructor
public class Factura {

    @Id
    @Column(name = "idReserva")
    private Long idReserva;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idReserva")
    private Transaccion transaccion;

    @Column(name = "fechaEmision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();
}
