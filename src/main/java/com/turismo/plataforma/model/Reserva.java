package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.EstadoReserva;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Reserva")
@Getter @Setter @NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReserva")
    private Long idReserva;

    @Column(name = "fechaReserva", nullable = false)
    private LocalDateTime fechaReserva = LocalDateTime.now();

    /** Empleado que atendió la reserva (opcional: el cliente puede reservar solo). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Empleado_idUsuario")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Cliente_idUsuario", nullable = false)
    private Cliente cliente;

    /** Oferta aplicada (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOferta")
    private Oferta oferta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEstadoReserva", nullable = false)
    private EstadoReserva estadoReserva;

    /** Viajes de la reserva (tabla ReservaViaje). */
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaViaje> viajes = new ArrayList<>();

    /** Pasajeros (tabla DetalleReserva). */
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id.idDetalle ASC")
    private List<DetalleReserva> detalles = new ArrayList<>();

    /** Pago de la reserva (entidad débil). */
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Transaccion transaccion;

    public void agregarViaje(ReservaViaje viaje) {
        viaje.setReserva(this);
        viajes.add(viaje);
    }

    /** Agrega un pasajero; su idDetalle es su número dentro de la reserva (1, 2, 3...). */
    public void agregarDetalle(DetalleReserva detalle) {
        detalle.setId(new DetalleReservaId((long) (detalles.size() + 1), null));
        detalle.setReserva(this);
        detalles.add(detalle);
    }

    public boolean tieneEstado(String nombreEstado) {
        return estadoReserva.getNombreEstadoReserva().equals(nombreEstado);
    }

    /** Cada pasajero paga el precio base de todos los viajes más el extra de su plan. */
    public BigDecimal calcularSubtotal() {
        BigDecimal tarifaViajes = viajes.stream()
                .map(viaje -> viaje.getProgramacionViaje().getRuta().getPrecioBase())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return detalles.stream()
                .map(detalle -> tarifaViajes.add(detalle.getPlan().getPrecioExtra()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Descuento de la oferta aplicada (0 si no tiene). */
    public BigDecimal calcularDescuento() {
        if (oferta == null) {
            return BigDecimal.ZERO.setScale(2);
        }
        return calcularSubtotal().multiply(oferta.getPorcentajeDescuento())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularValorTotal() {
        return calcularSubtotal().subtract(calcularDescuento());
    }
}
