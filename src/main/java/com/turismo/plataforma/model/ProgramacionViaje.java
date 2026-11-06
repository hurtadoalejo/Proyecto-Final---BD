package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "programacion_viaje",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_vehiculo", "fecha_hora_salida"}))
@Getter @Setter @NoArgsConstructor
public class ProgramacionViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programacion")
    private Long idProgramacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @NotNull
    @Column(name = "fecha_hora_salida", nullable = false)
    private LocalDateTime fechaHoraSalida;
}