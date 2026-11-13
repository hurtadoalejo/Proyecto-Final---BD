package com.turismo.plataforma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Nombres y apellidos (atributo compuesto). Lo usan Usuario y DetalleReserva. */
@Embeddable
@Getter @Setter @NoArgsConstructor
public class NombreCompleto {

    @Column(name = "primerNombre", nullable = false, length = 20)
    private String primerNombre;

    @Column(name = "segundoNombre", length = 20)
    private String segundoNombre;

    @Column(name = "primerApellido", nullable = false, length = 20)
    private String primerApellido;

    @Column(name = "segundoApellido", length = 20)
    private String segundoApellido;

    /** "Juan Pérez" o "Juan Carlos Pérez Gómez": omite lo que viene vacío. */
    public String completo() {
        StringBuilder sb = new StringBuilder(primerNombre);
        if (segundoNombre != null) sb.append(' ').append(segundoNombre);
        sb.append(' ').append(primerApellido);
        if (segundoApellido != null) sb.append(' ').append(segundoApellido);
        return sb.toString();
    }
}
