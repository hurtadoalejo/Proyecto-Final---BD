package com.turismo.plataforma.model;

import com.turismo.plataforma.model.enums.TipoTrayecto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva_trayecto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_reserva", "id_programacion"}))
@Getter @Setter @NoArgsConstructor
public class ReservaTrayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_trayecto")
    private Long idReservaTrayecto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_programacion", nullable = false)
    private ProgramacionViaje programacion;

    @NotNull @Min(1)
    @Column(name = "orden_trayecto", nullable = false)
    private Integer ordenTrayecto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_trayecto", nullable = false, length = 20)
    private TipoTrayecto tipoTrayecto;
}