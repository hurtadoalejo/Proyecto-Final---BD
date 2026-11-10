package com.turismo.plataforma.dto.pago;

import com.turismo.plataforma.model.enums.MetodoPago;
import jakarta.validation.constraints.NotNull;

/** El frontend solo elige el metodo: el monto lo calcula el servidor a partir de la reserva. */
public record PagoRequest(
        @NotNull(message = "El método de pago es obligatorio (TARJETA_CREDITO, TARJETA_DEBITO o PSE)")
        MetodoPago metodoPago) {
}