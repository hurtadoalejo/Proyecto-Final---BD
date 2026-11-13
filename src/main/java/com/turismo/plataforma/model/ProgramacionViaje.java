package com.turismo.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProgramacionViaje")
@Getter @Setter @NoArgsConstructor
public class ProgramacionViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProgramacionViaje")
    private Long idProgramacionViaje;

    @Column(name = "fechaHoraSalida", nullable = false)
    private LocalDateTime fechaHoraSalida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idRuta", nullable = false)
    private Ruta ruta;

    /** Salida + duración estimada de la ruta. */
    public LocalDateTime calcularLlegada() {
        return fechaHoraSalida.plusMinutes(ruta.getDuracionEstimada());
    }
}
