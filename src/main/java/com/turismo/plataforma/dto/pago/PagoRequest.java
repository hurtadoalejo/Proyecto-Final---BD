package com.turismo.plataforma.dto.pago;

import jakarta.validation.constraints.NotBlank;

/** metodoPago: un nombre de GET /api/catalogos/metodos-pago (ej.: TARJETA_CREDITO). */
public record PagoRequest(
        @NotBlank(message = "El método de pago es obligatorio")
        String metodoPago) {
}
