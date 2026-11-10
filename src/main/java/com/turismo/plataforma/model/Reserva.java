package com.turismo.plataforma.model;

import com.turismo.plataforma.model.enums.EstadoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva")
@Getter @Setter @NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @NotNull
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false, length = 20)
    private EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;

    /** No aparece en el diagrama, pero es como sabemos que  cliente reservo??
     * por eso la agregue. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    /** Reserva (N) TIENE ProgramacionViaje (N) con ordenTrayecto y tipoTrayecto. */
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordenTrayecto ASC")
    private List<ReservaTrayecto> trayectos = new ArrayList<>();

    /** Reserva (1) TIENE DetalleReserva (N): un detalle por pasajero. */
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReserva> detalles = new ArrayList<>();

    /** Reserva (1) TIENE Transaccion (1) - Transaccion es entidad débil. */
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Transaccion transaccion;

    /** Agrega un trayecto y enlaza los dos lados de la relación. */
    public void agregarTrayecto(ReservaTrayecto trayecto) {
        trayecto.setReserva(this);
        trayectos.add(trayecto);
    }

    /** Agrega un pasajero y enlaza los dos lados de la relación. */
    public void agregarDetalle(DetalleReserva detalle) {
        detalle.setReserva(this);
        detalles.add(detalle);
    }

    /** Cada pasajero paga el precio base de todos los trayectos más el extra de su plan. */
    public BigDecimal calcularValorTotal() {
        BigDecimal tarifaTrayectos = trayectos.stream()
                .map(trayecto -> trayecto.getProgramacion().getRuta().getPrecioBase())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return detalles.stream()
                .map(detalle -> tarifaTrayectos.add(detalle.getPlan().getPrecioExtra()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}