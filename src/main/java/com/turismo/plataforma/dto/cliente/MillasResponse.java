package com.turismo.plataforma.dto.cliente;

import java.math.BigDecimal;

/** Atributo derivado millasAcumuladas: se calcula, no se guarda. */
public record MillasResponse(Long idCliente, BigDecimal kilometrosRecorridos, BigDecimal millasAcumuladas) {
}
