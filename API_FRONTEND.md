# API del backend – guía para el frontend

Base URL: `http://localhost:8080`  ·  Todo es JSON  ·  Documentación interactiva: `http://localhost:8080/swagger-ui.html`

CORS abierto: el front puede llamar desde cualquier origen (cualquier puerto de localhost, Live Server, etc.). No hay Spring Security: ningún endpoint pide autenticación.

No hay token: el login valida correo y contraseña y devuelve los datos del usuario; el front los guarda (por ejemplo en `localStorage`) y usa `idUsuario` en las demás llamadas.

---

## 1. Registro – `POST /api/auth/registro`

Mismo JSON del formulario. `pais`, `departamento` y `ciudad` son **ids** de las listas de `/api/catalogos` (ver sección 4). `segundoNombre` y `segundoApellido` pueden ir en `null`. `indicativoPais` se guarda en su propia columna.

```json
{
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "1094912345",
  "primerNombre": "Juan",
  "segundoNombre": null,
  "primerApellido": "Pérez",
  "segundoApellido": null,
  "pais": 1,
  "departamento": 25,
  "ciudad": 3,
  "direccion": "Calle 15 # 12-34, Barrio Centro",
  "indicativoPais": "+57",
  "telefonoCelular": "3001234567",
  "correo": "correo@ejemplo.com",
  "contrasenia": "miPassword123"
}
```

Validaciones: `tipoIdentificacion` ∈ CC, CE, TI, PA, PEP · nombres máx. 20 · `indicativoPais` con formato `+57` · `telefonoCelular` de 7 a 15 dígitos sin espacios · `contrasenia` de 8 a 72 caracteres · la ciudad debe pertenecer al departamento y el departamento al país.

**201 Created**

```json
{
  "idUsuario": 2,
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "1094912345",
  "primerNombre": "Juan", "segundoNombre": null,
  "primerApellido": "Pérez", "segundoApellido": null,
  "pais": 1, "nombrePais": "Colombia",
  "departamento": 25, "nombreDepartamento": "Quindío",
  "ciudad": 3, "nombreCiudad": "Armenia",
  "direccion": "Calle 15 # 12-34, Barrio Centro",
  "indicativoPais": "+57",
  "telefonoCelular": "3001234567",
  "correo": "correo@ejemplo.com",
  "rol": "CLIENTE"
}
```

Errores: **400** datos inválidos · **409** correo o identificación ya registrados.

## 2. Login – `POST /api/auth/login`

```json
{ "correo": "correo@ejemplo.com", "contrasenia": "miPassword123" }
```

**200 OK**

```json
{ "idUsuario": 2, "nombreCompleto": "Juan Pérez", "correo": "correo@ejemplo.com", "rol": "CLIENTE", "tipoUsuario": "CLIENTE" }
```

`rol`: CLIENTE, EMPLEADO o ADMIN. `tipoUsuario`: CLIENTE o EMPLEADO. Si el correo o la contraseña no coinciden: **401**.

Administrador creado al arrancar: `admin@viajacol.com` / `AdminViajaCol2026` (rol ADMIN).

## 3. Búsqueda de viajes – `POST /api/rutas/buscar`

```json
{
  "tipoTransporte": "AVION",
  "pasajeros": 1,
  "tramos": [
    { "origen": "BOG", "destino": "MDE", "fecha": "2026-11-20" },
    { "origen": "MDE", "destino": "BOG", "fecha": "2026-11-25" }
  ]
}
```

`tipoTransporte`: AVION, BUS o TREN · `pasajeros`: 1 a 9 · `tramos`: 1 a 6, con fechas en orden y no pasadas · `origen`/`destino`: códigos de 3 letras (ver `/api/catalogos/ciudades-destino`).

**200 OK** – un bloque por tramo, en el mismo orden. Si un tramo no tiene salidas con cupo ese día, `viajes` viene vacío.

```json
{
  "tipoTransporte": "AVION",
  "pasajeros": 1,
  "tramos": [
    {
      "origen": "BOG", "ciudadOrigen": "Bogotá",
      "destino": "MDE", "ciudadDestino": "Medellín",
      "fecha": "2026-11-20",
      "viajes": [
        {
          "idProgramacionViaje": 1234,
          "idRuta": 1,
          "fechaHoraSalida": "2026-11-20T06:30:00",
          "fechaHoraLlegada": "2026-11-20T07:25:00",
          "duracionEstimada": 55,
          "direccionOrigen": "Aeropuerto El Dorado",
          "direccionDestino": "Aeropuerto José María Córdova",
          "tipoTransporte": "AVION",
          "codigoVehiculo": "HK-5120",
          "distancia": 215.00,
          "precioBase": 280000.00,
          "precioTotal": 280000.00,
          "asientosDisponibles": 180
        }
      ]
    },
    { "origen": "MDE", "ciudadOrigen": "Medellín", "destino": "BOG", "ciudadDestino": "Bogotá", "fecha": "2026-11-25", "viajes": [ ... ] }
  ]
}
```

`duracionEstimada` en minutos · `distancia` en km · `precioTotal` = `precioBase` × pasajeros (sin plan ni oferta). El `idProgramacionViaje` elegido es el que se manda al crear la reserva.

Hay salidas todos los días desde el 13-nov-2026 hasta el 31-ene-2027: avión 06:30 y 18:15, tren 07:00, 13:30 y 19:45, bus 08:00 y 22:30.

## 4. Catálogos (listas desplegables) – `GET`

| Endpoint | Devuelve |
|---|---|
| `/api/catalogos/paises` | `[{ "id": 1, "nombre": "Colombia" }, ...]` |
| `/api/catalogos/paises/{idPais}/departamentos` | `[{ "id": 25, "nombre": "Quindío" }, ...]` |
| `/api/catalogos/departamentos/{idDepartamento}/ciudades` | `[{ "id": 3, "nombre": "Armenia" }, ...]` |
| `/api/catalogos/ciudades-destino` | ciudades con código para la búsqueda: `[{ "idCiudad", "nombreCiudad", "codigoCiudad": "MAD", "idDepartamento", "nombreDepartamento", "idPais", "nombrePais" }]` |
| `/api/catalogos/tipos-identificacion` | CC, CE, TI, PA, PEP |
| `/api/catalogos/tipos-transporte` | AVION, BUS, TREN |
| `/api/catalogos/tipos-trayecto` | IDA, VUELTA |
| `/api/catalogos/metodos-pago` | PSE, TARJETA_CREDITO, TARJETA_DEBITO |
| `/api/catalogos/roles` · `/cargos` · `/estados-reserva` · `/estados-transaccion` · `/estados-notificacion` | `{ id, nombre }` |

Los ids de ejemplo de este documento pueden cambiar; tómalos siempre de estas listas.

## 5. Reserva y pago

### Crear reserva – `POST /api/reservas`

```json
{
  "idCliente": 2,
  "idEmpleado": null,
  "idOferta": 1,
  "viajes": [
    { "idProgramacionViaje": 1234, "tipoTrayecto": "IDA" },
    { "idProgramacionViaje": 1890, "tipoTrayecto": "VUELTA" }
  ],
  "pasajeros": [
    {
      "tipoIdentificacion": "CC", "numeroIdentificacion": "1094912345",
      "primerNombre": "Juan", "segundoNombre": null,
      "primerApellido": "Pérez", "segundoApellido": null,
      "numeroAsiento": "12A",
      "idPlan": 1
    }
  ]
}
```

Cada pasajero viaja en todos los viajes de la reserva con el mismo asiento y plan. `idEmpleado` (quien atiende) e `idOferta` son opcionales; la oferta debe estar vigente (`GET /api/ofertas?vigentes=true`). Planes: `GET /api/planes`.

**201 Created** – la reserva queda **PENDIENTE**:

```json
{
  "idReserva": 1, "fechaReserva": "2026-11-12T15:40:12", "estadoReserva": "PENDIENTE",
  "idCliente": 2, "nombreCliente": "Juan Pérez", "idEmpleado": null, "nombreEmpleado": null,
  "idOferta": 1, "tituloOferta": "Bienvenida",
  "viajes": [
    { "idProgramacionViaje": 1234, "tipoTrayecto": "IDA", "codigoOrigen": "BOG", "ciudadOrigen": "Bogotá",
      "codigoDestino": "MDE", "ciudadDestino": "Medellín", "fechaHoraSalida": "2026-11-20T06:30:00",
      "fechaHoraLlegada": "2026-11-20T07:25:00", "precioBase": 280000.00 }
  ],
  "pasajeros": [
    { "idDetalle": 1, "tipoIdentificacion": "CC", "numeroIdentificacion": "1094912345", "nombreCompleto": "Juan Pérez",
      "numeroAsiento": "12A", "idPlan": 1, "nombrePlan": "Básico", "precioExtra": 0.00 }
  ],
  "subtotal": 560000.00, "descuento": 56000.00, "valorTotal": 504000.00,
  "pago": null
}
```

`subtotal` = por cada pasajero (suma de `precioBase` de los viajes + `precioExtra` del plan) · `descuento` = subtotal × % de la oferta.

Errores: **400** datos o ids inválidos, asiento repetido dentro de la reserva, salida que ya partió · **409** sin cupo o asiento ya ocupado en esa salida.

### Pagar – `POST /api/reservas/{idReserva}/pago`

```json
{ "metodoPago": "TARJETA_CREDITO" }
```

**201 Created** – la reserva pasa a **CONFIRMADA** y se emite la factura:

```json
{ "idReserva": 1, "montoPagado": 504000.00, "fechaPago": "2026-11-12T15:42:00", "metodoPago": "TARJETA_CREDITO",
  "estadoTransaccion": "APROBADA", "numeroFactura": "FV-000001", "fechaEmision": "2026-11-12T15:42:00" }
```

### Otras

| Método y ruta | Uso |
|---|---|
| `GET /api/reservas?idCliente=2` | "Mis reservas": `[{ idReserva, fechaReserva, estadoReserva, idCliente, nombreCliente, pagada }]` (sin `idCliente` = todas) |
| `GET /api/reservas/{id}` | Detalle completo (mismo formato de la creación; `pago` lleno si ya pagó) |
| `PUT /api/reservas/{id}/cancelar` | Solo reservas PENDIENTES → CANCELADA (libera los asientos) |
| `DELETE /api/reservas/{id}` | Solo si no tiene pago |
| `GET /api/reservas/{id}/pago` | Pago y factura |
| `GET /api/clientes/{id}/millas` | `{ idCliente, kilometrosRecorridos, millasAcumuladas }` (reservas confirmadas) |

## 6. Administración (CRUD)

Todos siguen el mismo patrón: `GET /api/x` (lista, con filtro opcional), `GET /api/x/{id}`, `POST /api/x` (201), `PUT /api/x/{id}`, `DELETE /api/x/{id}` (204).

| Recurso | Cuerpo de POST/PUT | Filtro del GET |
|---|---|---|
| `/api/clientes` (sin POST: se usa el registro) | igual al registro, sin `contrasenia` | `?q=` identificación o apellido |
| `/api/empleados` | registro + `salario`, `fechaContratacion` ("2026-01-15"), `idCargo`, `idSede`, `rol` (EMPLEADO o ADMIN). En PUT sin `contrasenia` | `?idSede=` |
| `/api/sedes` | `{ nombreSede, direccion, idCiudad }` | `?q=` |
| `/api/planes` | `{ nombrePlan, descripcionPlan, pesoMaxEquipaje, incluyeCheckIn, precioExtra }` | `?q=` |
| `/api/ofertas` | `{ titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin }` | `?vigentes=true` |
| `/api/vehiculos` | `{ codigoIdentificacion, tipoTransporte, capacidad }` | `?q=` |
| `/api/rutas` | `{ idCiudadOrigen, direccionOrigen, idCiudadDestino, direccionDestino, distancia, duracionEstimada, precioBase, idVehiculo }` | `?q=` ciudad o código |
| `/api/programaciones` | `{ idRuta, fechaHoraSalida: "2026-12-20T08:30:00" }` | `?idRuta=&desde=2026-12-01&hasta=2026-12-07` (máx. 31 días; por defecto los próximos 7) |
| `/api/notificaciones` | `{ mensaje, estadoNotificacion, idOferta, idsClientes: [2, 5] }` | `?idCliente=` |

Extras de programación:

- `POST /api/programaciones/recurrente` – **repite una ruta cada cierto tiempo**:
  ```json
  { "idRuta": 1, "fechaHoraInicio": "2026-12-01T06:00:00", "fechaHoraFin": "2026-12-31T23:59:00", "cadaHoras": 24 }
  ```
  `cadaHoras`: 24 = diaria, 168 = semanal, 8 = tres al día. Respuesta: `{ "creadas": 31, "omitidas": 0, "salidas": [...] }` (las que ya existían se omiten).
- `GET /api/programaciones/{id}/asientos` → `{ idProgramacionViaje, capacidad, asientosOcupados, asientosDisponibles }`.

## 7. Formato de errores

Todos los errores usan el mismo formato (`application/problem+json`):

```json
{ "type": "about:blank", "title": "Datos inválidos", "status": 400, "detail": "Revisa los campos marcados.",
  "instance": "/api/auth/registro",
  "errores": { "correo": "El correo no es válido", "telefonoCelular": "El celular debe tener entre 7 y 15 dígitos, sin espacios" } }
```

`errores` (campo → mensaje) solo aparece en errores de validación. Códigos: **400** datos inválidos · **401** login incorrecto · **404** no existe · **409** conflicto (duplicado, sin cupo, registro en uso).
