package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sede")
@Getter @Setter @NoArgsConstructor
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Long idSede;

    @NotBlank @Size(max = 100)
    @Column(name = "nombre_sede", nullable = false, length = 100)
    private String nombreSede;

    @NotBlank @Size(max = 150)
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    @OneToMany(mappedBy = "sede")
    private List<Empleado> empleados = new ArrayList<>();
}