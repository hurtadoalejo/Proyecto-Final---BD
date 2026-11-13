package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Tipo de trayecto dentro de una reserva: IDA, VUELTA. */
@Entity
@Table(name = "TipoTrayecto")
@Getter @Setter @NoArgsConstructor
public class TipoTrayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoTrayecto")
    private Long idTipoTrayecto;

    @Column(name = "nombreTipoTrayecto", nullable = false, unique = true, length = 20)
    private String nombreTipoTrayecto;
}
