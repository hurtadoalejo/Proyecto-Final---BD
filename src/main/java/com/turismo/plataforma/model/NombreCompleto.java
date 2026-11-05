package com.turismo.plataforma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class NombreCompleto {

    @NotBlank @Size(max = 50)
    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Size(max = 50)
    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @NotBlank @Size(max = 50)
    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;

    @Size(max = 50)
    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;
}