package com.turismo.plataforma.exception;

/** Datos que no tienen sentido para el negocio (catálogo inexistente, ciudad de otro país...). HTTP 400. */
public class SolicitudInvalidaException extends RuntimeException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
