package com.turismo.plataforma.dto.auth;

/** Datos del usuario que inició sesión. tipoUsuario: CLIENTE o EMPLEADO. */
public record LoginResponse(Long idUsuario, String nombreCompleto, String correo, String rol, String tipoUsuario) {
}
