package com.turismo.plataforma.model.ubicacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ciudad")
@Getter @Setter @NoArgsConstructor
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCiudad")
    private Long idCiudad;

    @Column(name = "nombreCiudad", nullable = false, length = 40)
    private String nombreCiudad;

    /** Código de 3 letras que usa el frontend (BOG, MAD...). Solo lo tienen las ciudades con rutas. */
    @Column(name = "codigoCiudad", unique = true, length = 3)
    private String codigoCiudad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDepartamento", nullable = false)
    private Departamento departamento;
}
