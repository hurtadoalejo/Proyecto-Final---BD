# Plataforma de turismo – Backend

Spring Boot 4.1 · Java 21 · MySQL 8 · Proyecto final Bases de Datos I.

## Cómo ejecutarlo

1. **Base de datos** (MySQL Workbench o consola), en este orden:
   - `src/main/resources/db/01_esquema.sql` → crea `turismo_db` con las 29 tablas del modelo.
   - `src/main/resources/db/02_datos.sql` → catálogos, países/departamentos/ciudades, sedes, planes, ofertas, 48 vehículos, 96 rutas (Colombia y Europa; avión, tren y bus) y sus salidas diarias hasta el 31-ene-2027.
2. **Conexión**: usa `localhost:3306`, usuario `root`. La contraseña de MySQL va en la variable de entorno `DB_PASSWORD` (IntelliJ → Run → Edit Configurations → Environment variables: `DB_PASSWORD=tu_clave`). Si tu MySQL usa otro puerto, agrega `DB_PORT` (ej.: `DB_PASSWORD=tu_clave;DB_PORT=3307`).
3. Ejecuta `PlataformaApplication`. Al arrancar se crea el administrador `admin@viajacol.com` / `AdminViajaCol2026` (se puede cambiar con la variable `ADMIN_PASSWORD`).
4. API en `http://localhost:8080` · Swagger en `http://localhost:8080/swagger-ui.html`.

La guía de endpoints con los JSON para el frontend está en **[API_FRONTEND.md](API_FRONTEND.md)**.

## Ajustes al modelo relacional

Los scripts siguen las 29 tablas y columnas del modelo de Data Modeler, con estos ajustes:

| # | Ajuste | Motivo |
|---|---|---|
| C1 | `Usuario.contrasenia` VARCHAR(100) | Espacio suficiente para contraseñas largas |
| C2 | DATETIME en `fechaHoraSalida`, `fechaReserva`, `fechaPago`, `fechaEnvio`, `fechaEmision` | Guardar fecha **y** hora |
| C3 | DECIMAL en dinero y medidas (`precioBase`, `salario`, `distancia`, `porcentajeDescuento`...) | Evitar errores de redondeo |
| C4 | `Ciudad.codigoCiudad` VARCHAR(3) UNIQUE | La búsqueda del front usa códigos (BOG, MAD...) |
| C5 | Se quitó `Usuario_identificacion` de Cliente y Empleado | Dato repetido: ya está en Usuario |
| C6 | UNIQUE en `Usuario.correo` y `Usuario.identificacion` | No se puede registrar dos veces |
| C7 | `Notificacion.idOferta` admite NULL | No toda notificación es de una oferta |
| C8 | `Usuario.indicativoPais` VARCHAR(5) | El registro envía el indicativo (+57) en un campo aparte |
| C9 | `Pais.indicativo` VARCHAR(5) | Lista de indicativos para el select del registro |

## Estructura

```
config/       CORS (cualquier origen) y creación del administrador inicial
model/        Entidades JPA (catalogo/, ubicacion/ y las del negocio)
repository/   Consultas (Spring Data JPA)
service/      Reglas de negocio y validaciones
controller/   Endpoints REST (/api/...)
dto/          JSON de entrada y salida
exception/    Errores con formato ProblemDetail
```
