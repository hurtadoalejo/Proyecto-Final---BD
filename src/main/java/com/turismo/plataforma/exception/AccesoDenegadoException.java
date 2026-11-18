package com.turismo.plataforma.exception;

/** 403: el usuario no puede hacer esta acción sobre el recurso (por ejemplo, la reserva de otro cliente). */
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
