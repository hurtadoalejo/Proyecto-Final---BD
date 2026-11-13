package com.turismo.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Vehículo asignado a una ruta. Su PK es la de la ruta (@MapsId). */
@Entity
@Table(name = "RutaVehiculo")
@Getter @Setter @NoArgsConstructor
public class RutaVehiculo {

    @Id
    @Column(name = "idRuta")
    private Long idRuta;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idRuta")
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehiculo", nullable = false)
    private Vehiculo vehiculo;
}
