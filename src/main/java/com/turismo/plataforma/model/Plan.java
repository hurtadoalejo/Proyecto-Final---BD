package com.turismo.plataforma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Plan")
@Getter @Setter @NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlan")
    private Long idPlan;

    @Column(name = "nombrePlan", nullable = false, unique = true, length = 30)
    private String nombrePlan;

    @Column(name = "descripcionPlan", length = 200)
    private String descripcionPlan;

    /** Kilogramos. */
    @Column(name = "pesoMaxEquipaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal pesoMaxEquipaje;

    @Column(name = "incluyeCheckIn", nullable = false)
    private boolean incluyeCheckIn;

    @Column(name = "precioExtra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioExtra;
}
