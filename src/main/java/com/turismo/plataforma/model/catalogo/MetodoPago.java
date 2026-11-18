package com.turismo.plataforma.model.catalogo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catálogo: Método de pago: TARJETA_CREDITO, TARJETA_DEBITO, PSE. */
@Entity
@Table(name = "MetodoPago")
@Getter @Setter @NoArgsConstructor
public class MetodoPago {

    /** Solo se acepta en reservas hechas por un empleado (venta en sede). */
    public static final String EFECTIVO = "EFECTIVO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMetodoPago")
    private Long idMetodoPago;

    @Column(name = "nombreMetodoPago", nullable = false, unique = true, length = 20)
    private String nombreMetodoPago;
}
