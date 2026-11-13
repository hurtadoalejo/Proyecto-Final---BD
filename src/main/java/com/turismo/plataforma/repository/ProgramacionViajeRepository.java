package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ProgramacionViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    /** Búsqueda del frontend: salidas de un día entre dos ciudades (por código) en un tipo de transporte. */
    @Query("""
           select p from ProgramacionViaje p
             join fetch p.ruta r
             join fetch r.ciudadOrigen co
             join fetch r.ciudadDestino cd
             join fetch r.rutaVehiculo rv
             join fetch rv.vehiculo v
             join fetch v.tipoTransporte t
           where co.codigoCiudad = :origen
             and cd.codigoCiudad = :destino
             and t.nombreTipoTransporte = :tipoTransporte
             and p.fechaHoraSalida >= :desde and p.fechaHoraSalida < :hasta
           order by p.fechaHoraSalida
           """)
    List<ProgramacionViaje> buscarSalidas(@Param("origen") String origen,
                                          @Param("destino") String destino,
                                          @Param("tipoTransporte") String tipoTransporte,
                                          @Param("desde") LocalDateTime desde,
                                          @Param("hasta") LocalDateTime hasta);

    boolean existsByRutaIdRutaAndFechaHoraSalida(Long idRuta, LocalDateTime fechaHoraSalida);

    boolean existsByRutaIdRutaAndFechaHoraSalidaAndIdProgramacionViajeNot(
            Long idRuta, LocalDateTime fechaHoraSalida, Long idProgramacionViaje);

    /** Pasajeros ya reservados en una salida, sin contar reservas canceladas (asientos ocupados). */
    @Query("""
           select count(d) from DetalleReserva d
             join d.reserva r
             join r.viajes rv
           where rv.programacionViaje.idProgramacionViaje = :idProgramacion
             and r.estadoReserva.nombreEstadoReserva <> 'CANCELADA'
           """)
    long contarAsientosOcupados(@Param("idProgramacion") Long idProgramacion);

    /** Cuántas reservas activas tienen ese asiento en esa salida (0 = libre). */
    @Query("""
           select count(d) from DetalleReserva d
             join d.reserva r
             join r.viajes rv
           where rv.programacionViaje.idProgramacionViaje = :idProgramacion
             and upper(d.numeroAsiento) = upper(:asiento)
             and r.estadoReserva.nombreEstadoReserva <> 'CANCELADA'
           """)
    long contarAsiento(@Param("idProgramacion") Long idProgramacion, @Param("asiento") String asiento);
}
