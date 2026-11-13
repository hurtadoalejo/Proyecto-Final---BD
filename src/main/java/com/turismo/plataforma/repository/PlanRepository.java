package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByNombrePlanContainingIgnoreCaseOrderByPrecioExtra(String texto);

    boolean existsByNombrePlanIgnoreCase(String nombrePlan);

    boolean existsByNombrePlanIgnoreCaseAndIdPlanNot(String nombrePlan, Long idPlan);
}
