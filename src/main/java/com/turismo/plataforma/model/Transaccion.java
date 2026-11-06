package com.turismo.plataforma.model;

import com.turismo.plataforma.model.enums.EstadoTransaccion;
import com.turismo.plataforma.model.enums.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
@Getter @Setter @NoArgsConstructor
public class Transaccion {

    @Id
    @Column(name = "id_reserva")
    private Long idReserva;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @NotNull @PositiveOrZero
    @Column(name = "monto_pagado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoPagado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_transaccion", nullable = false, length = 20)
    private EstadoTransaccion estadoTransaccion = EstadoTransaccion.PENDIENTE;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @OneToOne(mappedBy = "transaccion", cascade = CascadeType.ALL)
    private Factura factura;
}