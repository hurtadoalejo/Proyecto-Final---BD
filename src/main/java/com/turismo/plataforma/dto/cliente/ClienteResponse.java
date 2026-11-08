package com.turismo.plataforma.dto.cliente;

import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.NombreCompleto;

import java.time.LocalDate;

public record ClienteResponse(Long idUsuario, String primerNombre, String segundoNombre,
                              String primerApellido, String segundoApellido, String identificacion,
                              LocalDate fechaNacimiento, String direccion, String telefono, String correo) {

    public static ClienteResponse from(Cliente cliente) {
        NombreCompleto nombre = cliente.getNombre();
        return new ClienteResponse(cliente.getIdUsuario(), nombre.getPrimerNombre(), nombre.getSegundoNombre(),
                nombre.getPrimerApellido(), nombre.getSegundoApellido(), cliente.getIdentificacion(),
                cliente.getFechaNacimiento(), cliente.getDireccion(), cliente.getTelefono(), cliente.getCorreo());
    }
}