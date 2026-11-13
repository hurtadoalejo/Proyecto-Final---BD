package com.turismo.plataforma.model;

import com.turismo.plataforma.model.catalogo.Rol;
import com.turismo.plataforma.model.catalogo.TipoIdentificacion;
import com.turismo.plataforma.model.ubicacion.Ciudad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Superclase de la especialización Usuario -> {Cliente, Empleado}.
 * JOINED: tabla Usuario con lo común; Cliente y Empleado comparten su PK (idUsuario).
 */
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Embedded
    private NombreCompleto nombre = new NombreCompleto();

    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(name = "indicativoPais", length = 5)
    private String indicativoPais;

    /** Número celular sin indicativo. */
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    /** Hash BCrypt, nunca la contraseña en texto plano. */
    @Column(name = "contrasenia", nullable = false, length = 100)
    private String contrasenia;

    @Column(name = "direccion", length = 50)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTipoIdentificacion", nullable = false)
    private TipoIdentificacion tipoIdentificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCiudad", nullable = false)
    private Ciudad ciudad;
}
