package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Tipo de documento: CC, CE, TI, PA, PEP. */
@Entity
@Table(name = "TipoIdentificacion")
@Getter @Setter @NoArgsConstructor
public class TipoIdentificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoIdentificacion")
    private Long idTipoIdentificacion;

    @Column(name = "nombreIdentificacion", nullable = false, unique = true, length = 40)
    private String nombreIdentificacion;
}
