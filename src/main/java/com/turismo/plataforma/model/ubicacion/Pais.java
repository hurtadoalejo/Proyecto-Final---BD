package com.turismo.plataforma.model.ubicacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pais")
@Getter @Setter @NoArgsConstructor
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPais")
    private Long idPais;

    @Column(name = "nombrePais", nullable = false, unique = true, length = 25)
    private String nombrePais;

    /** Indicativo telefónico del país (ej.: +57). */
    @Column(name = "indicativo", nullable = false, length = 5)
    private String indicativo;
}
