# API del backend – guía para el frontend

Base URL: `http://localhost:8080`  ·  Todo es JSON  ·  Documentación interactiva: `http://localhost:8080/swagger-ui.html`

CORS abierto: el front puede llamar desde cualquier origen (cualquier puerto de localhost, Live Server, etc.). No hay Spring Security: ningún endpoint pide autenticación.

No hay token: el login valida correo y contraseña y devuelve los datos del usuario; el front los guarda (por ejemplo en `localStorage`) y usa `idUsuario` en las demás llamadas.

---

## 1. Registro – `POST /api/auth/registro`

Se envía **el mismo objeto `formData` de `Register.tsx`**, tal cual:

```json
{
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "1094912345",
  "primerNombre": "Juan",
  "segundoNombre": "",
  "primerApellido": "Pérez",
  "segundoApellido": "",
  "fechaNacimiento": "2000-05-31",
  "pais": "Colombia",
  "departamento": "Quindío",
  "ciudad": "Armenia",
  "direccion": "Calle 15 # 12-34, Barrio Centro",
  "indicativo": "+57",
  "telefono": "300 123 4567",
  "correo": "correo@ejemplo.com",
  "contrasena": "miPassword123"
}
```

- `pais`, `departamento` y `ciudad` van **por nombre**. La base trae por defecto exactamente los 8 países, 80 departamentos y 160 ciudades de `UBICACIONES` en `Register.tsx`, así que no hace falta pedir listas al backend.
- `segundoNombre` y `segundoApellido` pueden ir vacíos (`""`) o en `null`.
- `fechaNacimiento` es el valor del `<input type="date">` (`"AAAA-MM-DD"`).
- `telefono` acepta espacios y guiones; se guarda junto al indicativo: `"+57 3001234567"`.

Validaciones: `tipoIdentificacion` ∈ CC, CE, TI (también PA, PEP) · nombres máx. 20 · `fechaNacimiento` obligatoria y en el pasado · `indicativo` con formato `+57` · `telefono` de 7 a 14 dígitos · `contrasena` de 8 a 50 caracteres · la ciudad debe existir en ese departamento y el departamento en ese país.

**201 Created** – mismos nombres de campo, sin la contraseña:

```json
{
  "idUsuario": 2,
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "1094912345",
  "primerNombre": "Juan", "segundoNombre": null,
  "primerApellido": "Pérez", "segundoApellido": null,
  "fechaNacimiento": "2000-05-31",
  "pais": "Colombia", "departamento": "Quindío", "ciudad": "Armenia",
  "direccion": "Calle 15 # 12-34, Barrio Centro",
  "indicativo": "+57", "telefono": "3001234567",
  "correo": "correo@ejemplo.com",
  "rol": "CLIENTE"
}
```

Errores: **400** datos inválidos (el detalle dice qué campo) · **409** correo o identificación ya registrados.

Ejemplo de llamada desde `handleSubmit`:

```ts
const res = await fetch('http://localhost:8080/api/auth/registro', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(formData),
});
if (!res.ok) {
  const error = await res.json();
  throw new Error(error.errores ? Object.values(error.errores).join('\n') : error.detail);
}
```

## 2. Login – `POST /api/auth/login`

Se envía el `formData` de `Login.tsx`:

```json
{ "correo": "correo@ejemplo.com", "contrasena": "miPassword123" }
```

**200 OK**

```json
{ "idUsuario": 2, "nombreCompleto": "Juan Pérez", "correo": "correo@ejemplo.com", "rol": "CLIENTE", "tipoUsuario": "CLIENTE" }
```

`rol`: CLIENTE, EMPLEADO o ADMIN. `tipoUsuario`: CLIENTE o EMPLEADO. Si el correo o la contraseña no coinciden: **401**.

Administradora: `admin@viajacol.com` / `AdminViajaCol2026` (rol ADMIN). Viene en `02_datos.sql` y, si la base no tiene ningún ADMIN, la aplicación también la crea al arrancar.

## 3. Búsqueda de viajes – `POST /api/rutas/buscar`

`origen` y `destino` van **por nombre de ciudad**, el `name` de `EUROPEAN_CITIES` (el `code` ya no se usa):

```json
{
  "tipoTransporte": "avion",
  "pasajeros": 1,
  "tramos": [
    { "origen": "Madrid", "destino": "París", "fecha": "2026-12-10" },
    { "origen": "París", "destino": "Madrid", "fecha": "2026-12-15" }
  ]
}
```

`tipoTransporte`: AVION, BUS o TREN (también en minúscula, como el `TransportMode` del front) · `pasajeros`: 1 a 9 · `tramos`: 1 a 6, con fechas en orden y no pasadas · mayúsculas y tildes no importan (`"paris"` = `"París"`).

**200 OK** – un bloque por tramo, en el mismo orden. Si un tramo no tiene salidas con cupo ese día (o esas ciudades no tienen ruta en ese transporte), `viajes` viene vacío.

```json
{
  "tipoTransporte": "AVION",
  "pasajeros": 1,
  "tramos": [
    {
      "origen": "Madrid", "paisOrigen": "España",
      "destino": "París", "paisDestino": "Francia",
      "fecha": "2026-12-10",
      "viajes": [
        {
          "idProgramacionViaje": 1234,
          "idRuta": 1,
          "fechaHoraSalida": "2026-12-10T06:30:00",
          "fechaHoraLlegada": "2026-12-10T08:30:00",
          "duracionEstimada": 120,
          "direccionOrigen": "Aeropuerto Adolfo Suárez Barajas",
          "direccionDestino": "Aeropuerto Charles de Gaulle",
          "tipoTransporte": "AVION",
          "codigoVehiculo": "EC-MXV",
          "distancia": 1050.00,
          "precioBase": 520000.00,
          "precioTotal": 520000.00,
          "asientosDisponibles": 180
        }
      ]
    },
    { "origen": "París", "paisOrigen": "Francia", "destino": "Madrid", "paisDestino": "España", "fecha": "2026-12-15", "viajes": [ ... ] }
  ]
}
```

`duracionEstimada` en minutos · `distancia` en km · `precioTotal` = `precioBase` × pasajeros (sin plan ni oferta). El `idProgramacionViaje` elegido es el que se manda al crear la reserva.

**Rutas:** 798 (ida y vuelta) que conectan ~80 % de los pares entre las 32 ciudades de `EUROPEAN_CITIES`; cada ciudad tiene entre 20 y 28 destinos directos. Hay **una salida diaria por ruta del 01-dic-2026 al 31-ene-2027**: avión ida 07:00 / vuelta 14:00 · tren ida 08:00 / vuelta 16:00 · bus ida 07:00 / vuelta 19:00 (cada ruta tiene su propio vehículo, para cumplir la regla de 12 horas de descanso). Una búsqueda con fecha de noviembre devuelve `viajes` vacío.

## 4. Catálogos (listas desplegables) – `GET`

**Ubicaciones completas** – forma `{ país: { departamento: [ciudades] } }`, igual a `UBICACIONES` de `Register.tsx` (un país sin ciudades sale como `{}`):

| Endpoint | Devuelve |
|---|---|
| `/api/catalogos/ubicaciones/no-europa` | países que **no** son de Europa (clientes y sedes): `{ "Colombia": { "Antioquia": ["Medellín", "Envigado"], ... }, "Estados Unidos": { ... }, ... }` |
| `/api/catalogos/ubicaciones/europa` | países de Europa (España y los de las rutas): `{ "España": { "Andalucía": ["Sevilla", "Málaga"], ... }, "Francia": { "Isla de Francia": ["París"], ... }, ... }` |
| `/api/catalogos/ubicaciones` | todos los países |
| `/api/catalogos/indicativos/no-europa` | nombre e indicativo de los países que no son de Europa: `[{ "idPais": 1, "nombrePais": "Colombia", "indicativo": "+57" }, ...]` |

**Consultas individuales:**

| Endpoint | Devuelve |
|---|---|
| `/api/catalogos/continentes` | `[{ "id": 1, "nombre": "América" }, { "id": 2, "nombre": "Europa" }, ...]` |
| `/api/catalogos/continentes/{idContinente}/paises` | `[{ "id": 1, "nombre": "Colombia" }, ...]` |
| `/api/catalogos/paises` | `[{ "id": 1, "nombre": "Colombia" }, ...]` |
| `/api/catalogos/indicativos` | indicativos de todos los países, mismo formato que `/indicativos/no-europa` |
| `/api/catalogos/paises/{idPais}/departamentos` | `[{ "id": 25, "nombre": "Quindío" }, ...]` |
| `/api/catalogos/departamentos/{idDepartamento}/ciudades` | `[{ "id": 3, "nombre": "Armenia" }, ...]` |
| `/api/catalogos/ciudades-destino` | ciudades con rutas (las 32 de `EUROPEAN_CITIES`): `[{ "idCiudad", "nombreCiudad", "idDepartamento", "nombreDepartamento", "idPais", "nombrePais" }]` |
| `/api/catalogos/tipos-identificacion` | CC, CE, TI, PA, PEP |
| `/api/catalogos/tipos-transporte` | AVION, BUS, TREN |
| `/api/catalogos/tipos-trayecto` | IDA, VUELTA |
| `/api/catalogos/metodos-pago` | PSE, TARJETA_CREDITO, TARJETA_DEBITO, EFECTIVO (solo para ventas hechas por un empleado) |
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
      "numeroAsiento": null,
      "idPlan": 1
    }
  ]
}
```

Cada pasajero viaja en todos los viajes de la reserva con el mismo asiento y plan: un `DetalleReserva` por pasajero, y los viajes (ida y regreso) quedan en `ReservaViaje`. **`numeroAsiento` es opcional**: si va en `null`, el backend asigna el primer número libre (1, 2, 3… hasta la capacidad del vehículo) en todas las salidas de la reserva; si se envía (ej. `"12A"`), se respeta y responde 409 si ya está ocupado. Cuando la compra la hace un **empleado**, `idEmpleado` es el id del empleado y `idCliente` el del cliente al que le vende. `idEmpleado` (quien atiende) e `idOferta` son opcionales; la oferta debe estar vigente y el cliente no puede haberla usado en otra reserva no cancelada (responde 409); las que puede usar salen en `GET /api/clientes/{id}/ofertas`. Planes: `GET /api/planes`.

**201 Created** – la reserva queda **PENDIENTE**:

```json
{
  "idReserva": 1, "fechaReserva": "2026-11-12T15:40:12", "estadoReserva": "PENDIENTE",
  "idCliente": 2, "nombreCliente": "Juan Pérez", "idEmpleado": null, "nombreEmpleado": null,
  "idOferta": 1, "tituloOferta": "Bienvenida",
  "viajes": [
    { "idProgramacionViaje": 1234, "tipoTrayecto": "IDA", "ciudadOrigen": "Madrid",
      "ciudadDestino": "París", "fechaHoraSalida": "2026-12-10T06:30:00",
      "fechaHoraLlegada": "2026-12-10T08:30:00", "precioBase": 520000.00 }
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

`metodoPago`: TARJETA_CREDITO, TARJETA_DEBITO, PSE o **EFECTIVO**. El efectivo solo se acepta si la reserva tiene `idEmpleado` (venta en sede); si no, responde 400.

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
| `PUT /api/reservas/{id}/cancelar?idCliente=11` | El **cliente dueño** cancela su reserva (pendiente o pagada) si faltan **5 días o más** para el día del primer viaje. Libera los asientos y la oferta; si estaba pagada, el pago queda `REEMBOLSADA`. 403 si `idCliente` no es el dueño; 409 si faltan menos de 5 días o ya estaba cancelada |
| `DELETE /api/reservas/{id}` | Solo si no tiene pago |
| `GET /api/reservas/{id}/pago` | Pago y factura |
| `GET /api/clientes/{id}/compras` | Historial de compras del cliente: lista con el mismo formato de `GET /api/reservas/{id}` (viajes, pasajeros con asiento, subtotal, descuento, total y `pago` con la factura), de la más reciente a la más antigua |
| `GET /api/clientes/{id}/ofertas` (también `/cupones`) | Ofertas disponibles para el cliente: vigentes hoy y que todavía no ha usado `[{ idOferta, titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin, fechaNotificacion }]` (`fechaNotificacion` = cuándo se le avisó, o null) |
| `GET /api/clientes/resumen?q=&gastoMin=&gastoMax=` | Gestión de clientes: `[{ datosPersonales: {...igual a GET /api/clientes/{id}}, totalGastado, comprasPagadas, reservas }]`, del que más gastó al que menos. `totalGastado` suma solo pagos APROBADOS; los filtros son opcionales e incluyen el límite |
| `GET /api/clientes/{id}/millas` | `{ idCliente, kilometrosRecorridos, millasAcumuladas }` (reservas confirmadas) |

## 6. Administración (CRUD)

Todos siguen el mismo patrón: `GET /api/x` (lista, con filtro opcional), `GET /api/x/{id}`, `POST /api/x` (201), `PUT /api/x/{id}`, `DELETE /api/x/{id}` (204).

| Recurso | Cuerpo de POST/PUT | Filtro del GET |
|---|---|---|
| `/api/clientes` | POST: igual al registro (con `contrasena`). PUT: igual, sin `contrasena`. `DELETE` responde **409** si el cliente tiene reservas: *"No se puede eliminar al cliente porque tiene 2 reservas registradas..."* | `?q=` identificación o apellido |
| `/api/empleados` | registro + `salario`, `fechaContratacion` ("2026-01-15"), `idCargo`, `idSede`, `rol` (EMPLEADO o ADMIN). En PUT sin `contrasena` | `?idSede=` |
| `/api/sedes` | `{ nombreSede, direccion, pais, departamento, ciudad }` (por nombre, como en `/api/catalogos/ubicaciones/no-europa`). `DELETE` responde **409** con el motivo si la sede tiene empleados: *"No se puede eliminar la sede Sede Cali: tiene 1 empleado trabajando en ella. Reasígnalos a otra sede primero"* | `?q=` |
| `/api/planes` | `{ nombrePlan, descripcionPlan, pesoMaxEquipaje, incluyeCheckIn, precioExtra }` | `?q=` |
| `/api/ofertas` | `{ titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin }`. Al crearla se registra **automáticamente** una notificación ENVIADA a todos los clientes (simulada: solo queda en la BD; se ve en `GET /api/notificaciones`). Al eliminarla se borran también sus notificaciones | `?vigentes=true` |
| `/api/vehiculos` | `{ codigoIdentificacion, tipoTransporte, capacidad }` | `?q=` |
| `/api/rutas` | `{ idCiudadOrigen, direccionOrigen, idCiudadDestino, direccionDestino, distancia, duracionEstimada, precioBase, idVehiculo }` | `?q=` nombre de ciudad |
| `/api/programaciones` | `{ idRuta, fechaHoraSalida: "2026-12-20T08:30:00" }`. **Regla de 12 horas:** el vehículo de la ruta no puede tener otro viaje que empiece antes de 12 h después de que este llegue (ni que llegue menos de 12 h antes de que este salga); si choca responde 409 diciendo con qué salida. También se valida al cambiar el vehículo o la duración de una ruta que ya tiene salidas | `?idRuta=&desde=2026-12-01&hasta=2026-12-07` (máx. 31 días; por defecto los próximos 7) |
| `/api/notificaciones` | `{ mensaje, estadoNotificacion, idOferta, idsClientes: [2, 5] }` | `?idCliente=` |

Extras de programación:

- `POST /api/programaciones/recurrente` – **repite una ruta cada cierto tiempo**:
  ```json
  { "idRuta": 1, "fechaHoraInicio": "2026-12-01T06:00:00", "fechaHoraFin": "2026-12-31T23:59:00", "cadaHoras": 24 }
  ```
  `cadaHoras`: 24 = diaria, 168 = semanal. Debe dejar el viaje más 12 horas de descanso (una ruta de 2 h necesita `cadaHoras` ≥ 14; si no, 400). Respuesta: `{ "creadas": 31, "omitidas": 0, "conflictos": 0, "salidas": [...] }` (`omitidas` = ya existían; `conflictos` = chocaban con otro viaje del mismo vehículo).
- `GET /api/programaciones/{id}/asientos` → `{ idProgramacionViaje, capacidad, asientosOcupados, asientosDisponibles, ocupados: ["1", "2", "12A"] }`.

## 7. Reportes – `GET /api/reportes/...`

Todos devuelven una lista JSON lista para tabla o gráfico. Ventas e ingresos cuentan solo pagos **APROBADOS**; los pasajeros no cuentan reservas **CANCELADAS**. La base trae 40 reservas de ejemplo (35 pagadas) para que los reportes tengan datos.

| Endpoint | Fila de la respuesta |
|---|---|
| `/ventas-mensuales` (`?anio=2026` opcional) | `{ "periodo": "2026-11", "cantidadVentas": 8, "totalVendido": 6123000.00 }` |
| `/ingresos-metodo-pago` | `{ "metodoPago": "PSE", "cantidadPagos": 10, "totalIngresos": 9000000.00, "porcentaje": 28.50 }` |
| `/reservas-por-empleado` | `{ "idEmpleado": 5, "nombreEmpleado": "Carlos Restrepo Gómez", "cargo": "Asesor de ventas", "sede": "Sede Principal Medellín", "cantidadReservas": 7, "totalVendido": 5400000.00 }` |
| `/rutas-mas-pasajeros` (`?limite=10`) | `{ "idRuta": 3, "origen": "Madrid", "destino": "París", "tipoTransporte": "AVION", "reservas": 4, "pasajeros": 9, "promedioCategoria": null }` |
| `/clientes-sobre-promedio` | `{ "idCliente": 13, "nombreCliente": "Ana Gutiérrez Londoño", "correo": "...", "cantidadReservas": 12, "totalGastado": 15484600.00, "promedioClientes": 7894067.50 }` |
| `/rutas-bajo-promedio` (`?tipo=AVION\|BUS\|TREN` opcional) | rutas con **menos** pasajeros que el promedio de su tipo (las menos eficientes); mismo formato que `/rutas-mas-pasajeros`, con `promedioCategoria` lleno |
| `/rutas-sobre-promedio` (`?tipo=` opcional) | rutas más vendidas de cada tipo: **más** pasajeros que el promedio de su tipo; mismo formato |

## 8. Formato de errores

Todos los errores usan el mismo formato (`application/problem+json`):

```json
{ "type": "about:blank", "title": "Datos inválidos", "status": 400, "detail": "Revisa los campos marcados.",
  "instance": "/api/auth/registro",
  "errores": { "correo": "El correo no es válido", "telefono": "El teléfono solo admite números, espacios y guiones" } }
```

`errores` (campo → mensaje) solo aparece en errores de validación. Códigos: **400** datos inválidos · **401** login incorrecto · **404** no existe · **409** conflicto (duplicado, sin cupo, registro en uso).
