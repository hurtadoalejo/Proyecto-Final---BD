package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ReservaViaje;
import com.turismo.plataforma.model.ReservaViajeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReservaViajeRepository extends JpaRepository<ReservaViaje, ReservaViajeId> {

    /** Kilómetros de los viajes de las reservas CONFIRMADAS de un cliente (null si no tiene). */
    @Query("""
           select sum(rv.programacionViaje.ruta.distancia) from ReservaViaje rv
           where rv.reserva.cliente.idUsuario = :idCliente
             and rv.reserva.estadoReserva.nombreEstadoReserva = 'CONFIRMADA'
           """)
    BigDecimal sumarDistanciaRecorrida(@Param("idCliente") Long idCliente);

    /**
     * Cambio de fecha: la fila de ReservaViaje pasa a apuntar a otra salida (idProgramacionViaje).
     * Es parte de la llave primaria, por eso se actualiza con SQL y no con la entidad.
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
           UPDATE ReservaViaje
           SET idProgramacionViaje = :idNueva
           WHERE idReserva = :idReserva AND idProgramacionViaje = :idActual
           """, nativeQuery = true)
    int cambiarSalida(@Param("idReserva") Long idReserva,
                      @Param("idActual") Long idActual,
                      @Param("idNueva") Long idNueva);
}
