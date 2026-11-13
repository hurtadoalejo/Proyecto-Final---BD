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

Los scripts siguen las 29 tablas, columnas y nombres de llaves del modelo de Data Modeler (versión del 13-nov-2026), con estos ajustes:

| # | Ajuste | Motivo |
|---|---|---|
| C1 | DATETIME en `fechaHoraSalida`, `fechaReserva`, `fechaPago`, `fechaEnvio`, `fechaEmision` | Guardar fecha **y** hora |
| C2 | DECIMAL en dinero y medidas (`precioBase`, `salario`, `distancia`, `porcentajeDescuento`...) | Evitar errores de redondeo |
| C3 | `Ciudad.codigoCiudad` VARCHAR(3) UNIQUE | La búsqueda del front usa códigos (BOG, MAD...) |
| C4 | UNIQUE en `Usuario.correo` y `Usuario.identificacion` | No se puede registrar dos veces |
| C5 | `Notificacion.idOferta` admite NULL | No toda notificación es de una oferta |
| C6 | `nombreDepartamento` y `nombreCiudad` VARCHAR(40) | El registro tiene nombres de hasta 31 caracteres ("Ciudad Autónoma de Buenos Aires") |

`Usuario.telefono` guarda indicativo y número juntos (`+57 3001234567`); `Pais.indicativo` guarda el indicativo de cada país (57, 1, 34...).

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
