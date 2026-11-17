package com.turismo.plataforma.model.ubicacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Continente")
@Getter @Setter @NoArgsConstructor
public class Continente {

    public static final String EUROPA = "Europa";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContinente")
    private Long idContinente;

    @Column(name = "nombreContinente", nullable = false, unique = true, length = 25)
    private String nombreContinente;
}
