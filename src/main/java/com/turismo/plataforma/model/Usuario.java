package com.turismo.plataforma.model;

import com.turismo.plataforma.model.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Valid
    @Embedded
    private NombreCompleto nombre = new NombreCompleto();

    @NotBlank @Size(max = 20)
    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @NotNull @Past
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Size(max = 150)
    @Column(name = "direccion", length = 150)
    private String direccion;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @NotBlank @Email @Size(max = 120)
    @Column(name = "correo", nullable = false, unique = true, length = 120)
    private String correo;


    @Column(name = "contrasenia", nullable = false, length = 100)
    private String contrasenia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Rol rol;
}