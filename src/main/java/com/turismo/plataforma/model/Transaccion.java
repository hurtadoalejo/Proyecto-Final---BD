package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.EstadoTransaccion;
import com.turismo.plataforma.model.catalogo.MetodoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Entidad débil de Reserva: su PK es la de la reserva (@MapsId). */
@Entity
@Table(name = "Transaccion")
@Getter @Setter @NoArgsConstructor
public class Transaccion {

    @Id
    @Column(name = "idReserva")
    private Long idReserva;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idReserva")
    private Reserva reserva;

    @Column(name = "montoPagado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoPagado;

    @Column(name = "fechaPago")
    private LocalDateTime fechaPago;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idMetodoPago", nullable = false)
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEstadoTransaccion", nullable = false)
    private EstadoTransaccion estadoTransaccion;

    @OneToOne(mappedBy = "transaccion", cascade = CascadeType.ALL)
    private Factura factura;
}
