package com.turismo.plataforma.repository;
import java.time.LocalDateTime;

import com.turismo.plataforma.model.ProgramacionViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramacionViajeRepository extends JpaRepository<ProgramacionViaje, Long> {

    @Query("""
           select p from ProgramacionViaje p
             join fetch p.ruta
             join fetch p.vehiculo
           order by p.fechaHoraSalida
           """)
    List<ProgramacionViaje> listarConRutaYVehiculo();

    @Query("""
           select p from ProgramacionViaje p
             join fetch p.ruta r
             join fetch p.vehiculo
           where r.idRuta = :idRuta
           order by p.fechaHoraSalida
           """)
    List<ProgramacionViaje> listarPorRuta(@Param("idRuta") Long idRuta);

    boolean existsByVehiculoIdVehiculoAndFechaHoraSalida(Long idVehiculo, LocalDateTime fechaHoraSalida);

    boolean existsByVehiculoIdVehiculoAndFechaHoraSalidaAndIdProgramacionNot(
            Long idVehiculo, LocalDateTime fechaHoraSalida, Long idProgramacion);
}