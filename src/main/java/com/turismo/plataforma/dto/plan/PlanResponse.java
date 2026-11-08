package com.turismo.plataforma.dto.plan;

import com.turismo.plataforma.model.Plan;

import java.math.BigDecimal;

public record PlanResponse(Long idPlan, String nombrePlan, String descripcionPlan, BigDecimal precioExtra,
                           boolean incluyeCheckIn, BigDecimal pesoMaxEquipaje) {

    public static PlanResponse from(Plan plan) {
        return new PlanResponse(plan.getIdPlan(), plan.getNombrePlan(), plan.getDescripcionPlan(),
                plan.getPrecioExtra(), plan.isIncluyeCheckIn(), plan.getPesoMaxEquipaje());
    }
}