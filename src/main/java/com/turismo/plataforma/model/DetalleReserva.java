package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_reserva")
@Getter @Setter @NoArgsConstructor
public class DetalleReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @Valid
    @Embedded
    private NombreCompleto nombre = new NombreCompleto();

    @NotBlank @Size(max = 20)
    @Column(name = "identificacion", nullable = false, length = 20)
    private String identificacion;

    @NotBlank @Size(max = 5)
    @Column(name = "numero_asiento", nullable = false, length = 5)
    private String numeroAsiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;
}