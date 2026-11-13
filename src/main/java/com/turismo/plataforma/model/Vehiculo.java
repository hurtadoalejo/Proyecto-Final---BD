package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.TipoTransporte;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Vehiculo")
@Getter @Setter @NoArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVehiculo")
    private Long idVehiculo;

    @Column(name = "codigoIdentificacion", nullable = false, unique = true, length = 25)
    private String codigoIdentificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTipoTransporte", nullable = false)
    private TipoTransporte tipoTransporte;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;
}
