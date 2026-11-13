package com.turismo.plataforma.model;

import com.turismo.plataforma.model.ubicacion.Ciudad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Sede")
@Getter @Setter @NoArgsConstructor
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSede")
    private Long idSede;

    @Column(name = "nombreSede", nullable = false, unique = true, length = 40)
    private String nombreSede;

    @Column(name = "direccion", nullable = false, length = 60)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCiudad", nullable = false)
    private Ciudad ciudad;
}
