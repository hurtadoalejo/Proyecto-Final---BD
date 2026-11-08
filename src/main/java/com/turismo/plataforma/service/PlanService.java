package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.plan.PlanRequest;
import com.turismo.plataforma.dto.plan.PlanResponse;
import com.turismo.plataforma.exception.ConflictoException;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Plan;
import com.turismo.plataforma.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<PlanResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return planRepository.findByNombrePlanContainingIgnoreCaseOrderByPrecioExtra(filtro).stream()
                .map(PlanResponse::from)
                .toList();
    }

    /** Consulta: un plan por su id. */
    public PlanResponse obtener(Long id) {
        return PlanResponse.from(buscarEntidad(id));
    }

    @Transactional
    public PlanResponse crear(PlanRequest request) {
        String nombre = request.nombrePlan().trim();
        if (planRepository.existsByNombrePlanIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe un plan llamado " + nombre);
        }
        Plan plan = new Plan();
        aplicarDatos(plan, request);
        return PlanResponse.from(planRepository.save(plan));
    }

    @Transactional
    public PlanResponse actualizar(Long id, PlanRequest request) {
        Plan plan = buscarEntidad(id);
        String nombre = request.nombrePlan().trim();
        if (planRepository.existsByNombrePlanIgnoreCaseAndIdPlanNot(nombre, id)) {
            throw new ConflictoException("Ya existe otro plan llamado " + nombre);
        }
        aplicarDatos(plan, request);
        return PlanResponse.from(plan);
    }

    @Transactional
    public void eliminar(Long id) {
        Plan plan = buscarEntidad(id);
        planRepository.delete(plan);
        planRepository.flush();
    }

    private Plan buscarEntidad(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el plan con id " + id));
    }

    private void aplicarDatos(Plan plan, PlanRequest request) {
        plan.setNombrePlan(request.nombrePlan().trim());
        plan.setDescripcionPlan(request.descripcionPlan());
        plan.setPrecioExtra(request.precioExtra());
        plan.setIncluyeCheckIn(request.incluyeCheckIn());
        plan.setPesoMaxEquipaje(request.pesoMaxEquipaje());
    }
}