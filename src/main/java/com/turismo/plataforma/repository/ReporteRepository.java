package com.turismo.plataforma.repository;

import com.turismo.plataforma.dto.reporte.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Consultas SQL (MySQL) de los reportes. Se escriben en SQL nativo para poder usar agrupaciones,
 * subconsultas y CTE (WITH) tal como se sustentan en la materia.
 * Las ventas e ingresos cuentan solo transacciones APROBADAS; los pasajeros no cuentan reservas CANCELADAS.
 */
@Repository
public class ReporteRepository {

    /** Pasajeros y reservas por ruta (0 si no tiene). La usan los tres reportes de rutas. */
    private static final String PASAJEROS_POR_RUTA = """
            WITH pasajerosRuta AS (
                SELECT ru.idRuta,
                       co.nombreCiudad          AS origen,
                       cd.nombreCiudad          AS destino,
                       tt.nombreTipoTransporte  AS tipoTransporte,
                       COALESCE(x.reservas, 0)  AS reservas,
                       COALESCE(x.pasajeros, 0) AS pasajeros
                FROM Ruta ru
                JOIN Ciudad co         ON co.idCiudad = ru.idCiudadOrigen
                JOIN Ciudad cd         ON cd.idCiudad = ru.idCiudadDestino
                JOIN RutaVehiculo rv   ON rv.idRuta = ru.idRuta
                JOIN Vehiculo v        ON v.idVehiculo = rv.idVehiculo
                JOIN TipoTransporte tt ON tt.idTipoTransporte = v.idTipoTransporte
                LEFT JOIN (
                    SELECT pv.idRuta,
                           COUNT(DISTINCT r.idReserva) AS reservas,
                           COUNT(*)                    AS pasajeros
                    FROM DetalleReserva dr
                    JOIN ProgramacionViaje pv ON pv.idProgramacionViaje = dr.idProgramacionViaje
                    JOIN Reserva r            ON r.idReserva = dr.idReserva
                    JOIN EstadoReserva er     ON er.idEstadoReserva = r.idEstadoReserva
                    WHERE er.nombreEstadoReserva <> 'CANCELADA'
                    GROUP BY pv.idRuta
                ) x ON x.idRuta = ru.idRuta
            )
            """;

    /** Promedio de pasajeros por tipo de transporte (subconsulta sobre pasajerosRuta). */
    private static final String PROMEDIO_POR_TIPO = """
            JOIN (SELECT tipoTransporte, AVG(pasajeros) AS promedio
                  FROM pasajerosRuta
                  GROUP BY tipoTransporte) a ON a.tipoTransporte = p.tipoTransporte
            """;

    private final JdbcClient jdbcClient;

    public ReporteRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /** 1. Ventas aprobadas mes a mes. anio = null: todos los años. */
    public List<VentaMensualResponse> ventasMensuales(Integer anio) {
        return jdbcClient.sql("""
                        SELECT DATE_FORMAT(t.fechaPago, '%Y-%m') AS periodo,
                               COUNT(*)                          AS cantidadVentas,
                               SUM(t.montoPagado)                AS totalVendido
                        FROM Transaccion t
                        JOIN EstadoTransaccion et ON et.idEstadoTransaccion = t.idEstadoTransaccion
                        WHERE et.nombreEstadoTransaccion = 'APROBADA'
                          AND (:anio IS NULL OR YEAR(t.fechaPago) = :anio)
                        GROUP BY periodo
                        ORDER BY periodo
                        """)
                .param("anio", anio)
                .query(VentaMensualResponse.class)
                .list();
    }

    /** 2. Ingresos aprobados por método de pago, con su porcentaje del total. */
    public List<IngresoMetodoPagoResponse> ingresosPorMetodoPago() {
        return jdbcClient.sql("""
                        SELECT mp.nombreMetodoPago                AS metodoPago,
                               COUNT(t.idReserva)                 AS cantidadPagos,
                               COALESCE(SUM(t.montoPagado), 0)    AS totalIngresos,
                               COALESCE(ROUND(100 * SUM(t.montoPagado) / (
                                   SELECT SUM(t2.montoPagado)
                                   FROM Transaccion t2
                                   JOIN EstadoTransaccion e2 ON e2.idEstadoTransaccion = t2.idEstadoTransaccion
                                   WHERE e2.nombreEstadoTransaccion = 'APROBADA'), 2), 0) AS porcentaje
                        FROM MetodoPago mp
                        LEFT JOIN Transaccion t
                               ON t.idMetodoPago = mp.idMetodoPago
                              AND t.idEstadoTransaccion = (SELECT idEstadoTransaccion FROM EstadoTransaccion
                                                           WHERE nombreEstadoTransaccion = 'APROBADA')
                        GROUP BY mp.idMetodoPago, mp.nombreMetodoPago
                        ORDER BY totalIngresos DESC, metodoPago
                        """)
                .query(IngresoMetodoPagoResponse.class)
                .list();
    }

    /** 3. En cuántas reservas ha participado cada empleado (incluye a los que tienen 0). */
    public List<ReservasEmpleadoResponse> reservasPorEmpleado() {
        return jdbcClient.sql("""
                        SELECT e.idUsuario                                                AS idEmpleado,
                               CONCAT_WS(' ', u.primerNombre, u.primerApellido, u.segundoApellido) AS nombreEmpleado,
                               cg.nombreCargo                                             AS cargo,
                               s.nombreSede                                               AS sede,
                               COUNT(r.idReserva)                                         AS cantidadReservas,
                               COALESCE(SUM(t.montoPagado), 0)                            AS totalVendido
                        FROM Empleado e
                        JOIN Usuario u ON u.idUsuario = e.idUsuario
                        JOIN Cargo cg  ON cg.idCargo = e.idCargo
                        JOIN Sede s    ON s.idSede = e.idSede
                        LEFT JOIN Reserva r     ON r.Empleado_idUsuario = e.idUsuario
                        LEFT JOIN Transaccion t ON t.idReserva = r.idReserva
                                               AND t.idEstadoTransaccion = (SELECT idEstadoTransaccion FROM EstadoTransaccion
                                                                            WHERE nombreEstadoTransaccion = 'APROBADA')
                        GROUP BY e.idUsuario, u.primerNombre, u.primerApellido, u.segundoApellido,
                                 cg.nombreCargo, s.nombreSede
                        ORDER BY cantidadReservas DESC, nombreEmpleado
                        """)
                .query(ReservasEmpleadoResponse.class)
                .list();
    }

    /** 4. Rutas con más pasajeros (más detalles de reserva). */
    public List<RutaPasajerosResponse> rutasConMasPasajeros(int limite) {
        return jdbcClient.sql(PASAJEROS_POR_RUTA + """
                        SELECT idRuta, origen, destino, tipoTransporte, reservas, pasajeros,
                               NULL AS promedioCategoria
                        FROM pasajerosRuta
                        WHERE pasajeros > 0
                        ORDER BY pasajeros DESC, idRuta
                        LIMIT :limite
                        """)
                .param("limite", limite)
                .query(RutaPasajerosResponse.class)
                .list();
    }

    /** 5. Clientes que han gastado más que el promedio de todos los clientes. */
    public List<ClienteGastoResponse> clientesSobrePromedio() {
        return jdbcClient.sql("""
                        WITH gastoCliente AS (
                            SELECT c.idUsuario                     AS idCliente,
                                   COUNT(DISTINCT r.idReserva)     AS cantidadReservas,
                                   COALESCE(SUM(t.montoPagado), 0) AS totalGastado
                            FROM Cliente c
                            LEFT JOIN Reserva r     ON r.Cliente_idUsuario = c.idUsuario
                            LEFT JOIN Transaccion t ON t.idReserva = r.idReserva
                                                   AND t.idEstadoTransaccion = (SELECT idEstadoTransaccion FROM EstadoTransaccion
                                                                                WHERE nombreEstadoTransaccion = 'APROBADA')
                            GROUP BY c.idUsuario
                        )
                        SELECT g.idCliente,
                               CONCAT_WS(' ', u.primerNombre, u.primerApellido, u.segundoApellido) AS nombreCliente,
                               u.correo,
                               g.cantidadReservas,
                               g.totalGastado,
                               ROUND((SELECT AVG(totalGastado) FROM gastoCliente), 2) AS promedioClientes
                        FROM gastoCliente g
                        JOIN Usuario u ON u.idUsuario = g.idCliente
                        WHERE g.totalGastado > (SELECT AVG(totalGastado) FROM gastoCliente)
                        ORDER BY g.totalGastado DESC
                        """)
                .query(ClienteGastoResponse.class)
                .list();
    }

    /** 6. Rutas con menos pasajeros que el promedio de su tipo de transporte (las menos eficientes). */
    public List<RutaPasajerosResponse> rutasBajoPromedio(String tipoTransporte) {
        return jdbcClient.sql(PASAJEROS_POR_RUTA + """
                        SELECT p.idRuta, p.origen, p.destino, p.tipoTransporte, p.reservas, p.pasajeros,
                               ROUND(a.promedio, 2) AS promedioCategoria
                        FROM pasajerosRuta p
                        """ + PROMEDIO_POR_TIPO + """
                        WHERE p.pasajeros < a.promedio
                          AND (:tipo IS NULL OR p.tipoTransporte = :tipo)
                        ORDER BY p.tipoTransporte, p.pasajeros, p.idRuta
                        """)
                .param("tipo", tipoTransporte)
                .query(RutaPasajerosResponse.class)
                .list();
    }

    /** 7. Rutas más vendidas de cada tipo de transporte: las que superan el promedio de su tipo. */
    public List<RutaPasajerosResponse> rutasSobrePromedio(String tipoTransporte) {
        return jdbcClient.sql(PASAJEROS_POR_RUTA + """
                        SELECT p.idRuta, p.origen, p.destino, p.tipoTransporte, p.reservas, p.pasajeros,
                               ROUND(a.promedio, 2) AS promedioCategoria
                        FROM pasajerosRuta p
                        """ + PROMEDIO_POR_TIPO + """
                        WHERE p.pasajeros > a.promedio
                          AND (:tipo IS NULL OR p.tipoTransporte = :tipo)
                        ORDER BY p.tipoTransporte, p.pasajeros DESC, p.idRuta
                        """)
                .param("tipo", tipoTransporte)
                .query(RutaPasajerosResponse.class)
                .list();
    }
}
