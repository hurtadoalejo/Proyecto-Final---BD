-- =====================================================================
--  Plataforma de Turismo - Proyecto Final Bases de Datos I (2026-2)
--  Esquema MySQL 8 generado a partir del modelo relacional del equipo
--  (Oracle SQL Developer Data Modeler, ProyectoFinal.dmd).
--
--  Nombres de tablas y columnas: EXACTOS a los del modelo.
--  Correcciones aplicadas sobre el modelo (marcadas con [C1]..[C7]):
--   [C1] Usuario.contrasenia VARCHAR(50) -> VARCHAR(100): el hash BCrypt mide 60.
--   [C2] Fechas con hora (salida, reserva, pago, envío, emisión): DATE -> DATETIME.
--   [C3] Dinero y medidas: NUMERIC sin escala -> DECIMAL con decimales.
--   [C4] Ciudad.codigoCiudad (nuevo): código de 3 letras que usa el frontend (BOG, MAD...).
--   [C5] Se quita la columna sobrante Usuario_identificacion de Cliente y Empleado.
--   [C6] UNIQUE en Usuario.correo y Usuario.identificacion.
--   [C7] Notificacion.idOferta pasa a ser opcional (recordatorios sin oferta).
--   [C8] Usuario.indicativoPais (nuevo): el registro del frontend lo envía aparte del celular.
--   [C9] Pais.indicativo (nuevo): indicativo telefónico de cada país, para el select del registro.
-- =====================================================================

DROP DATABASE IF EXISTS turismo_db;
CREATE DATABASE turismo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE turismo_db;

-- ---------------------------------------------------------------------
-- Catálogos
-- ---------------------------------------------------------------------
CREATE TABLE Rol (
    idRol      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreRol  VARCHAR(40)  NOT NULL,
    CONSTRAINT Rol_PK PRIMARY KEY (idRol),
    CONSTRAINT Rol_nombre_UK UNIQUE (nombreRol)
);

CREATE TABLE TipoIdentificacion (
    idTipoIdentificacion  BIGINT       NOT NULL AUTO_INCREMENT,
    nombreIdentificacion  VARCHAR(40)  NOT NULL,
    CONSTRAINT TipoIdentificacion_PK PRIMARY KEY (idTipoIdentificacion),
    CONSTRAINT TipoIdentificacion_nombre_UK UNIQUE (nombreIdentificacion)
);

CREATE TABLE Cargo (
    idCargo      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreCargo  VARCHAR(40)  NOT NULL,
    CONSTRAINT Cargo_PK PRIMARY KEY (idCargo),
    CONSTRAINT Cargo_nombre_UK UNIQUE (nombreCargo)
);

CREATE TABLE EstadoNotificacion (
    idEstadoNotificacion      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreEstadoNotificacion  VARCHAR(20)  NOT NULL,
    CONSTRAINT EstadoNotificacion_PK PRIMARY KEY (idEstadoNotificacion),
    CONSTRAINT EstadoNotificacion_nombre_UK UNIQUE (nombreEstadoNotificacion)
);

CREATE TABLE EstadoReserva (
    idEstadoReserva      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreEstadoReserva  VARCHAR(20)  NOT NULL,
    CONSTRAINT EstadoReserva_PK PRIMARY KEY (idEstadoReserva),
    CONSTRAINT EstadoReserva_nombre_UK UNIQUE (nombreEstadoReserva)
);

CREATE TABLE EstadoTransaccion (
    idEstadoTransaccion      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreEstadoTransaccion  VARCHAR(20)  NOT NULL,
    CONSTRAINT EstadoTransaccion_PK PRIMARY KEY (idEstadoTransaccion),
    CONSTRAINT EstadoTransaccion_nombre_UK UNIQUE (nombreEstadoTransaccion)
);

CREATE TABLE MetodoPago (
    idMetodoPago      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreMetodoPago  VARCHAR(20)  NOT NULL,
    CONSTRAINT MetodoPago_PK PRIMARY KEY (idMetodoPago),
    CONSTRAINT MetodoPago_nombre_UK UNIQUE (nombreMetodoPago)
);

CREATE TABLE TipoTransporte (
    idTipoTransporte      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreTipoTransporte  VARCHAR(25)  NOT NULL,
    CONSTRAINT TipoTransporte_PK PRIMARY KEY (idTipoTransporte),
    CONSTRAINT TipoTransporte_nombre_UK UNIQUE (nombreTipoTransporte)
);

CREATE TABLE TipoTrayecto (
    idTipoTrayecto      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreTipoTrayecto  VARCHAR(20)  NOT NULL,
    CONSTRAINT TipoTrayecto_PK PRIMARY KEY (idTipoTrayecto),
    CONSTRAINT TipoTrayecto_nombre_UK UNIQUE (nombreTipoTrayecto)
);

-- ---------------------------------------------------------------------
-- Ubicación: Pais -> Departamento -> Ciudad
-- ---------------------------------------------------------------------
CREATE TABLE Pais (
    idPais      BIGINT       NOT NULL AUTO_INCREMENT,
    nombrePais  VARCHAR(25)  NOT NULL,
    indicativo  VARCHAR(5)   NOT NULL,   -- [C9] indicativo telefónico (+57)
    CONSTRAINT Pais_PK PRIMARY KEY (idPais),
    CONSTRAINT Pais_nombre_UK UNIQUE (nombrePais)
);

CREATE TABLE Departamento (
    idDepartamento      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreDepartamento  VARCHAR(25)  NOT NULL,
    idPais              BIGINT       NOT NULL,
    CONSTRAINT Departamento_PK PRIMARY KEY (idDepartamento),
    CONSTRAINT Departamento_Pais_FK FOREIGN KEY (idPais) REFERENCES Pais (idPais),
    CONSTRAINT Departamento_nombre_UK UNIQUE (nombreDepartamento, idPais)
);

CREATE TABLE Ciudad (
    idCiudad        BIGINT       NOT NULL AUTO_INCREMENT,
    nombreCiudad    VARCHAR(25)  NOT NULL,
    codigoCiudad    VARCHAR(3),                                   -- [C4]
    idDepartamento  BIGINT       NOT NULL,
    CONSTRAINT Ciudad_PK PRIMARY KEY (idCiudad),
    CONSTRAINT Ciudad_Departamento_FK FOREIGN KEY (idDepartamento) REFERENCES Departamento (idDepartamento),
    CONSTRAINT Ciudad_codigo_UK UNIQUE (codigoCiudad),
    CONSTRAINT Ciudad_nombre_UK UNIQUE (nombreCiudad, idDepartamento)
);

-- ---------------------------------------------------------------------
-- Sede y usuarios (Usuario -> {Cliente, Empleado})
-- ---------------------------------------------------------------------
CREATE TABLE Sede (
    idSede      BIGINT       NOT NULL AUTO_INCREMENT,
    nombreSede  VARCHAR(40)  NOT NULL,
    direccion   VARCHAR(60)  NOT NULL,
    idCiudad    BIGINT       NOT NULL,
    CONSTRAINT Sede_PK PRIMARY KEY (idSede),
    CONSTRAINT Sede_Ciudad_FK FOREIGN KEY (idCiudad) REFERENCES Ciudad (idCiudad),
    CONSTRAINT Sede_nombre_UK UNIQUE (nombreSede)
);

CREATE TABLE Usuario (
    idUsuario             BIGINT        NOT NULL AUTO_INCREMENT,
    primerNombre          VARCHAR(20)   NOT NULL,
    segundoNombre         VARCHAR(20),
    primerApellido        VARCHAR(20)   NOT NULL,
    segundoApellido       VARCHAR(20),
    identificacion        VARCHAR(20)   NOT NULL,
    indicativoPais        VARCHAR(5),                             -- [C8] ej. +57
    telefono              VARCHAR(20),                            -- celular sin indicativo
    fechaNacimiento       DATE,
    correo                VARCHAR(100)  NOT NULL,
    contrasenia           VARCHAR(100)  NOT NULL,                 -- [C1]
    direccion             VARCHAR(50),
    idRol                 BIGINT        NOT NULL,
    idTipoIdentificacion  BIGINT        NOT NULL,
    idCiudad              BIGINT        NOT NULL,
    CONSTRAINT Usuario_PK PRIMARY KEY (idUsuario),
    CONSTRAINT Usuario_Rol_FK FOREIGN KEY (idRol) REFERENCES Rol (idRol),
    CONSTRAINT Usuario_TipoIdentificacion_FK FOREIGN KEY (idTipoIdentificacion) REFERENCES TipoIdentificacion (idTipoIdentificacion),
    CONSTRAINT Usuario_Ciudad_FK FOREIGN KEY (idCiudad) REFERENCES Ciudad (idCiudad),
    CONSTRAINT Usuario_correo_UK UNIQUE (correo),                 -- [C6]
    CONSTRAINT Usuario_identificacion_UK UNIQUE (identificacion)  -- [C6]
);

CREATE TABLE Cliente (                                            -- [C5]
    idUsuario  BIGINT  NOT NULL,
    CONSTRAINT Cliente_PK PRIMARY KEY (idUsuario),
    CONSTRAINT Cliente_Usuario_FK FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario) ON DELETE CASCADE
);

CREATE TABLE Empleado (                                           -- [C5]
    idUsuario          BIGINT         NOT NULL,
    salario            DECIMAL(12,2)  NOT NULL,                   -- [C3]
    fechaContratacion  DATE           NOT NULL,
    idCargo            BIGINT         NOT NULL,
    idSede             BIGINT         NOT NULL,
    CONSTRAINT Empleado_PK PRIMARY KEY (idUsuario),
    CONSTRAINT Empleado_Usuario_FK FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario) ON DELETE CASCADE,
    CONSTRAINT Empleado_Cargo_FK FOREIGN KEY (idCargo) REFERENCES Cargo (idCargo),
    CONSTRAINT Empleado_Sede_FK FOREIGN KEY (idSede) REFERENCES Sede (idSede),
    CONSTRAINT Empleado_salario_CK CHECK (salario >= 0)
);

-- ---------------------------------------------------------------------
-- Ofertas y notificaciones
-- ---------------------------------------------------------------------
CREATE TABLE Oferta (
    idOferta             BIGINT         NOT NULL AUTO_INCREMENT,
    titulo               VARCHAR(30)    NOT NULL,
    descripcion          VARCHAR(100),
    porcentajeDescuento  DECIMAL(5,2)   NOT NULL,                 -- [C3]
    fechaInicio          DATE           NOT NULL,
    fechaFin             DATE           NOT NULL,
    CONSTRAINT Oferta_PK PRIMARY KEY (idOferta),
    CONSTRAINT Oferta_fechas_CK CHECK (fechaFin >= fechaInicio),
    CONSTRAINT Oferta_porcentaje_CK CHECK (porcentajeDescuento > 0 AND porcentajeDescuento <= 100)
);

CREATE TABLE Notificacion (
    idNotificacion        BIGINT        NOT NULL AUTO_INCREMENT,
    mensaje               VARCHAR(200)  NOT NULL,
    fechaEnvio            DATETIME,                               -- [C2]
    idEstadoNotificacion  BIGINT        NOT NULL,
    idOferta              BIGINT,                                 -- [C7]
    CONSTRAINT Notificacion_PK PRIMARY KEY (idNotificacion),
    CONSTRAINT Notificacion_EstadoNotificacion_FK FOREIGN KEY (idEstadoNotificacion) REFERENCES EstadoNotificacion (idEstadoNotificacion),
    CONSTRAINT Notificacion_Oferta_FK FOREIGN KEY (idOferta) REFERENCES Oferta (idOferta)
);

CREATE TABLE ClienteNotificacion (
    idUsuario       BIGINT  NOT NULL,
    idNotificacion  BIGINT  NOT NULL,
    CONSTRAINT ClienteNotificacion_PK PRIMARY KEY (idUsuario, idNotificacion),
    CONSTRAINT ClienteNotificacion_Cliente_FK FOREIGN KEY (idUsuario) REFERENCES Cliente (idUsuario) ON DELETE CASCADE,
    CONSTRAINT ClienteNotificacion_Notificacion_FK FOREIGN KEY (idNotificacion) REFERENCES Notificacion (idNotificacion) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Transporte: Vehiculo, Ruta, RutaVehiculo, ProgramacionViaje
-- ---------------------------------------------------------------------
CREATE TABLE Vehiculo (
    idVehiculo            BIGINT       NOT NULL AUTO_INCREMENT,
    codigoIdentificacion  VARCHAR(25)  NOT NULL,
    idTipoTransporte      BIGINT       NOT NULL,
    capacidad             INT          NOT NULL,
    CONSTRAINT Vehiculo_PK PRIMARY KEY (idVehiculo),
    CONSTRAINT Vehiculo_TipoTransporte_FK FOREIGN KEY (idTipoTransporte) REFERENCES TipoTransporte (idTipoTransporte),
    CONSTRAINT Vehiculo_codigo_UK UNIQUE (codigoIdentificacion),
    CONSTRAINT Vehiculo_capacidad_CK CHECK (capacidad > 0)
);

CREATE TABLE Ruta (
    idRuta            BIGINT         NOT NULL AUTO_INCREMENT,
    distancia         DECIMAL(10,2)  NOT NULL COMMENT 'kilómetros',   -- [C3]
    duracionEstimada  INT            NOT NULL COMMENT 'minutos',
    precioBase        DECIMAL(12,2)  NOT NULL COMMENT 'COP',         -- [C3]
    idCiudadOrigen    BIGINT         NOT NULL,
    direccionOrigen   VARCHAR(40),
    idCiudadDestino   BIGINT         NOT NULL,
    direccionDestino  VARCHAR(40),
    CONSTRAINT Ruta_PK PRIMARY KEY (idRuta),
    CONSTRAINT CiudadOrigen FOREIGN KEY (idCiudadOrigen) REFERENCES Ciudad (idCiudad),
    CONSTRAINT CiudadDestino FOREIGN KEY (idCiudadDestino) REFERENCES Ciudad (idCiudad),
    CONSTRAINT Ruta_distancia_CK CHECK (distancia > 0),
    CONSTRAINT Ruta_duracion_CK CHECK (duracionEstimada > 0),
    CONSTRAINT Ruta_precio_CK CHECK (precioBase >= 0),
    CONSTRAINT Ruta_ciudades_CK CHECK (idCiudadOrigen <> idCiudadDestino)
);

-- Cada ruta la cubre un vehículo (PK = idRuta, como en el modelo).
CREATE TABLE RutaVehiculo (
    idRuta      BIGINT  NOT NULL,
    idVehiculo  BIGINT  NOT NULL,
    CONSTRAINT RutaVehiculo_PK PRIMARY KEY (idRuta),
    CONSTRAINT RutaVehiculo_Ruta_FK FOREIGN KEY (idRuta) REFERENCES Ruta (idRuta) ON DELETE CASCADE,
    CONSTRAINT RutaVehiculo_Vehiculo_FK FOREIGN KEY (idVehiculo) REFERENCES Vehiculo (idVehiculo)
);

CREATE TABLE ProgramacionViaje (
    idProgramacionViaje  BIGINT    NOT NULL AUTO_INCREMENT,
    fechaHoraSalida      DATETIME  NOT NULL,                      -- [C2]
    idRuta               BIGINT    NOT NULL,
    CONSTRAINT ProgramacionViaje_PK PRIMARY KEY (idProgramacionViaje),
    CONSTRAINT ProgramacionViaje_Ruta_FK FOREIGN KEY (idRuta) REFERENCES Ruta (idRuta)
);
CREATE INDEX ProgramacionViaje_salida_IX ON ProgramacionViaje (idRuta, fechaHoraSalida);

-- ---------------------------------------------------------------------
-- Plan, Reserva y su detalle
-- ---------------------------------------------------------------------
CREATE TABLE Plan (
    idPlan           BIGINT         NOT NULL AUTO_INCREMENT,
    nombrePlan       VARCHAR(30)    NOT NULL,
    descripcionPlan  VARCHAR(200),
    pesoMaxEquipaje  DECIMAL(5,2)   NOT NULL COMMENT 'kilogramos',  -- [C3]
    incluyeCheckIn   BOOLEAN        NOT NULL DEFAULT FALSE,
    precioExtra      DECIMAL(12,2)  NOT NULL,                       -- [C3]
    CONSTRAINT Plan_PK PRIMARY KEY (idPlan),
    CONSTRAINT Plan_nombre_UK UNIQUE (nombrePlan),
    CONSTRAINT Plan_precio_CK CHECK (precioExtra >= 0)
);

CREATE TABLE Reserva (
    idReserva           BIGINT    NOT NULL AUTO_INCREMENT,
    fechaReserva        DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP, -- [C2]
    Empleado_idUsuario  BIGINT,
    Cliente_idUsuario   BIGINT    NOT NULL,
    idOferta            BIGINT,
    idEstadoReserva     BIGINT    NOT NULL,
    CONSTRAINT Reserva_PK PRIMARY KEY (idReserva),
    CONSTRAINT Reserva_Empleado_FK FOREIGN KEY (Empleado_idUsuario) REFERENCES Empleado (idUsuario),
    CONSTRAINT Reserva_Cliente_FK FOREIGN KEY (Cliente_idUsuario) REFERENCES Cliente (idUsuario),
    CONSTRAINT Reserva_Oferta_FK FOREIGN KEY (idOferta) REFERENCES Oferta (idOferta),
    CONSTRAINT Reserva_EstadoReserva_FK FOREIGN KEY (idEstadoReserva) REFERENCES EstadoReserva (idEstadoReserva)
);

CREATE TABLE ReservaViaje (
    idReserva            BIGINT  NOT NULL,
    idProgramacionViaje  BIGINT  NOT NULL,
    idTipoTrayecto       BIGINT  NOT NULL,
    CONSTRAINT ReservaViaje_PK PRIMARY KEY (idReserva, idProgramacionViaje),
    CONSTRAINT ReservaViaje_Reserva_FK FOREIGN KEY (idReserva) REFERENCES Reserva (idReserva) ON DELETE CASCADE,
    CONSTRAINT ReservaViaje_ProgramacionViaje_FK FOREIGN KEY (idProgramacionViaje) REFERENCES ProgramacionViaje (idProgramacionViaje),
    CONSTRAINT ReservaViaje_TipoTrayecto_FK FOREIGN KEY (idTipoTrayecto) REFERENCES TipoTrayecto (idTipoTrayecto)
);

-- idDetalle es el número del pasajero dentro de la reserva (1, 2, 3...).
CREATE TABLE DetalleReserva (
    idDetalle             BIGINT       NOT NULL,
    idReserva             BIGINT       NOT NULL,
    primerNombre          VARCHAR(20)  NOT NULL,
    segundoNombre         VARCHAR(20),
    primerApellido        VARCHAR(20)  NOT NULL,
    segundoApellido       VARCHAR(20),
    identificacion        VARCHAR(20)  NOT NULL,
    numeroAsiento         VARCHAR(10)  NOT NULL,
    idTipoIdentificacion  BIGINT       NOT NULL,
    idPlan                BIGINT       NOT NULL,
    CONSTRAINT DetalleReserva_PK PRIMARY KEY (idDetalle, idReserva),
    CONSTRAINT DetalleReserva_Reserva_FK FOREIGN KEY (idReserva) REFERENCES Reserva (idReserva) ON DELETE CASCADE,
    CONSTRAINT DetalleReserva_TipoIdentificacion_FK FOREIGN KEY (idTipoIdentificacion) REFERENCES TipoIdentificacion (idTipoIdentificacion),
    CONSTRAINT DetalleReserva_Plan_FK FOREIGN KEY (idPlan) REFERENCES Plan (idPlan)
);

-- ---------------------------------------------------------------------
-- Pago: Transaccion (débil de Reserva) y Factura (débil de Transaccion)
-- ---------------------------------------------------------------------
CREATE TABLE Transaccion (
    idReserva            BIGINT         NOT NULL,
    montoPagado          DECIMAL(12,2)  NOT NULL,                 -- [C3]
    fechaPago            DATETIME,                                -- [C2]
    idMetodoPago         BIGINT         NOT NULL,
    idEstadoTransaccion  BIGINT         NOT NULL,
    CONSTRAINT Transaccion_PK PRIMARY KEY (idReserva),
    CONSTRAINT Transaccion_Reserva_FK FOREIGN KEY (idReserva) REFERENCES Reserva (idReserva),
    CONSTRAINT Transaccion_MetodoPago_FK FOREIGN KEY (idMetodoPago) REFERENCES MetodoPago (idMetodoPago),
    CONSTRAINT Transaccion_EstadoTransaccion_FK FOREIGN KEY (idEstadoTransaccion) REFERENCES EstadoTransaccion (idEstadoTransaccion),
    CONSTRAINT Transaccion_monto_CK CHECK (montoPagado >= 0)
);

CREATE TABLE Factura (
    idReserva     BIGINT    NOT NULL,
    fechaEmision  DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- [C2]
    CONSTRAINT Factura_PK PRIMARY KEY (idReserva),
    CONSTRAINT Factura_Transaccion_FK FOREIGN KEY (idReserva) REFERENCES Transaccion (idReserva)
);
