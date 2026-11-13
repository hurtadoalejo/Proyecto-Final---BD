package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ReservaViaje;
import com.turismo.plataforma.model.ReservaViajeId;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
