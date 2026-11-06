package com.turismo.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "plan")
@Getter @Setter @NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;

    @NotBlank @Size(max = 60)
    @Column(name = "nombre_plan", nullable = false, unique = true, length = 60)
    private String nombrePlan;

    @Size(max = 255)
    @Column(name = "descripcion_plan", length = 255)
    private String descripcionPlan;

    @NotNull @PositiveOrZero
    @Column(name = "precio_extra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioExtra;

    @Column(name = "incluye_check_in", nullable = false)
    private boolean incluyeCheckIn;

    @NotNull @PositiveOrZero
    @Column(name = "peso_max_equipaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal pesoMaxEquipaje;
}