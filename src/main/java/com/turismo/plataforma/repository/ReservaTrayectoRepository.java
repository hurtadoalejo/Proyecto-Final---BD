package com.turismo.plataforma.repository;

import com.turismo.plataforma.model.ReservaTrayecto;
import com.turismo.plataforma.model.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/** Acceso a la tabla reserva_trayecto. */
public interface ReservaTrayectoRepository extends JpaRepository<ReservaTrayecto, Long> {

    /** Kilómetros recorridos por un cliente en sus reservas con el estado indicado (null si no tiene). */
    @Query("""
           select sum(t.programacion.ruta.distancia) from ReservaTrayecto t
           where t.reserva.cliente.idUsuario = :idCliente
             and t.reserva.estadoReserva = :estado
           """)
    BigDecimal sumarDistanciaRecorrida(@Param("idCliente") Long idCliente,
                                       @Param("estado") EstadoReserva estado);
}