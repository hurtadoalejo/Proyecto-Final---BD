package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Tipo de transporte: AVION, BUS, TREN. */
@Entity
@Table(name = "TipoTransporte")
@Getter @Setter @NoArgsConstructor
public class TipoTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoTransporte")
    private Long idTipoTransporte;

    @Column(name = "nombreTipoTransporte", nullable = false, unique = true, length = 25)
    private String nombreTipoTransporte;
}
