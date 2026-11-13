package com.turismo.plataforma.exception;

/** Correo o contraseña incorrectos en el login. HTTP 401. */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Correo o contraseña incorrectos");
    }
}
