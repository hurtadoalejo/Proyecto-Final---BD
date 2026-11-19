package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ProgramacionViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ProgramacionViajeRepository extends JpaRepository<ProgramacionViaje, Long> {

    /** Salidas entre dos fechas (de una ruta, o de todas si idRuta es null), con ruta, ciudades y vehículo. */
    @Query("""
           select p from ProgramacionViaje p
             join fetch p.ruta r
             join fetch r.ciudadOrigen
             join fetch r.ciudadDestino
             left join fetch r.rutaVehiculo rv
             left join fetch rv.vehiculo v
             left join fetch v.tipoTransporte
           where (:idRuta is null or r.idRuta = :idRuta)
             and p.fechaHoraSalida >= :desde and p.fechaHoraSalida < :hasta
           order by p.fechaHoraSalida, r.idRuta
           """)
    List<ProgramacionViaje> listar(@Param("idRuta") Long idRuta,
                                   @Param("desde") LocalDateTime desde,
                                   @Param("hasta") LocalDateTime hasta);

    /** Búsqueda del frontend: salidas de un día entre dos ciudades (por nombre) en un tipo de transporte. */
    @Query("""
           select p from ProgramacionViaje p
             join fetch p.ruta r
             join fetch r.ciudadOrigen co
             join fetch r.ciudadDestino cd
             join fetch r.rutaVehiculo rv
             join fetch rv.vehiculo v
             join fetch v.tipoTransporte t
           where co.idCiudad in :origenes
             and cd.idCiudad in :destinos
             and t.nombreTipoTransporte = :tipoTransporte
             and p.fechaHoraSalida >= :desde and p.fechaHoraSalida < :hasta
           order by p.fechaHoraSalida
           """)
    List<ProgramacionViaje> buscarSalidas(@Param("origenes") Collection<Long> origenes,
                                          @Param("destinos") Collection<Long> destinos,
                                          @Param("tipoTransporte") String tipoTransporte,
                                          @Param("desde") LocalDateTime desde,
                                          @Param("hasta") LocalDateTime hasta);

    boolean existsByRutaIdRutaAndFechaHoraSalida(Long idRuta, LocalDateTime fechaHoraSalida);

    boolean existsByRutaIdRutaAndFechaHoraSalidaAndIdProgramacionViajeNot(
            Long idRuta, LocalDateTime fechaHoraSalida, Long idProgramacionViaje);

    /**
     * Salidas del mismo vehículo que chocan con un viaje nuevo: entre la llegada de un viaje y la
     * salida del siguiente deben pasar al menos minutosDescanso. limiteSuperior = llegada nueva + descanso.
     * Devuelve como máximo una (la primera que choca).
     */
    @Query(nativeQuery = true, value = """
           SELECT pv.* FROM ProgramacionViaje pv
           JOIN Ruta r          ON r.idRuta = pv.idRuta
           JOIN RutaVehiculo rv ON rv.idRuta = r.idRuta
           WHERE rv.idVehiculo = :idVehiculo
             AND pv.idProgramacionViaje <> :idExcluir
             AND pv.fechaHoraSalida < :limiteSuperior
             AND DATE_ADD(pv.fechaHoraSalida, INTERVAL (r.duracionEstimada + :minutosDescanso) MINUTE) > :salida
           ORDER BY pv.fechaHoraSalida
           LIMIT 1
           """)
    List<ProgramacionViaje> buscarCruceVehiculo(@Param("idVehiculo") Long idVehiculo,
                                                @Param("idExcluir") Long idExcluir,
                                                @Param("salida") LocalDateTime salida,
                                                @Param("limiteSuperior") LocalDateTime limiteSuperior,
                                                @Param("minutosDescanso") int minutosDescanso);

    List<ProgramacionViaje> findByRutaIdRuta(Long idRuta);

    /** Pasajeros ya reservados en una salida, sin contar reservas canceladas (asientos ocupados). */
    @Query("""
           select count(d) from DetalleReserva d
             join d.reserva r
             join r.viajes rv
           where rv.programacionViaje.idProgramacionViaje = :idProgramacion
             and r.estadoReserva.nombreEstadoReserva <> 'CANCELADA'
           """)
    long contarAsientosOcupados(@Param("idProgramacion") Long idProgramacion);

    /** Asientos ocupados en una salida (en mayúsculas), sin contar reservas canceladas. */
    @Query("""
           select upper(d.numeroAsiento) from DetalleReserva d
             join d.reserva r
             join r.viajes rv
           where rv.programacionViaje.idProgramacionViaje = :idProgramacion
             and r.estadoReserva.nombreEstadoReserva <> 'CANCELADA'
           """)
    List<String> listarAsientosOcupados(@Param("idProgramacion") Long idProgramacion);
}
