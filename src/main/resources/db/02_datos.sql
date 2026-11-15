-- =====================================================================
--  Datos iniciales. Ejecutar DESPUÉS de 01_esquema.sql.
--  Incluye: catálogos, ubicación (ciudades de UBICACIONES + EUROPEAN_CITIES),
--  10 sedes en Colombia (central en Medellín, idSede = 1), 10 asesores de
--  ventas (uno por sede), 4 clientes de prueba, vehículos, 798 rutas europeas
--  (~80 % de los pares entre las 32 ciudades de EUROPEAN_CITIES, ida y vuelta)
--  y su programación del 01-dic-2026 al 31-ene-2027.
--  La administradora (admin@viajacol.com) la crea la aplicación al arrancar.
-- =====================================================================
USE turismo_db;

-- ---------------- Catálogos ----------------
INSERT INTO Rol (nombreRol) VALUES ('ADMIN'), ('EMPLEADO'), ('CLIENTE');
INSERT INTO TipoIdentificacion (nombreIdentificacion) VALUES ('CC'), ('CE'), ('TI'), ('PA'), ('PEP'), ('RC');
INSERT INTO Cargo (nombreCargo) VALUES ('Administrador del sistema'), ('Asesor de ventas'), ('Agente de servicio al cliente'), ('Coordinador de operaciones');
INSERT INTO EstadoNotificacion (nombreEstadoNotificacion) VALUES ('PENDIENTE'), ('ENVIADO'), ('FALLIDO');
INSERT INTO EstadoReserva (nombreEstadoReserva) VALUES ('PENDIENTE'), ('CONFIRMADA'), ('CANCELADA');
INSERT INTO EstadoTransaccion (nombreEstadoTransaccion) VALUES ('PENDIENTE'), ('APROBADA'), ('RECHAZADA');
INSERT INTO MetodoPago (nombreMetodoPago) VALUES ('TARJETA_CREDITO'), ('TARJETA_DEBITO'), ('PSE');
INSERT INTO TipoTransporte (nombreTipoTransporte) VALUES ('AVION'), ('BUS'), ('TREN');
INSERT INTO TipoTrayecto (nombreTipoTrayecto) VALUES ('IDA'), ('VUELTA');

-- ---------------- Ubicación ----------------
-- 28 países con su indicativo telefónico.
INSERT INTO Pais (nombrePais, indicativo) VALUES
('Colombia', 57), ('Estados Unidos', 1), ('España', 34), ('México', 52), ('Argentina', 54),
('Chile', 56), ('Perú', 51), ('Ecuador', 593), ('Francia', 33), ('Reino Unido', 44),
('Italia', 39), ('Países Bajos', 31), ('Alemania', 49), ('Portugal', 351), ('Bélgica', 32),
('Austria', 43), ('Suiza', 41), ('Grecia', 30), ('Irlanda', 353), ('República Checa', 420),
('Hungría', 36), ('Polonia', 48), ('Dinamarca', 45), ('Suecia', 46), ('Noruega', 47),
('Finlandia', 358), ('Turquía', 90), ('Islandia', 354);

-- Tabla temporal (país, departamento, ciudad): de ella salen Departamento y Ciudad.
CREATE TEMPORARY TABLE tmp_ubic (pais VARCHAR(25), depto VARCHAR(40), ciudad VARCHAR(40));
INSERT INTO tmp_ubic VALUES
-- Colombia
('Colombia','Antioquia','Medellín'), ('Colombia','Antioquia','Envigado'),
('Colombia','Atlántico','Barranquilla'), ('Colombia','Atlántico','Soledad'),
('Colombia','Bogotá D.C.','Bogotá'), ('Colombia','Bogotá D.C.','Usaquén'),
('Colombia','Bolívar','Cartagena'), ('Colombia','Bolívar','Magangué'),
('Colombia','Boyacá','Tunja'), ('Colombia','Boyacá','Duitama'),
('Colombia','Caldas','Manizales'), ('Colombia','Caldas','Villamaría'),
('Colombia','Cundinamarca','Soacha'), ('Colombia','Cundinamarca','Chía'),
('Colombia','Quindío','Armenia'), ('Colombia','Quindío','Calarcá'),
('Colombia','Risaralda','Pereira'), ('Colombia','Risaralda','Dosquebradas'),
('Colombia','Valle del Cauca','Cali'), ('Colombia','Valle del Cauca','Palmira'),
-- Estados Unidos
('Estados Unidos','California','Los Ángeles'), ('Estados Unidos','California','San Francisco'),
('Estados Unidos','Texas','Houston'), ('Estados Unidos','Texas','Austin'),
('Estados Unidos','Florida','Miami'), ('Estados Unidos','Florida','Orlando'),
('Estados Unidos','Nueva York','Nueva York'), ('Estados Unidos','Nueva York','Búfalo'),
('Estados Unidos','Illinois','Chicago'), ('Estados Unidos','Illinois','Aurora'),
('Estados Unidos','Pensilvania','Filadelfia'), ('Estados Unidos','Pensilvania','Pittsburgh'),
('Estados Unidos','Ohio','Columbus'), ('Estados Unidos','Ohio','Cleveland'),
('Estados Unidos','Georgia','Atlanta'), ('Estados Unidos','Georgia','Savannah'),
('Estados Unidos','Carolina del Norte','Charlotte'), ('Estados Unidos','Carolina del Norte','Raleigh'),
('Estados Unidos','Míchigan','Detroit'), ('Estados Unidos','Míchigan','Grand Rapids'),
-- España
('España','Andalucía','Sevilla'), ('España','Andalucía','Málaga'),
('España','Cataluña','Barcelona'), ('España','Cataluña','Girona'),
('España','Comunidad de Madrid','Madrid'), ('España','Comunidad de Madrid','Alcalá de Henares'),
('España','Comunidad Valenciana','Valencia'), ('España','Comunidad Valenciana','Alicante'),
('España','Galicia','Vigo'), ('España','Galicia','A Coruña'),
('España','Castilla y León','Valladolid'), ('España','Castilla y León','Salamanca'),
('España','País Vasco','Bilbao'), ('España','País Vasco','San Sebastián'),
('España','Canarias','Las Palmas'), ('España','Canarias','Santa Cruz de Tenerife'),
('España','Castilla-La Mancha','Toledo'), ('España','Castilla-La Mancha','Albacete'),
('España','Aragón','Zaragoza'), ('España','Aragón','Huesca'),
-- México
('México','Ciudad de México','Ciudad de México'), ('México','Ciudad de México','Coyoacán'),
('México','Jalisco','Guadalajara'), ('México','Jalisco','Zapopan'),
('México','Nuevo León','Monterrey'), ('México','Nuevo León','San Pedro Garza García'),
('México','Puebla','Puebla'), ('México','Puebla','Tehuacán'),
('México','Guanajuato','León'), ('México','Guanajuato','Irapuato'),
('México','Estado de México','Toluca'), ('México','Estado de México','Ecatepec'),
('México','Veracruz','Veracruz'), ('México','Veracruz','Xalapa'),
('México','Yucatán','Mérida'), ('México','Yucatán','Valladolid'),
('México','Quintana Roo','Cancún'), ('México','Quintana Roo','Playa del Carmen'),
('México','Baja California','Tijuana'), ('México','Baja California','Mexicali'),
-- Argentina
('Argentina','Buenos Aires','La Plata'), ('Argentina','Buenos Aires','Mar del Plata'),
('Argentina','Ciudad Autónoma de Buenos Aires','Buenos Aires'), ('Argentina','Ciudad Autónoma de Buenos Aires','Palermo'),
('Argentina','Córdoba','Córdoba'), ('Argentina','Córdoba','Villa Carlos Paz'),
('Argentina','Santa Fe','Rosario'), ('Argentina','Santa Fe','Santa Fe'),
('Argentina','Mendoza','Mendoza'), ('Argentina','Mendoza','San Rafael'),
('Argentina','Tucumán','San Miguel de Tucumán'), ('Argentina','Tucumán','Yerba Buena'),
('Argentina','Salta','Salta'), ('Argentina','Salta','San Ramón de la Nueva Orán'),
('Argentina','Entre Ríos','Paraná'), ('Argentina','Entre Ríos','Concordia'),
('Argentina','Misiones','Posadas'), ('Argentina','Misiones','Puerto Iguazú'),
('Argentina','Chaco','Resistencia'), ('Argentina','Chaco','Presidencia Roque Sáenz Peña'),
-- Chile
('Chile','Región Metropolitana','Santiago'), ('Chile','Región Metropolitana','Puente Alto'),
('Chile','Valparaíso','Valparaíso'), ('Chile','Valparaíso','Viña del Mar'),
('Chile','Biobío','Concepción'), ('Chile','Biobío','Talcahuano'),
('Chile','La Araucanía','Temuco'), ('Chile','La Araucanía','Villarrica'),
('Chile','Los Lagos','Puerto Montt'), ('Chile','Los Lagos','Osorno'),
('Chile','Antofagasta','Antofagasta'), ('Chile','Antofagasta','Calama'),
('Chile','Coquimbo','La Serena'), ('Chile','Coquimbo','Coquimbo'),
('Chile','O''Higgins','Rancagua'), ('Chile','O''Higgins','San Fernando'),
('Chile','Maule','Talca'), ('Chile','Maule','Curicó'),
('Chile','Tarapacá','Iquique'), ('Chile','Tarapacá','Alto Hospicio'),
-- Perú
('Perú','Lima','Lima'), ('Perú','Lima','Miraflores'),
('Perú','Arequipa','Arequipa'), ('Perú','Arequipa','Camaná'),
('Perú','La Libertad','Trujillo'), ('Perú','La Libertad','Chepén'),
('Perú','Piura','Piura'), ('Perú','Piura','Sullana'),
('Perú','Lambayeque','Chiclayo'), ('Perú','Lambayeque','Lambayeque'),
('Perú','Cusco','Cusco'), ('Perú','Cusco','Urubamba'),
('Perú','Junín','Huancayo'), ('Perú','Junín','Tarma'),
('Perú','Áncash','Chimbote'), ('Perú','Áncash','Huaraz'),
('Perú','Loreto','Iquitos'), ('Perú','Loreto','Yurimaguas'),
('Perú','Ica','Ica'), ('Perú','Ica','Chincha Alta'),
-- Ecuador
('Ecuador','Pichincha','Quito'), ('Ecuador','Pichincha','Sangolquí'),
('Ecuador','Guayas','Guayaquil'), ('Ecuador','Guayas','Durán'),
('Ecuador','Azuay','Cuenca'), ('Ecuador','Azuay','Gualaceo'),
('Ecuador','Manabí','Portoviejo'), ('Ecuador','Manabí','Manta'),
('Ecuador','El Oro','Machala'), ('Ecuador','El Oro','Pasaje'),
('Ecuador','Tungurahua','Ambato'), ('Ecuador','Tungurahua','Baños'),
('Ecuador','Loja','Loja'), ('Ecuador','Loja','Catamayo'),
('Ecuador','Imbabura','Ibarra'), ('Ecuador','Imbabura','Otavalo'),
('Ecuador','Chimborazo','Riobamba'), ('Ecuador','Chimborazo','Guano'),
('Ecuador','Esmeraldas','Esmeraldas'), ('Ecuador','Esmeraldas','Atacames'),
-- Europa (EUROPEAN_CITIES, sin Madrid ni Barcelona, que ya están arriba en España)
('Francia','Isla de Francia','París'),
('Francia','Provenza-Alpes-Costa Azul','Niza'),
('Reino Unido','Inglaterra','Londres'),
('Reino Unido','Inglaterra','Mánchester'),
('Reino Unido','Escocia','Edimburgo'),
('Italia','Lacio','Roma'),
('Italia','Lombardía','Milán'),
('Italia','Véneto','Venecia'),
('Italia','Toscana','Florencia'),
('Países Bajos','Holanda Septentrional','Ámsterdam'),
('Alemania','Hesse','Fráncfort'),
('Alemania','Baviera','Múnich'),
('Alemania','Berlín','Berlín'),
('Portugal','Lisboa','Lisboa'),
('Portugal','Norte','Oporto'),
('Bélgica','Región de Bruselas','Bruselas'),
('Austria','Viena','Viena'),
('Suiza','Zúrich','Zúrich'),
('Suiza','Ginebra','Ginebra'),
('Grecia','Ática','Atenas'),
('Irlanda','Leinster','Dublín'),
('República Checa','Praga','Praga'),
('Hungría','Budapest','Budapest'),
('Polonia','Mazovia','Varsovia'),
('Dinamarca','Región Capital','Copenhague'),
('Suecia','Estocolmo','Estocolmo'),
('Noruega','Oslo','Oslo'),
('Finlandia','Uusimaa','Helsinki'),
('Turquía','Estambul','Estambul'),
('Islandia','Región Capital','Reikiavik');

INSERT INTO Departamento (nombreDepartamento, idPais)
SELECT DISTINCT t.depto, p.idPais
FROM tmp_ubic t
JOIN Pais p ON p.nombrePais = t.pais;

INSERT INTO Ciudad (nombreCiudad, idDepartamento)
SELECT t.ciudad, d.idDepartamento
FROM tmp_ubic t
JOIN Pais p ON p.nombrePais = t.pais
JOIN Departamento d ON d.nombreDepartamento = t.depto AND d.idPais = p.idPais;

DROP TEMPORARY TABLE tmp_ubic;

-- ---------------- Sedes (una por departamento de Colombia) ----------------
-- La central (Medellín) se inserta primero para que tenga idSede = 1.
INSERT INTO Sede (nombreSede, direccion, idCiudad)
SELECT 'Sede Principal Medellín', 'Carrera 43A # 1-50, El Poblado', c.idCiudad
FROM Ciudad c
JOIN Departamento d ON d.idDepartamento = c.idDepartamento
WHERE c.nombreCiudad = 'Medellín' AND d.nombreDepartamento = 'Antioquia';

INSERT INTO Sede (nombreSede, direccion, idCiudad)
SELECT x.nombre, x.direccion, c.idCiudad FROM (
  SELECT 'Sede Barranquilla' AS nombre, 'Carrera 53 # 75-120, El Prado' AS direccion, 'Barranquilla' AS ciudad, 'Atlántico' AS depto
  UNION ALL SELECT 'Sede Bogotá', 'Calle 26 # 92-32, Fontibón', 'Bogotá', 'Bogotá D.C.'
  UNION ALL SELECT 'Sede Cartagena', 'Avenida San Martín # 8-45, Bocagrande', 'Cartagena', 'Bolívar'
  UNION ALL SELECT 'Sede Tunja', 'Carrera 10 # 19-30, Centro', 'Tunja', 'Boyacá'
  UNION ALL SELECT 'Sede Manizales', 'Carrera 23 # 62-15, Cable', 'Manizales', 'Caldas'
  UNION ALL SELECT 'Sede Soacha', 'Carrera 7 # 30-18, Centro', 'Soacha', 'Cundinamarca'
  UNION ALL SELECT 'Sede Armenia', 'Carrera 14 # 18-56, Centro', 'Armenia', 'Quindío'
  UNION ALL SELECT 'Sede Pereira', 'Carrera 7 # 19-40, Centro', 'Pereira', 'Risaralda'
  UNION ALL SELECT 'Sede Cali', 'Avenida 6N # 25-50, Granada', 'Cali', 'Valle del Cauca'
) x
JOIN Departamento d ON d.nombreDepartamento = x.depto
JOIN Pais p ON p.idPais = d.idPais AND p.nombrePais = 'Colombia'
JOIN Ciudad c ON c.nombreCiudad = x.ciudad AND c.idDepartamento = d.idDepartamento;

-- ---------------- Usuarios: 10 asesores (uno por sede) y 4 clientes ----------------
-- Contraseñas en texto plano: asesores trabajador1..trabajador10, clientes cliente01..cliente04.
CREATE TEMPORARY TABLE tmp_usuario (
    rol VARCHAR(10), pn VARCHAR(20), sn VARCHAR(20), pa VARCHAR(20), sa VARCHAR(20),
    ident VARCHAR(20), tipoId VARCHAR(40), tel VARCHAR(20), nac DATE,
    correo VARCHAR(100), clave VARCHAR(50), dir VARCHAR(50),
    ciudad VARCHAR(40), depto VARCHAR(40), pais VARCHAR(25),
    sede VARCHAR(40), salario DECIMAL(12,2), contratacion DATE
);
INSERT INTO tmp_usuario VALUES
('EMPLEADO','Carlos','Andrés','Restrepo','Gómez','1017234501','CC','+57 3001110001','1990-03-12','asesor.medellin@viajacol.com','trabajador1','Calle 10 # 35-20','Medellín','Antioquia','Colombia','Sede Principal Medellín',2800000,'2024-02-01'),
('EMPLEADO','Laura','Sofía','Pérez','Mejía','1045678902','CC','+57 3001110002','1993-07-25','asesor.barranquilla@viajacol.com','trabajador2','Carrera 46 # 80-15','Barranquilla','Atlántico','Colombia','Sede Barranquilla',2600000,'2024-03-15'),
('EMPLEADO','Juan','David','Ortiz','Salazar','1020345603','CC','+57 3001110003','1989-11-08','asesor.bogota@viajacol.com','trabajador3','Calle 85 # 15-30','Bogotá','Bogotá D.C.','Colombia','Sede Bogotá',2800000,'2023-09-01'),
('EMPLEADO','Valentina',NULL,'Marín','Herrera','1047890104','CC','+57 3001110004','1995-01-19','asesor.cartagena@viajacol.com','trabajador4','Barrio Manga # 21-44','Cartagena','Bolívar','Colombia','Sede Cartagena',2600000,'2024-05-06'),
('EMPLEADO','Santiago',NULL,'Rojas','Pineda','1049012305','CC','+57 3001110005','1992-05-30','asesor.tunja@viajacol.com','trabajador5','Calle 20 # 8-35','Tunja','Boyacá','Colombia','Sede Tunja',2500000,'2024-06-17'),
('EMPLEADO','Daniela','Alejandra','Ramírez','Cardona','1053123406','CC','+57 3001110006','1994-09-14','asesor.manizales@viajacol.com','trabajador6','Carrera 21 # 50-12','Manizales','Caldas','Colombia','Sede Manizales',2500000,'2024-01-22'),
('EMPLEADO','Andrés','Felipe','Torres','Vargas','1010234507','CC','+57 3001110007','1991-12-03','asesor.soacha@viajacol.com','trabajador7','Calle 13 # 7-60','Soacha','Cundinamarca','Colombia','Sede Soacha',2500000,'2024-08-12'),
('EMPLEADO','Camila','Andrea','Quintero','Ospina','1094345608','CC','+57 3001110008','1996-04-27','asesor.armenia@viajacol.com','trabajador8','Carrera 19 # 12-40','Armenia','Quindío','Colombia','Sede Armenia',2600000,'2023-11-20'),
('EMPLEADO','Mateo',NULL,'Jiménez','Arango','1088456709','CC','+57 3001110009','1990-08-09','asesor.pereira@viajacol.com','trabajador9','Calle 17 # 6-25','Pereira','Risaralda','Colombia','Sede Pereira',2600000,'2024-04-08'),
('EMPLEADO','Isabela',NULL,'Mosquera','Valencia','1144567810','CC','+57 3001110010','1993-02-16','asesor.cali@viajacol.com','trabajador10','Calle 9 # 24-18','Cali','Valle del Cauca','Colombia','Sede Cali',2700000,'2024-02-26'),
('CLIENTE','Ana','María','Gutiérrez','Londoño','1094567811','CC','+57 3105550001','1998-06-21','ana.gutierrez@correo.com','cliente01','Calle 21 # 15-30','Armenia','Quindío','Colombia',NULL,NULL,NULL),
('CLIENTE','Pedro','Luis','Martínez','Ruiz','80123456','CC','+57 3105550002','1985-10-02','pedro.martinez@correo.com','cliente02','Carrera 11 # 93-20','Bogotá','Bogotá D.C.','Colombia',NULL,NULL,NULL),
('CLIENTE','Lucía',NULL,'Fernández','Gil','X1234567','PA','+34 612345678','1991-03-17','lucia.fernandez@correo.com','cliente03','Calle Mayor 12','Madrid','Comunidad de Madrid','España',NULL,NULL,NULL),
('CLIENTE','Diego','Alejandro','Cano','Duque','16234567','CC','+57 3105550004','2000-12-05','diego.cano@correo.com','cliente04','Carrera 8 # 20-14','Cali','Valle del Cauca','Colombia',NULL,NULL,NULL);

INSERT INTO Usuario (primerNombre, segundoNombre, primerApellido, segundoApellido, identificacion, telefono,
                     fechaNacimiento, correo, contrasenia, direccion, idRol, idTipoIdentificacion, idCiudad)
SELECT t.pn, t.sn, t.pa, t.sa, t.ident, t.tel, t.nac, t.correo, t.clave, t.dir,
       r.idRol, ti.idTipoIdentificacion, c.idCiudad
FROM tmp_usuario t
JOIN Rol r                 ON r.nombreRol = t.rol
JOIN TipoIdentificacion ti ON ti.nombreIdentificacion = t.tipoId
JOIN Pais p                ON p.nombrePais = t.pais
JOIN Departamento d        ON d.nombreDepartamento = t.depto AND d.idPais = p.idPais
JOIN Ciudad c              ON c.nombreCiudad = t.ciudad AND c.idDepartamento = d.idDepartamento;

INSERT INTO Empleado (idUsuario, salario, fechaContratacion, idCargo, idSede)
SELECT u.idUsuario, t.salario, t.contratacion, cg.idCargo, s.idSede
FROM tmp_usuario t
JOIN Usuario u ON u.correo = t.correo
JOIN Cargo cg  ON cg.nombreCargo = 'Asesor de ventas'
JOIN Sede s    ON s.nombreSede = t.sede
WHERE t.rol = 'EMPLEADO';

INSERT INTO Cliente (idUsuario)
SELECT u.idUsuario
FROM Usuario u
JOIN Rol r ON r.idRol = u.idRol
WHERE r.nombreRol = 'CLIENTE';

DROP TEMPORARY TABLE tmp_usuario;

-- ---------------- Planes y ofertas ----------------
INSERT INTO Plan (nombrePlan, descripcionPlan, pesoMaxEquipaje, incluyeCheckIn, precioExtra) VALUES
('Básico', 'Solo equipaje de mano', 10.00, FALSE, 0.00),
('Clásico', 'Equipaje de mano y una maleta de bodega', 23.00, FALSE, 120000.00),
('Flexible', 'Maleta de bodega, check-in prioritario y cambios sin costo', 32.00, TRUE, 260000.00),
('Premium', 'Dos maletas, sala VIP, check-in prioritario y asiento preferente', 46.00, TRUE, 480000.00);
INSERT INTO Oferta (titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin) VALUES
('Bienvenida', '10% de descuento en tu primera reserva durante 2026', 10.00, '2026-01-01', '2026-12-31'),
('Mitad de Año', '12% de descuento general en reservas realizadas durante junio', 12.00, '2026-06-01', '2026-06-30'),
('Semana de Receso', '15% de descuento general en reservas de temporada de octubre', 15.00, '2026-10-01', '2026-10-15'),
('Black Friday', '25% de descuento en cualquier reserva realizada en noviembre', 25.00, '2026-11-01', '2026-11-30'),
('Especial Navidad', '15% de descuento general para reservas de fin de año', 15.00, '2026-12-01', '2026-12-31'),
('Año Nuevo 2027', '20% de descuento general para empezar el año viajando', 20.00, '2027-01-01', '2027-01-31');

-- ---------------- Vehículos ----------------
-- Varios quedan sin ruta a propósito, para que los uses al crear rutas nuevas.
INSERT INTO Vehiculo (codigoIdentificacion, idTipoTransporte, capacidad)
SELECT x.codigo, t.idTipoTransporte, x.capacidad FROM (
  SELECT 'HK-5120' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'HK-5288', 'AVION', 180
  UNION ALL SELECT 'HK-5301', 'AVION', 180
  UNION ALL SELECT 'HK-5317', 'AVION', 180
  UNION ALL SELECT 'EC-NVJ', 'AVION', 300
  UNION ALL SELECT 'EC-MXV', 'AVION', 180
  UNION ALL SELECT 'G-EUYR', 'AVION', 180
  UNION ALL SELECT 'I-RIBN', 'AVION', 180
  UNION ALL SELECT 'F-HBNK', 'AVION', 180
  UNION ALL SELECT 'EI-DVM', 'AVION', 180
  UNION ALL SELECT 'PH-BXA', 'AVION', 180
  UNION ALL SELECT 'LN-RKF', 'AVION', 180
  UNION ALL SELECT 'OH-LXF', 'AVION', 180
  UNION ALL SELECT 'TF-ICE', 'AVION', 160
  UNION ALL SELECT 'TC-JTA', 'AVION', 180
  UNION ALL SELECT 'D-AIZQ', 'AVION', 180
  UNION ALL SELECT 'SP-LWA', 'AVION', 180
  UNION ALL SELECT 'G-EZWA', 'AVION', 180
  UNION ALL SELECT 'AVE-103', 'TREN', 400
  UNION ALL SELECT 'EST-3201', 'TREN', 400
  UNION ALL SELECT 'THA-4532', 'TREN', 400
  UNION ALL SELECT 'THA-4534', 'TREN', 400
  UNION ALL SELECT 'FR-1000A', 'TREN', 400
  UNION ALL SELECT 'FR-1000B', 'TREN', 400
  UNION ALL SELECT 'ITA-0912', 'TREN', 400
  UNION ALL SELECT 'ICE-4711', 'TREN', 400
  UNION ALL SELECT 'ICE-4712', 'TREN', 400
  UNION ALL SELECT 'EC-171', 'TREN', 400
  UNION ALL SELECT 'RJ-0075', 'TREN', 400
  UNION ALL SELECT 'RJ-0061', 'TREN', 400
  UNION ALL SELECT 'IC-0712', 'TREN', 400
  UNION ALL SELECT 'EC-19', 'TREN', 400
  UNION ALL SELECT 'AP-0131', 'TREN', 400
  UNION ALL SELECT 'SJ-0393', 'TREN', 400
  UNION ALL SELECT 'TGV-6173', 'TREN', 400
  UNION ALL SELECT 'RN-0332', 'TREN', 400
  UNION ALL SELECT 'TSX-482', 'BUS', 40
  UNION ALL SELECT 'WPK-318', 'BUS', 40
  UNION ALL SELECT 'SMB-702', 'BUS', 40
  UNION ALL SELECT 'VCX-215', 'BUS', 40
  UNION ALL SELECT 'ALS-2210', 'BUS', 40
  UNION ALL SELECT 'FLX-7731', 'BUS', 40
  UNION ALL SELECT 'NEX-0451', 'BUS', 40
  UNION ALL SELECT 'NEX-0452', 'BUS', 40
  UNION ALL SELECT 'FLX-7740', 'BUS', 40
  UNION ALL SELECT 'FLX-7752', 'BUS', 40
  UNION ALL SELECT 'FLX-7763', 'BUS', 40
  UNION ALL SELECT 'FLX-7771', 'BUS', 40
  -- Vehículos de las rutas agregadas (uno por par de ciudades)
  UNION ALL SELECT 'SX-TYZ', 'AVION', 220
  UNION ALL SELECT 'SX-XAM', 'AVION', 220
  UNION ALL SELECT 'SX-CMK', 'AVION', 180
  UNION ALL SELECT 'SX-GUE', 'AVION', 220
  UNION ALL SELECT 'SX-WWH', 'AVION', 220
  UNION ALL SELECT 'SX-EDL', 'AVION', 180
  UNION ALL SELECT 'SX-SDC', 'AVION', 220
  UNION ALL SELECT 'SX-NJW', 'AVION', 180
  UNION ALL SELECT 'SX-KXD', 'AVION', 180
  UNION ALL SELECT 'SX-MSS', 'AVION', 180
  UNION ALL SELECT 'EC-DUT', 'AVION', 220
  UNION ALL SELECT 'EC-HMY', 'AVION', 180
  UNION ALL SELECT 'EC-RVM', 'AVION', 180
  UNION ALL SELECT 'EC-CCC', 'AVION', 180
  UNION ALL SELECT 'EC-SKJ', 'AVION', 180
  UNION ALL SELECT 'EC-YHF', 'AVION', 180
  UNION ALL SELECT 'EC-LPT', 'AVION', 180
  UNION ALL SELECT 'EC-ULL', 'AVION', 220
  UNION ALL SELECT 'EC-YSN', 'AVION', 180
  UNION ALL SELECT 'EC-LFG', 'AVION', 180
  UNION ALL SELECT 'EC-RHZ', 'AVION', 220
  UNION ALL SELECT 'EC-MMP', 'AVION', 180
  UNION ALL SELECT 'EC-VNG', 'AVION', 180
  UNION ALL SELECT 'EC-ERV', 'AVION', 180
  UNION ALL SELECT 'EC-YEP', 'AVION', 180
  UNION ALL SELECT 'EC-JLD', 'AVION', 180
  UNION ALL SELECT 'EC-LJR', 'AVION', 180
  UNION ALL SELECT 'EC-JEU', 'AVION', 220
  UNION ALL SELECT 'EC-XJH', 'AVION', 180
  UNION ALL SELECT 'EC-LEU', 'AVION', 180
  UNION ALL SELECT 'EC-DJK', 'AVION', 180
  UNION ALL SELECT 'EC-VRX', 'AVION', 180
  UNION ALL SELECT 'D-NNAU', 'AVION', 180
  UNION ALL SELECT 'D-ZLYP', 'AVION', 180
  UNION ALL SELECT 'D-LDYS', 'AVION', 180
  UNION ALL SELECT 'D-YXYU', 'AVION', 180
  UNION ALL SELECT 'D-TGPM', 'AVION', 180
  UNION ALL SELECT 'D-XLBN', 'AVION', 180
  UNION ALL SELECT 'D-FAJU', 'AVION', 180
  UNION ALL SELECT 'D-ZJEL', 'AVION', 220
  UNION ALL SELECT 'D-VXWP', 'AVION', 180
  UNION ALL SELECT 'D-SSLA', 'AVION', 220
  UNION ALL SELECT 'D-BHXD', 'AVION', 180
  UNION ALL SELECT 'OO-YJL', 'AVION', 220
  UNION ALL SELECT 'OO-AGR', 'AVION', 180
  UNION ALL SELECT 'OO-TJW', 'AVION', 180
  UNION ALL SELECT 'OO-XMC', 'AVION', 180
  UNION ALL SELECT 'OO-UZG', 'AVION', 180
  UNION ALL SELECT 'OO-UAD', 'AVION', 220
  UNION ALL SELECT 'OO-ELH', 'AVION', 180
  UNION ALL SELECT 'OO-RGW', 'AVION', 180
  UNION ALL SELECT 'OO-YFZ', 'AVION', 180
  UNION ALL SELECT 'OO-KCV', 'AVION', 180
  UNION ALL SELECT 'OO-PZT', 'AVION', 180
  UNION ALL SELECT 'OO-YVU', 'AVION', 180
  UNION ALL SELECT 'OO-EDA', 'AVION', 180
  UNION ALL SELECT 'OO-JPB', 'AVION', 220
  UNION ALL SELECT 'OO-FUT', 'AVION', 180
  UNION ALL SELECT 'OO-HSY', 'AVION', 180
  UNION ALL SELECT 'OO-YLL', 'AVION', 180
  UNION ALL SELECT 'HA-MLW', 'AVION', 180
  UNION ALL SELECT 'HA-SJS', 'AVION', 180
  UNION ALL SELECT 'HA-LJB', 'AVION', 180
  UNION ALL SELECT 'HA-XSE', 'AVION', 180
  UNION ALL SELECT 'HA-ASM', 'AVION', 180
  UNION ALL SELECT 'HA-JRG', 'AVION', 180
  UNION ALL SELECT 'HA-MJT', 'AVION', 180
  UNION ALL SELECT 'HA-NKG', 'AVION', 180
  UNION ALL SELECT 'OY-VBP', 'AVION', 180
  UNION ALL SELECT 'OY-CXY', 'AVION', 220
  UNION ALL SELECT 'OY-TYU', 'AVION', 180
  UNION ALL SELECT 'OY-KSW', 'AVION', 180
  UNION ALL SELECT 'OY-CTT', 'AVION', 180
  UNION ALL SELECT 'OY-WLS', 'AVION', 220
  UNION ALL SELECT 'OY-LYL', 'AVION', 180
  UNION ALL SELECT 'EI-MFC', 'AVION', 180
  UNION ALL SELECT 'EI-UFJ', 'AVION', 180
  UNION ALL SELECT 'EI-TTN', 'AVION', 180
  UNION ALL SELECT 'EI-XDW', 'AVION', 180
  UNION ALL SELECT 'EI-GAR', 'AVION', 220
  UNION ALL SELECT 'EI-HGP', 'AVION', 180
  UNION ALL SELECT 'EI-TTE', 'AVION', 180
  UNION ALL SELECT 'EI-TGJ', 'AVION', 180
  UNION ALL SELECT 'EI-ZSE', 'AVION', 180
  UNION ALL SELECT 'EI-UYL', 'AVION', 180
  UNION ALL SELECT 'EI-MXN', 'AVION', 220
  UNION ALL SELECT 'EI-UWU', 'AVION', 180
  UNION ALL SELECT 'TC-GEW', 'AVION', 220
  UNION ALL SELECT 'TC-UZE', 'AVION', 180
  UNION ALL SELECT 'TC-EEY', 'AVION', 220
  UNION ALL SELECT 'TC-VBY', 'AVION', 180
  UNION ALL SELECT 'SE-BJK', 'AVION', 180
  UNION ALL SELECT 'SE-FEJ', 'AVION', 220
  UNION ALL SELECT 'SE-UZM', 'AVION', 180
  UNION ALL SELECT 'SE-ERF', 'AVION', 180
  UNION ALL SELECT 'SE-FZB', 'AVION', 220
  UNION ALL SELECT 'SE-FYE', 'AVION', 220
  UNION ALL SELECT 'SE-PXW', 'AVION', 180
  UNION ALL SELECT 'I-SUAK', 'AVION', 180
  UNION ALL SELECT 'I-HTMU', 'AVION', 180
  UNION ALL SELECT 'D-CRYP', 'AVION', 180
  UNION ALL SELECT 'D-ELEZ', 'AVION', 180
  UNION ALL SELECT 'D-TBSA', 'AVION', 180
  UNION ALL SELECT 'D-BDMV', 'AVION', 220
  UNION ALL SELECT 'D-ZZJL', 'AVION', 180
  UNION ALL SELECT 'D-AFTG', 'AVION', 180
  UNION ALL SELECT 'D-CPYP', 'AVION', 180
  UNION ALL SELECT 'D-GFHH', 'AVION', 180
  UNION ALL SELECT 'D-YSGM', 'AVION', 180
  UNION ALL SELECT 'D-FUPP', 'AVION', 180
  UNION ALL SELECT 'D-RMZG', 'AVION', 180
  UNION ALL SELECT 'D-KXRU', 'AVION', 220
  UNION ALL SELECT 'D-WSCG', 'AVION', 180
  UNION ALL SELECT 'D-GDCM', 'AVION', 180
  UNION ALL SELECT 'HB-ZYW', 'AVION', 180
  UNION ALL SELECT 'HB-ZFD', 'AVION', 180
  UNION ALL SELECT 'HB-FGX', 'AVION', 180
  UNION ALL SELECT 'HB-PGP', 'AVION', 180
  UNION ALL SELECT 'HB-AUE', 'AVION', 220
  UNION ALL SELECT 'HB-XXJ', 'AVION', 180
  UNION ALL SELECT 'HB-KMN', 'AVION', 220
  UNION ALL SELECT 'HB-AST', 'AVION', 180
  UNION ALL SELECT 'HB-RJM', 'AVION', 180
  UNION ALL SELECT 'HB-SED', 'AVION', 180
  UNION ALL SELECT 'HB-EUH', 'AVION', 220
  UNION ALL SELECT 'OH-MRN', 'AVION', 220
  UNION ALL SELECT 'OH-WXV', 'AVION', 220
  UNION ALL SELECT 'OH-BZY', 'AVION', 180
  UNION ALL SELECT 'OH-ULJ', 'AVION', 220
  UNION ALL SELECT 'OH-YCR', 'AVION', 220
  UNION ALL SELECT 'OH-RMU', 'AVION', 220
  UNION ALL SELECT 'CS-TTD', 'AVION', 220
  UNION ALL SELECT 'CS-JKB', 'AVION', 180
  UNION ALL SELECT 'CS-CVB', 'AVION', 180
  UNION ALL SELECT 'CS-CTR', 'AVION', 220
  UNION ALL SELECT 'CS-VDN', 'AVION', 180
  UNION ALL SELECT 'CS-XVR', 'AVION', 180
  UNION ALL SELECT 'CS-WSU', 'AVION', 220
  UNION ALL SELECT 'CS-AEL', 'AVION', 220
  UNION ALL SELECT 'CS-EAD', 'AVION', 220
  UNION ALL SELECT 'CS-NCF', 'AVION', 220
  UNION ALL SELECT 'CS-UJA', 'AVION', 180
  UNION ALL SELECT 'G-BCWT', 'AVION', 180
  UNION ALL SELECT 'G-DLZE', 'AVION', 180
  UNION ALL SELECT 'G-KSHL', 'AVION', 180
  UNION ALL SELECT 'G-ZRZH', 'AVION', 180
  UNION ALL SELECT 'G-NHTL', 'AVION', 180
  UNION ALL SELECT 'G-PBSH', 'AVION', 180
  UNION ALL SELECT 'G-WFRG', 'AVION', 180
  UNION ALL SELECT 'G-DVSV', 'AVION', 220
  UNION ALL SELECT 'G-NSTA', 'AVION', 180
  UNION ALL SELECT 'G-SGNB', 'AVION', 180
  UNION ALL SELECT 'G-TREK', 'AVION', 180
  UNION ALL SELECT 'G-HTLJ', 'AVION', 180
  UNION ALL SELECT 'G-PVAM', 'AVION', 180
  UNION ALL SELECT 'G-WTFT', 'AVION', 180
  UNION ALL SELECT 'G-DVMG', 'AVION', 180
  UNION ALL SELECT 'G-VCUF', 'AVION', 180
  UNION ALL SELECT 'G-RNDD', 'AVION', 180
  UNION ALL SELECT 'G-PNAH', 'AVION', 180
  UNION ALL SELECT 'EC-GBJ', 'AVION', 220
  UNION ALL SELECT 'EC-FTJ', 'AVION', 220
  UNION ALL SELECT 'EC-MCJ', 'AVION', 220
  UNION ALL SELECT 'EC-FBG', 'AVION', 180
  UNION ALL SELECT 'EC-ZXZ', 'AVION', 180
  UNION ALL SELECT 'EC-UDR', 'AVION', 220
  UNION ALL SELECT 'EC-UYB', 'AVION', 180
  UNION ALL SELECT 'EC-MZR', 'AVION', 180
  UNION ALL SELECT 'EC-TXB', 'AVION', 180
  UNION ALL SELECT 'EC-GXX', 'AVION', 220
  UNION ALL SELECT 'EC-SBK', 'AVION', 180
  UNION ALL SELECT 'EC-CFX', 'AVION', 180
  UNION ALL SELECT 'EC-CJG', 'AVION', 180
  UNION ALL SELECT 'EC-AKW', 'AVION', 220
  UNION ALL SELECT 'EC-XBS', 'AVION', 180
  UNION ALL SELECT 'EC-JCZ', 'AVION', 220
  UNION ALL SELECT 'EC-UFU', 'AVION', 220
  UNION ALL SELECT 'EC-XTV', 'AVION', 220
  UNION ALL SELECT 'EC-UHS', 'AVION', 180
  UNION ALL SELECT 'I-BUMH', 'AVION', 180
  UNION ALL SELECT 'I-UNRC', 'AVION', 180
  UNION ALL SELECT 'I-HBAP', 'AVION', 180
  UNION ALL SELECT 'I-NUCR', 'AVION', 180
  UNION ALL SELECT 'I-YWFF', 'AVION', 180
  UNION ALL SELECT 'I-HFUG', 'AVION', 180
  UNION ALL SELECT 'I-TLNJ', 'AVION', 180
  UNION ALL SELECT 'I-XXFR', 'AVION', 180
  UNION ALL SELECT 'I-CAMC', 'AVION', 180
  UNION ALL SELECT 'I-EVTH', 'AVION', 180
  UNION ALL SELECT 'I-SZEY', 'AVION', 220
  UNION ALL SELECT 'I-LFSU', 'AVION', 180
  UNION ALL SELECT 'I-DEAN', 'AVION', 180
  UNION ALL SELECT 'D-CZSX', 'AVION', 180
  UNION ALL SELECT 'D-PYVL', 'AVION', 180
  UNION ALL SELECT 'D-TMCY', 'AVION', 180
  UNION ALL SELECT 'D-VAXJ', 'AVION', 180
  UNION ALL SELECT 'D-HDNH', 'AVION', 180
  UNION ALL SELECT 'D-KWYV', 'AVION', 180
  UNION ALL SELECT 'D-YVFK', 'AVION', 220
  UNION ALL SELECT 'D-UAAL', 'AVION', 180
  UNION ALL SELECT 'D-FZRV', 'AVION', 180
  UNION ALL SELECT 'D-DHYS', 'AVION', 180
  UNION ALL SELECT 'D-FWTY', 'AVION', 220
  UNION ALL SELECT 'D-SDCE', 'AVION', 180
  UNION ALL SELECT 'F-AWTK', 'AVION', 180
  UNION ALL SELECT 'F-EPJA', 'AVION', 180
  UNION ALL SELECT 'CS-RWK', 'AVION', 220
  UNION ALL SELECT 'CS-ENX', 'AVION', 180
  UNION ALL SELECT 'CS-XAR', 'AVION', 220
  UNION ALL SELECT 'CS-VDD', 'AVION', 220
  UNION ALL SELECT 'CS-HLR', 'AVION', 180
  UNION ALL SELECT 'CS-CNJ', 'AVION', 180
  UNION ALL SELECT 'CS-RRK', 'AVION', 220
  UNION ALL SELECT 'CS-JRW', 'AVION', 180
  UNION ALL SELECT 'CS-PSK', 'AVION', 180
  UNION ALL SELECT 'CS-LFD', 'AVION', 220
  UNION ALL SELECT 'CS-WUA', 'AVION', 220
  UNION ALL SELECT 'CS-GGZ', 'AVION', 220
  UNION ALL SELECT 'CS-KZN', 'AVION', 220
  UNION ALL SELECT 'CS-HZY', 'AVION', 180
  UNION ALL SELECT 'CS-HNE', 'AVION', 220
  UNION ALL SELECT 'LN-WLX', 'AVION', 180
  UNION ALL SELECT 'LN-UNG', 'AVION', 180
  UNION ALL SELECT 'LN-WUK', 'AVION', 180
  UNION ALL SELECT 'LN-UUN', 'AVION', 180
  UNION ALL SELECT 'LN-ZJM', 'AVION', 220
  UNION ALL SELECT 'LN-RHF', 'AVION', 180
  UNION ALL SELECT 'LN-TKB', 'AVION', 180
  UNION ALL SELECT 'F-WGKF', 'AVION', 220
  UNION ALL SELECT 'F-PYVE', 'AVION', 180
  UNION ALL SELECT 'F-RNXS', 'AVION', 180
  UNION ALL SELECT 'F-YBWK', 'AVION', 180
  UNION ALL SELECT 'F-BWTL', 'AVION', 180
  UNION ALL SELECT 'F-UKPH', 'AVION', 180
  UNION ALL SELECT 'F-ZXUT', 'AVION', 180
  UNION ALL SELECT 'F-KYWT', 'AVION', 220
  UNION ALL SELECT 'F-MMNT', 'AVION', 180
  UNION ALL SELECT 'F-HAPT', 'AVION', 180
  UNION ALL SELECT 'F-PNWM', 'AVION', 180
  UNION ALL SELECT 'F-HUVN', 'AVION', 180
  UNION ALL SELECT 'F-MADH', 'AVION', 180
  UNION ALL SELECT 'F-GFSW', 'AVION', 180
  UNION ALL SELECT 'F-NSFP', 'AVION', 220
  UNION ALL SELECT 'F-TGMA', 'AVION', 180
  UNION ALL SELECT 'F-UCDD', 'AVION', 180
  UNION ALL SELECT 'F-GNDR', 'AVION', 180
  UNION ALL SELECT 'OK-WMX', 'AVION', 180
  UNION ALL SELECT 'OK-TXD', 'AVION', 180
  UNION ALL SELECT 'OK-PHL', 'AVION', 180
  UNION ALL SELECT 'OK-KAD', 'AVION', 180
  UNION ALL SELECT 'OK-CLY', 'AVION', 180
  UNION ALL SELECT 'OK-ERX', 'AVION', 180
  UNION ALL SELECT 'OK-PDX', 'AVION', 220
  UNION ALL SELECT 'TF-DGU', 'AVION', 180
  UNION ALL SELECT 'TF-SLW', 'AVION', 180
  UNION ALL SELECT 'TF-FDC', 'AVION', 220
  UNION ALL SELECT 'I-MHER', 'AVION', 180
  UNION ALL SELECT 'I-RDUP', 'AVION', 180
  UNION ALL SELECT 'I-ECAY', 'AVION', 180
  UNION ALL SELECT 'I-RPCR', 'AVION', 180
  UNION ALL SELECT 'I-SBRS', 'AVION', 180
  UNION ALL SELECT 'I-MDBP', 'AVION', 220
  UNION ALL SELECT 'I-FPXC', 'AVION', 220
  UNION ALL SELECT 'I-JZUT', 'AVION', 180
  UNION ALL SELECT 'I-HPDM', 'AVION', 220
  UNION ALL SELECT 'I-FAEW', 'AVION', 180
  UNION ALL SELECT 'I-WKJP', 'AVION', 180
  UNION ALL SELECT 'I-DZHM', 'AVION', 220
  UNION ALL SELECT 'I-CPXC', 'AVION', 180
  UNION ALL SELECT 'I-VWJL', 'AVION', 180
  UNION ALL SELECT 'I-PWVK', 'AVION', 180
  UNION ALL SELECT 'I-HWTF', 'AVION', 220
  UNION ALL SELECT 'I-TPJB', 'AVION', 180
  UNION ALL SELECT 'I-UWJT', 'AVION', 180
  UNION ALL SELECT 'I-RKPJ', 'AVION', 180
  UNION ALL SELECT 'I-GZVR', 'AVION', 180
  UNION ALL SELECT 'I-FMTK', 'AVION', 180
  UNION ALL SELECT 'SP-JKD', 'AVION', 180
  UNION ALL SELECT 'SP-WHA', 'AVION', 180
  UNION ALL SELECT 'SP-GWW', 'AVION', 180
  UNION ALL SELECT 'SP-LZL', 'AVION', 180
  UNION ALL SELECT 'SP-XDY', 'AVION', 180
  UNION ALL SELECT 'SP-LNX', 'AVION', 220
  UNION ALL SELECT 'I-PWRW', 'AVION', 180
  UNION ALL SELECT 'OE-NYD', 'AVION', 180
  UNION ALL SELECT 'OE-MPY', 'AVION', 180
  UNION ALL SELECT 'OE-RVL', 'AVION', 180
  UNION ALL SELECT 'OE-VNM', 'AVION', 180
  UNION ALL SELECT 'OE-AMN', 'AVION', 180
  UNION ALL SELECT 'OE-MJS', 'AVION', 180
  UNION ALL SELECT 'OE-AMP', 'AVION', 180
  UNION ALL SELECT 'OE-RBW', 'AVION', 180
  UNION ALL SELECT 'OE-AZK', 'AVION', 180
  UNION ALL SELECT 'OE-UUB', 'AVION', 220
  UNION ALL SELECT 'HB-DLP', 'AVION', 180
  UNION ALL SELECT 'HB-JNK', 'AVION', 180
  UNION ALL SELECT 'HB-ZJA', 'AVION', 180
  UNION ALL SELECT 'HB-GGB', 'AVION', 180
  UNION ALL SELECT 'HB-CHF', 'AVION', 180
  UNION ALL SELECT 'HB-BZM', 'AVION', 180
  UNION ALL SELECT 'HB-WUR', 'AVION', 180
  UNION ALL SELECT 'HB-DYW', 'AVION', 180
  UNION ALL SELECT 'HB-UTX', 'AVION', 220
  UNION ALL SELECT 'HB-WBK', 'AVION', 180
  UNION ALL SELECT 'PH-EDP', 'AVION', 220
  UNION ALL SELECT 'PH-HDZ', 'AVION', 180
  UNION ALL SELECT 'PH-CDC', 'AVION', 180
  UNION ALL SELECT 'PH-VNB', 'AVION', 180
  UNION ALL SELECT 'PH-ERU', 'AVION', 220
  UNION ALL SELECT 'PH-AKP', 'AVION', 180
  UNION ALL SELECT 'PH-MUY', 'AVION', 180
  UNION ALL SELECT 'PH-RGB', 'AVION', 180
  UNION ALL SELECT 'PH-PKE', 'AVION', 180
  UNION ALL SELECT 'PH-JXU', 'AVION', 220
  UNION ALL SELECT 'PH-FWN', 'AVION', 180
  UNION ALL SELECT 'PH-ZBC', 'AVION', 180
  UNION ALL SELECT 'PH-RVP', 'AVION', 180
  UNION ALL SELECT 'PH-SRX', 'AVION', 180
  UNION ALL SELECT 'PH-XEA', 'AVION', 180
  UNION ALL SELECT 'PH-KDE', 'AVION', 220
  UNION ALL SELECT 'PH-PGJ', 'AVION', 180
  UNION ALL SELECT 'PH-RTW', 'AVION', 180
  UNION ALL SELECT 'ICE-110', 'TREN', 400
  UNION ALL SELECT 'ICE-124', 'TREN', 400
  UNION ALL SELECT 'FR-121', 'TREN', 400
  UNION ALL SELECT 'ICE-135', 'TREN', 400
  UNION ALL SELECT 'AP-119', 'TREN', 400
  UNION ALL SELECT 'LNER-102', 'TREN', 400
  UNION ALL SELECT 'LNER-109', 'TREN', 400
  UNION ALL SELECT 'AVE-113', 'TREN', 400
  UNION ALL SELECT 'FR-151', 'TREN', 400
  UNION ALL SELECT 'FR-176', 'TREN', 400
  UNION ALL SELECT 'FR-181', 'TREN', 400
  UNION ALL SELECT 'ICE-159', 'TREN', 400
  UNION ALL SELECT 'ICE-185', 'TREN', 400
  UNION ALL SELECT 'ICE-190', 'TREN', 400
  UNION ALL SELECT 'TGV-101', 'TREN', 400
  UNION ALL SELECT 'CD-124', 'TREN', 400
  UNION ALL SELECT 'FR-218', 'TREN', 400
  UNION ALL SELECT 'RJ-106', 'TREN', 400
  UNION ALL SELECT 'SBB-118', 'TREN', 400
  UNION ALL SELECT 'SBB-154', 'TREN', 400
  UNION ALL SELECT 'FLX-8015', 'BUS', 40
  UNION ALL SELECT 'FLX-8028', 'BUS', 40
  UNION ALL SELECT 'FLX-8050', 'BUS', 40
  UNION ALL SELECT 'FLX-8067', 'BUS', 40
  UNION ALL SELECT 'FLX-8073', 'BUS', 40
  UNION ALL SELECT 'FLX-8093', 'BUS', 40
  UNION ALL SELECT 'FLX-8128', 'BUS', 40
  UNION ALL SELECT 'FLX-8164', 'BUS', 40
  UNION ALL SELECT 'FLX-8172', 'BUS', 40
  UNION ALL SELECT 'FLX-8207', 'BUS', 40
  UNION ALL SELECT 'FLX-8212', 'BUS', 40
  UNION ALL SELECT 'FLX-8227', 'BUS', 40
  UNION ALL SELECT 'FLX-8257', 'BUS', 40
  UNION ALL SELECT 'FLX-8293', 'BUS', 40
  UNION ALL SELECT 'FLX-8316', 'BUS', 40
  UNION ALL SELECT 'FLX-8325', 'BUS', 40
  UNION ALL SELECT 'FLX-8336', 'BUS', 40
  UNION ALL SELECT 'FLX-8346', 'BUS', 40
  UNION ALL SELECT 'FLX-8359', 'BUS', 40
  UNION ALL SELECT 'FLX-8395', 'BUS', 40
  UNION ALL SELECT 'FLX-8412', 'BUS', 40
  UNION ALL SELECT 'FLX-8420', 'BUS', 40
  UNION ALL SELECT 'FLX-8445', 'BUS', 40
  UNION ALL SELECT 'FLX-8462', 'BUS', 40
  UNION ALL SELECT 'FLX-8493', 'BUS', 40
  UNION ALL SELECT 'FLX-8517', 'BUS', 40
  UNION ALL SELECT 'FLX-8530', 'BUS', 40
  UNION ALL SELECT 'FLX-8543', 'BUS', 40
  UNION ALL SELECT 'FLX-8555', 'BUS', 40
  UNION ALL SELECT 'FLX-8579', 'BUS', 40
  UNION ALL SELECT 'FLX-8610', 'BUS', 40
  UNION ALL SELECT 'FLX-8615', 'BUS', 40
  UNION ALL SELECT 'FLX-8624', 'BUS', 40
  UNION ALL SELECT 'FLX-8643', 'BUS', 40
  UNION ALL SELECT 'FLX-8678', 'BUS', 40
) x JOIN TipoTransporte t ON t.nombreTipoTransporte = x.tipo;

-- ---------------- Rutas europeas (798 rutas: 44 iniciales + 754 agregadas) ----------------
-- Las agregadas las genera tools/gen_rutas.py: avión para islas, mar de por medio o
-- distancias largas; tren o bus por tierra hasta ~850 km. Cada ciudad tiene entre 20 y 28 destinos.
-- sentido 1 = ida del par, 2 = vuelta. El mismo vehículo cubre ida y vuelta.
-- Madrid-Barcelona y París-Londres tienen dos medios (avión y tren).
CREATE TEMPORARY TABLE tmp_ruta (
    orden INT, sentido TINYINT, origen VARCHAR(40), destino VARCHAR(40),
    distancia DECIMAL(10,2), duracion INT, precio DECIMAL(12,2),
    dirOrigen VARCHAR(40), dirDestino VARCHAR(40), vehiculo VARCHAR(25)
);
INSERT INTO tmp_ruta VALUES
-- Aviones
(1, 1, 'Madrid', 'París', 1050, 120, 520000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Charles de Gaulle', 'EC-MXV'),
(2, 2, 'París', 'Madrid', 1050, 120, 520000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Adolfo Suárez Barajas', 'EC-MXV'),
(3, 1, 'Madrid', 'Londres', 1260, 140, 560000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Heathrow', 'G-EUYR'),
(4, 2, 'Londres', 'Madrid', 1260, 140, 560000, 'Aeropuerto de Heathrow', 'Aeropuerto Adolfo Suárez Barajas', 'G-EUYR'),
(5, 1, 'Barcelona', 'Roma', 860, 115, 480000, 'Aeropuerto de El Prat', 'Aeropuerto de Fiumicino', 'I-RIBN'),
(6, 2, 'Roma', 'Barcelona', 860, 115, 480000, 'Aeropuerto de Fiumicino', 'Aeropuerto de El Prat', 'I-RIBN'),
(7, 1, 'Londres', 'Dublín', 460, 80, 390000, 'Aeropuerto de Heathrow', 'Aeropuerto de Dublín', 'EI-DVM'),
(8, 2, 'Dublín', 'Londres', 460, 80, 390000, 'Aeropuerto de Dublín', 'Aeropuerto de Heathrow', 'EI-DVM'),
(9, 1, 'Ámsterdam', 'Copenhague', 620, 85, 450000, 'Aeropuerto de Schiphol', 'Aeropuerto de Kastrup', 'PH-BXA'),
(10, 2, 'Copenhague', 'Ámsterdam', 620, 85, 450000, 'Aeropuerto de Kastrup', 'Aeropuerto de Schiphol', 'PH-BXA'),
(11, 1, 'Fráncfort', 'Lisboa', 1890, 185, 650000, 'Aeropuerto de Fráncfort', 'Aeropuerto Humberto Delgado', 'D-AIZQ'),
(12, 2, 'Lisboa', 'Fráncfort', 1890, 185, 650000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Fráncfort', 'D-AIZQ'),
(13, 1, 'Estambul', 'Atenas', 560, 75, 420000, 'Aeropuerto de Estambul', 'Aeropuerto Eleftherios Venizelos', 'TC-JTA'),
(14, 2, 'Atenas', 'Estambul', 560, 75, 420000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Estambul', 'TC-JTA'),
(15, 1, 'París', 'Londres', 460, 80, 380000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Heathrow', 'F-HBNK'),
(16, 2, 'Londres', 'París', 460, 80, 380000, 'Aeropuerto de Heathrow', 'Aeropuerto Charles de Gaulle', 'F-HBNK'),
(17, 1, 'Madrid', 'Barcelona', 505, 80, 250000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de El Prat', 'EC-NVJ'),
(18, 2, 'Barcelona', 'Madrid', 505, 80, 250000, 'Aeropuerto de El Prat', 'Aeropuerto Adolfo Suárez Barajas', 'EC-NVJ'),
-- Trenes
(19, 1, 'Madrid', 'Barcelona', 621, 150, 290000, 'Estación Madrid Puerta de Atocha', 'Estación Barcelona Sants', 'AVE-103'),
(20, 2, 'Barcelona', 'Madrid', 621, 150, 290000, 'Estación Barcelona Sants', 'Estación Madrid Puerta de Atocha', 'AVE-103'),
(21, 1, 'París', 'Londres', 460, 135, 420000, 'Estación París Gare du Nord', 'Estación Londres St Pancras', 'EST-3201'),
(22, 2, 'Londres', 'París', 460, 135, 420000, 'Estación Londres St Pancras', 'Estación París Gare du Nord', 'EST-3201'),
(23, 1, 'París', 'Bruselas', 315, 82, 230000, 'Estación París Gare du Nord', 'Estación Bruselas-Midi', 'THA-4532'),
(24, 2, 'Bruselas', 'París', 315, 82, 230000, 'Estación Bruselas-Midi', 'Estación París Gare du Nord', 'THA-4532'),
(25, 1, 'Roma', 'Florencia', 275, 95, 170000, 'Estación Roma Termini', 'Estación Florencia Santa María Novella', 'FR-1000A'),
(26, 2, 'Florencia', 'Roma', 275, 95, 170000, 'Estación Florencia Santa María Novella', 'Estación Roma Termini', 'FR-1000A'),
(27, 1, 'Milán', 'Venecia', 270, 145, 150000, 'Estación Milano Centrale', 'Estación Venecia Santa Lucía', 'ITA-0912'),
(28, 2, 'Venecia', 'Milán', 270, 145, 150000, 'Estación Venecia Santa Lucía', 'Estación Milano Centrale', 'ITA-0912'),
(29, 1, 'Fráncfort', 'Múnich', 390, 190, 210000, 'Estación Frankfurt Hbf', 'Estación München Hbf', 'ICE-4711'),
(30, 2, 'Múnich', 'Fráncfort', 390, 190, 210000, 'Estación München Hbf', 'Estación Frankfurt Hbf', 'ICE-4711'),
(31, 1, 'Viena', 'Budapest', 245, 160, 120000, 'Estación Wien Hauptbahnhof', 'Estación Budapest-Keleti', 'RJ-0061'),
(32, 2, 'Budapest', 'Viena', 245, 160, 120000, 'Estación Budapest-Keleti', 'Estación Wien Hauptbahnhof', 'RJ-0061'),
-- Buses
(33, 1, 'Madrid', 'Lisboa', 625, 540, 140000, 'Estación Sur de Autobuses', 'Terminal Rodoviário Oriente', 'ALS-2210'),
(34, 2, 'Lisboa', 'Madrid', 625, 540, 140000, 'Terminal Rodoviário Oriente', 'Estación Sur de Autobuses', 'ALS-2210'),
(35, 1, 'Londres', 'Mánchester', 335, 270, 90000, 'Victoria Coach Station', 'Manchester Coach Station', 'NEX-0451'),
(36, 2, 'Mánchester', 'Londres', 335, 270, 90000, 'Manchester Coach Station', 'Victoria Coach Station', 'NEX-0451'),
(37, 1, 'Ámsterdam', 'Berlín', 655, 450, 150000, 'Terminal Amsterdam Sloterdijk', 'Terminal Berlin ZOB', 'FLX-7740'),
(38, 2, 'Berlín', 'Ámsterdam', 655, 450, 150000, 'Terminal Berlin ZOB', 'Terminal Amsterdam Sloterdijk', 'FLX-7740'),
(39, 1, 'Múnich', 'Viena', 435, 330, 110000, 'Terminal München ZOB', 'Terminal Wien Erdberg', 'FLX-7763'),
(40, 2, 'Viena', 'Múnich', 435, 330, 110000, 'Terminal Wien Erdberg', 'Terminal München ZOB', 'FLX-7763'),
(41, 1, 'París', 'Ámsterdam', 500, 480, 120000, 'Terminal París Bercy', 'Terminal Amsterdam Sloterdijk', 'FLX-7771'),
(42, 2, 'Ámsterdam', 'París', 500, 480, 120000, 'Terminal Amsterdam Sloterdijk', 'Terminal París Bercy', 'FLX-7771'),
(43, 1, 'Barcelona', 'Niza', 660, 600, 160000, 'Estación de Autobuses Barcelona Nord', 'Estación de autobuses de Niza', 'FLX-7731'),
(44, 2, 'Niza', 'Barcelona', 660, 600, 160000, 'Estación de autobuses de Niza', 'Estación de Autobuses Barcelona Nord', 'FLX-7731'),
-- Rutas agregadas para conectar ~80 % de los pares de ciudades
(45, 1, 'Atenas', 'Copenhague', 2137, 200, 714000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Kastrup', 'SX-TYZ'),
(46, 2, 'Copenhague', 'Atenas', 2137, 200, 714000, 'Aeropuerto de Kastrup', 'Aeropuerto Eleftherios Venizelos', 'SX-TYZ'),
(47, 1, 'Atenas', 'Edimburgo', 2823, 250, 886000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Edimburgo', 'SX-XAM'),
(48, 2, 'Edimburgo', 'Atenas', 2823, 250, 886000, 'Aeropuerto de Edimburgo', 'Aeropuerto Eleftherios Venizelos', 'SX-XAM'),
(49, 1, 'Atenas', 'Florencia', 1228, 130, 487000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Amerigo Vespucci', 'SX-CMK'),
(50, 2, 'Florencia', 'Atenas', 1228, 130, 487000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Eleftherios Venizelos', 'SX-CMK'),
(51, 1, 'Atenas', 'Helsinki', 2469, 225, 797000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Helsinki-Vantaa', 'SX-GUE'),
(52, 2, 'Helsinki', 'Atenas', 2469, 225, 797000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Eleftherios Venizelos', 'SX-GUE'),
(53, 1, 'Atenas', 'Mánchester', 2626, 235, 836000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Mánchester', 'SX-WWH'),
(54, 2, 'Mánchester', 'Atenas', 2626, 235, 836000, 'Aeropuerto de Mánchester', 'Aeropuerto Eleftherios Venizelos', 'SX-WWH'),
(55, 1, 'Atenas', 'Niza', 1520, 150, 560000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Costa Azul', 'SX-EDL'),
(56, 2, 'Niza', 'Atenas', 1520, 150, 560000, 'Aeropuerto Costa Azul', 'Aeropuerto Eleftherios Venizelos', 'SX-EDL'),
(57, 1, 'Atenas', 'Oslo', 2605, 235, 831000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Gardermoen', 'SX-SDC'),
(58, 2, 'Oslo', 'Atenas', 2605, 235, 831000, 'Aeropuerto de Gardermoen', 'Aeropuerto Eleftherios Venizelos', 'SX-SDC'),
(59, 1, 'Atenas', 'Praga', 1534, 155, 564000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Václav Havel', 'SX-NJW'),
(60, 2, 'Praga', 'Atenas', 1534, 155, 564000, 'Aeropuerto Václav Havel', 'Aeropuerto Eleftherios Venizelos', 'SX-NJW'),
(61, 1, 'Atenas', 'Varsovia', 1599, 160, 580000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Chopin de Varsovia', 'SX-KXD'),
(62, 2, 'Varsovia', 'Atenas', 1599, 160, 580000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto Eleftherios Venizelos', 'SX-KXD'),
(63, 1, 'Atenas', 'Venecia', 1257, 130, 494000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Marco Polo', 'SX-MSS'),
(64, 2, 'Venecia', 'Atenas', 1257, 130, 494000, 'Aeropuerto Marco Polo', 'Aeropuerto Eleftherios Venizelos', 'SX-MSS'),
(65, 1, 'Barcelona', 'Atenas', 1878, 180, 650000, 'Aeropuerto de El Prat', 'Aeropuerto Eleftherios Venizelos', 'EC-DUT'),
(66, 2, 'Atenas', 'Barcelona', 1878, 180, 650000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de El Prat', 'EC-DUT'),
(67, 1, 'Barcelona', 'Berlín', 1499, 150, 555000, 'Aeropuerto de El Prat', 'Aeropuerto de Berlín-Brandeburgo', 'EC-HMY'),
(68, 2, 'Berlín', 'Barcelona', 1499, 150, 555000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de El Prat', 'EC-HMY'),
(69, 1, 'Barcelona', 'Bruselas', 1065, 115, 446000, 'Aeropuerto de El Prat', 'Aeropuerto de Bruselas-Zaventem', 'EC-RVM'),
(70, 2, 'Bruselas', 'Barcelona', 1065, 115, 446000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de El Prat', 'EC-RVM'),
(71, 1, 'Barcelona', 'Budapest', 1497, 150, 554000, 'Aeropuerto de El Prat', 'Aeropuerto Ferenc Liszt', 'EC-CCC'),
(72, 2, 'Budapest', 'Barcelona', 1497, 150, 554000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de El Prat', 'EC-CCC'),
(73, 1, 'Barcelona', 'Copenhague', 1759, 170, 620000, 'Aeropuerto de El Prat', 'Aeropuerto de Kastrup', 'EC-SKJ'),
(74, 2, 'Copenhague', 'Barcelona', 1759, 170, 620000, 'Aeropuerto de Kastrup', 'Aeropuerto de El Prat', 'EC-SKJ'),
(75, 1, 'Barcelona', 'Dublín', 1471, 150, 548000, 'Aeropuerto de El Prat', 'Aeropuerto de Dublín', 'EC-YHF'),
(76, 2, 'Dublín', 'Barcelona', 1471, 150, 548000, 'Aeropuerto de Dublín', 'Aeropuerto de El Prat', 'EC-YHF'),
(77, 1, 'Barcelona', 'Edimburgo', 1665, 165, 596000, 'Aeropuerto de El Prat', 'Aeropuerto de Edimburgo', 'EC-LPT'),
(78, 2, 'Edimburgo', 'Barcelona', 1665, 165, 596000, 'Aeropuerto de Edimburgo', 'Aeropuerto de El Prat', 'EC-LPT'),
(79, 1, 'Barcelona', 'Estocolmo', 2278, 210, 750000, 'Aeropuerto de El Prat', 'Aeropuerto de Arlanda', 'EC-ULL'),
(80, 2, 'Estocolmo', 'Barcelona', 2278, 210, 750000, 'Aeropuerto de Arlanda', 'Aeropuerto de El Prat', 'EC-ULL'),
(81, 1, 'Barcelona', 'Florencia', 789, 95, 377000, 'Aeropuerto de El Prat', 'Aeropuerto Amerigo Vespucci', 'EC-YSN'),
(82, 2, 'Florencia', 'Barcelona', 789, 95, 377000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de El Prat', 'EC-YSN'),
(83, 1, 'Barcelona', 'Fráncfort', 1092, 120, 453000, 'Aeropuerto de El Prat', 'Aeropuerto de Fráncfort', 'EC-LFG'),
(84, 2, 'Fráncfort', 'Barcelona', 1092, 120, 453000, 'Aeropuerto de Fráncfort', 'Aeropuerto de El Prat', 'EC-LFG'),
(85, 1, 'Barcelona', 'Helsinki', 2603, 235, 831000, 'Aeropuerto de El Prat', 'Aeropuerto de Helsinki-Vantaa', 'EC-RHZ'),
(86, 2, 'Helsinki', 'Barcelona', 2603, 235, 831000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de El Prat', 'EC-RHZ'),
(87, 1, 'Barcelona', 'Lisboa', 1006, 110, 432000, 'Aeropuerto de El Prat', 'Aeropuerto Humberto Delgado', 'EC-MMP'),
(88, 2, 'Lisboa', 'Barcelona', 1006, 110, 432000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de El Prat', 'EC-MMP'),
(89, 1, 'Barcelona', 'Londres', 1139, 125, 465000, 'Aeropuerto de El Prat', 'Aeropuerto de Heathrow', 'EC-VNG'),
(90, 2, 'Londres', 'Barcelona', 1139, 125, 465000, 'Aeropuerto de Heathrow', 'Aeropuerto de El Prat', 'EC-VNG'),
(91, 1, 'Barcelona', 'Milán', 725, 90, 361000, 'Aeropuerto de El Prat', 'Aeropuerto de Malpensa', 'EC-ERV'),
(92, 2, 'Milán', 'Barcelona', 725, 90, 361000, 'Aeropuerto de Malpensa', 'Aeropuerto de El Prat', 'EC-ERV'),
(93, 1, 'Barcelona', 'Mánchester', 1384, 140, 526000, 'Aeropuerto de El Prat', 'Aeropuerto de Mánchester', 'EC-YEP'),
(94, 2, 'Mánchester', 'Barcelona', 1384, 140, 526000, 'Aeropuerto de Mánchester', 'Aeropuerto de El Prat', 'EC-YEP'),
(95, 1, 'Barcelona', 'Oporto', 901, 105, 405000, 'Aeropuerto de El Prat', 'Aeropuerto Francisco Sá Carneiro', 'EC-JLD'),
(96, 2, 'Oporto', 'Barcelona', 901, 105, 405000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de El Prat', 'EC-JLD'),
(97, 1, 'Barcelona', 'París', 831, 100, 388000, 'Aeropuerto de El Prat', 'Aeropuerto Charles de Gaulle', 'EC-LJR'),
(98, 2, 'París', 'Barcelona', 831, 100, 388000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de El Prat', 'EC-LJR'),
(99, 1, 'Barcelona', 'Varsovia', 1863, 180, 646000, 'Aeropuerto de El Prat', 'Aeropuerto Chopin de Varsovia', 'EC-JEU'),
(100, 2, 'Varsovia', 'Barcelona', 1863, 180, 646000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de El Prat', 'EC-JEU'),
(101, 1, 'Barcelona', 'Venecia', 934, 105, 414000, 'Aeropuerto de El Prat', 'Aeropuerto Marco Polo', 'EC-XJH'),
(102, 2, 'Venecia', 'Barcelona', 934, 105, 414000, 'Aeropuerto Marco Polo', 'Aeropuerto de El Prat', 'EC-XJH'),
(103, 1, 'Barcelona', 'Viena', 1350, 140, 517000, 'Aeropuerto de El Prat', 'Aeropuerto de Viena-Schwechat', 'EC-LEU'),
(104, 2, 'Viena', 'Barcelona', 1350, 140, 517000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de El Prat', 'EC-LEU'),
(105, 1, 'Barcelona', 'Zúrich', 836, 100, 389000, 'Aeropuerto de El Prat', 'Aeropuerto de Zúrich', 'EC-DJK'),
(106, 2, 'Zúrich', 'Barcelona', 836, 100, 389000, 'Aeropuerto de Zúrich', 'Aeropuerto de El Prat', 'EC-DJK'),
(107, 1, 'Barcelona', 'Ámsterdam', 1238, 130, 490000, 'Aeropuerto de El Prat', 'Aeropuerto de Schiphol', 'EC-VRX'),
(108, 2, 'Ámsterdam', 'Barcelona', 1238, 130, 490000, 'Aeropuerto de Schiphol', 'Aeropuerto de El Prat', 'EC-VRX'),
(109, 1, 'Berlín', 'Budapest', 688, 90, 352000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Ferenc Liszt', 'D-NNAU'),
(110, 2, 'Budapest', 'Berlín', 688, 90, 352000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Berlín-Brandeburgo', 'D-NNAU'),
(111, 1, 'Berlín', 'Edimburgo', 1140, 125, 465000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Edimburgo', 'D-ZLYP'),
(112, 2, 'Edimburgo', 'Berlín', 1140, 125, 465000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Berlín-Brandeburgo', 'D-ZLYP'),
(113, 1, 'Berlín', 'Estambul', 1738, 170, 615000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Estambul', 'D-LDYS'),
(114, 2, 'Estambul', 'Berlín', 1738, 170, 615000, 'Aeropuerto de Estambul', 'Aeropuerto de Berlín-Brandeburgo', 'D-LDYS'),
(115, 1, 'Berlín', 'Estocolmo', 811, 95, 383000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Arlanda', 'D-YXYU'),
(116, 2, 'Estocolmo', 'Berlín', 811, 95, 383000, 'Aeropuerto de Arlanda', 'Aeropuerto de Berlín-Brandeburgo', 'D-YXYU'),
(117, 1, 'Berlín', 'Florencia', 986, 110, 426000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Amerigo Vespucci', 'D-TGPM'),
(118, 2, 'Florencia', 'Berlín', 986, 110, 426000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Berlín-Brandeburgo', 'D-TGPM'),
(119, 1, 'Berlín', 'Ginebra', 877, 100, 399000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Ginebra', 'D-XLBN'),
(120, 2, 'Ginebra', 'Berlín', 877, 100, 399000, 'Aeropuerto de Ginebra', 'Aeropuerto de Berlín-Brandeburgo', 'D-XLBN'),
(121, 1, 'Berlín', 'Niza', 1080, 120, 450000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Costa Azul', 'D-FAJU'),
(122, 2, 'Niza', 'Berlín', 1080, 120, 450000, 'Aeropuerto Costa Azul', 'Aeropuerto de Berlín-Brandeburgo', 'D-FAJU'),
(123, 1, 'Berlín', 'Oporto', 2084, 195, 701000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Francisco Sá Carneiro', 'D-ZJEL'),
(124, 2, 'Oporto', 'Berlín', 2084, 195, 701000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Berlín-Brandeburgo', 'D-ZJEL'),
(125, 1, 'Berlín', 'Oslo', 838, 100, 389000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Gardermoen', 'D-VXWP'),
(126, 2, 'Oslo', 'Berlín', 838, 100, 389000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Berlín-Brandeburgo', 'D-VXWP'),
(127, 1, 'Berlín', 'Reikiavik', 2388, 220, 777000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Keflavík', 'D-SSLA'),
(128, 2, 'Reikiavik', 'Berlín', 2388, 220, 777000, 'Aeropuerto de Keflavík', 'Aeropuerto de Berlín-Brandeburgo', 'D-SSLA'),
(129, 1, 'Berlín', 'Venecia', 791, 95, 378000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Marco Polo', 'D-BHXD'),
(130, 2, 'Venecia', 'Berlín', 791, 95, 378000, 'Aeropuerto Marco Polo', 'Aeropuerto de Berlín-Brandeburgo', 'D-BHXD'),
(131, 1, 'Bruselas', 'Atenas', 2090, 195, 702000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Eleftherios Venizelos', 'OO-YJL'),
(132, 2, 'Atenas', 'Bruselas', 2090, 195, 702000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Bruselas-Zaventem', 'OO-YJL'),
(133, 1, 'Bruselas', 'Budapest', 1129, 120, 462000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Ferenc Liszt', 'OO-AGR'),
(134, 2, 'Budapest', 'Bruselas', 1129, 120, 462000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Bruselas-Zaventem', 'OO-AGR'),
(135, 1, 'Bruselas', 'Copenhague', 765, 95, 371000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Kastrup', 'OO-TJW'),
(136, 2, 'Copenhague', 'Bruselas', 765, 95, 371000, 'Aeropuerto de Kastrup', 'Aeropuerto de Bruselas-Zaventem', 'OO-TJW'),
(137, 1, 'Bruselas', 'Dublín', 775, 95, 374000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Dublín', 'OO-XMC'),
(138, 2, 'Dublín', 'Bruselas', 775, 95, 374000, 'Aeropuerto de Dublín', 'Aeropuerto de Bruselas-Zaventem', 'OO-XMC'),
(139, 1, 'Bruselas', 'Edimburgo', 755, 95, 369000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Edimburgo', 'OO-UZG'),
(140, 2, 'Edimburgo', 'Bruselas', 755, 95, 369000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Bruselas-Zaventem', 'OO-UZG'),
(141, 1, 'Bruselas', 'Estambul', 2182, 205, 725000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Estambul', 'OO-UAD'),
(142, 2, 'Estambul', 'Bruselas', 2182, 205, 725000, 'Aeropuerto de Estambul', 'Aeropuerto de Bruselas-Zaventem', 'OO-UAD'),
(143, 1, 'Bruselas', 'Estocolmo', 1280, 135, 500000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Arlanda', 'OO-ELH'),
(144, 2, 'Estocolmo', 'Bruselas', 1280, 135, 500000, 'Aeropuerto de Arlanda', 'Aeropuerto de Bruselas-Zaventem', 'OO-ELH'),
(145, 1, 'Bruselas', 'Florencia', 943, 110, 416000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Amerigo Vespucci', 'OO-RGW'),
(146, 2, 'Florencia', 'Bruselas', 943, 110, 416000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Bruselas-Zaventem', 'OO-RGW'),
(147, 1, 'Bruselas', 'Helsinki', 1648, 160, 592000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Helsinki-Vantaa', 'OO-YFZ'),
(148, 2, 'Helsinki', 'Bruselas', 1648, 160, 592000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Bruselas-Zaventem', 'OO-YFZ'),
(149, 1, 'Bruselas', 'Mánchester', 536, 75, 314000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Mánchester', 'OO-KCV'),
(150, 2, 'Mánchester', 'Bruselas', 536, 75, 314000, 'Aeropuerto de Mánchester', 'Aeropuerto de Bruselas-Zaventem', 'OO-KCV'),
(151, 1, 'Bruselas', 'Niza', 825, 100, 386000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Costa Azul', 'OO-PZT'),
(152, 2, 'Niza', 'Bruselas', 825, 100, 386000, 'Aeropuerto Costa Azul', 'Aeropuerto de Bruselas-Zaventem', 'OO-PZT'),
(153, 1, 'Bruselas', 'Oslo', 1084, 120, 451000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Gardermoen', 'OO-YVU'),
(154, 2, 'Oslo', 'Bruselas', 1084, 120, 451000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Bruselas-Zaventem', 'OO-YVU'),
(155, 1, 'Bruselas', 'Praga', 719, 90, 360000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Václav Havel', 'OO-EDA'),
(156, 2, 'Praga', 'Bruselas', 719, 90, 360000, 'Aeropuerto Václav Havel', 'Aeropuerto de Bruselas-Zaventem', 'OO-EDA'),
(157, 1, 'Bruselas', 'Reikiavik', 2129, 200, 712000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Keflavík', 'OO-JPB'),
(158, 2, 'Reikiavik', 'Bruselas', 2129, 200, 712000, 'Aeropuerto de Keflavík', 'Aeropuerto de Bruselas-Zaventem', 'OO-JPB'),
(159, 1, 'Bruselas', 'Varsovia', 1160, 125, 470000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Chopin de Varsovia', 'OO-FUT'),
(160, 2, 'Varsovia', 'Bruselas', 1160, 125, 470000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Bruselas-Zaventem', 'OO-FUT'),
(161, 1, 'Bruselas', 'Viena', 915, 105, 409000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Viena-Schwechat', 'OO-HSY'),
(162, 2, 'Viena', 'Bruselas', 915, 105, 409000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Bruselas-Zaventem', 'OO-HSY'),
(163, 1, 'Bruselas', 'Zúrich', 492, 75, 303000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Zúrich', 'OO-YLL'),
(164, 2, 'Zúrich', 'Bruselas', 492, 75, 303000, 'Aeropuerto de Zúrich', 'Aeropuerto de Bruselas-Zaventem', 'OO-YLL'),
(165, 1, 'Budapest', 'Copenhague', 1012, 115, 433000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Kastrup', 'HA-MLW'),
(166, 2, 'Copenhague', 'Budapest', 1012, 115, 433000, 'Aeropuerto de Kastrup', 'Aeropuerto Ferenc Liszt', 'HA-MLW'),
(167, 1, 'Budapest', 'Estambul', 1070, 115, 447000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Estambul', 'HA-SJS'),
(168, 2, 'Estambul', 'Budapest', 1070, 115, 447000, 'Aeropuerto de Estambul', 'Aeropuerto Ferenc Liszt', 'HA-SJS'),
(169, 1, 'Budapest', 'Florencia', 733, 90, 363000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Amerigo Vespucci', 'HA-LJB'),
(170, 2, 'Florencia', 'Budapest', 733, 90, 363000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Ferenc Liszt', 'HA-LJB'),
(171, 1, 'Budapest', 'Helsinki', 1460, 145, 545000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Helsinki-Vantaa', 'HA-XSE'),
(172, 2, 'Helsinki', 'Budapest', 1460, 145, 545000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Ferenc Liszt', 'HA-XSE'),
(173, 1, 'Budapest', 'Mánchester', 1638, 160, 589000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Mánchester', 'HA-ASM'),
(174, 2, 'Mánchester', 'Budapest', 1638, 160, 589000, 'Aeropuerto de Mánchester', 'Aeropuerto Ferenc Liszt', 'HA-ASM'),
(175, 1, 'Budapest', 'Niza', 1007, 110, 432000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Costa Azul', 'HA-JRG'),
(176, 2, 'Niza', 'Budapest', 1007, 110, 432000, 'Aeropuerto Costa Azul', 'Aeropuerto Ferenc Liszt', 'HA-JRG'),
(177, 1, 'Budapest', 'Oslo', 1481, 150, 550000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Gardermoen', 'HA-MJT'),
(178, 2, 'Oslo', 'Budapest', 1481, 150, 550000, 'Aeropuerto de Gardermoen', 'Aeropuerto Ferenc Liszt', 'HA-MJT'),
(179, 1, 'Budapest', 'Venecia', 563, 80, 321000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Marco Polo', 'HA-NKG'),
(180, 2, 'Venecia', 'Budapest', 563, 80, 321000, 'Aeropuerto Marco Polo', 'Aeropuerto Ferenc Liszt', 'HA-NKG'),
(181, 1, 'Copenhague', 'Edimburgo', 983, 110, 426000, 'Aeropuerto de Kastrup', 'Aeropuerto de Edimburgo', 'OY-VBP'),
(182, 2, 'Edimburgo', 'Copenhague', 983, 110, 426000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Kastrup', 'OY-VBP'),
(183, 1, 'Copenhague', 'Estambul', 2022, 190, 685000, 'Aeropuerto de Kastrup', 'Aeropuerto de Estambul', 'OY-CXY'),
(184, 2, 'Estambul', 'Copenhague', 2022, 190, 685000, 'Aeropuerto de Estambul', 'Aeropuerto de Kastrup', 'OY-CXY'),
(185, 1, 'Copenhague', 'Florencia', 1328, 135, 512000, 'Aeropuerto de Kastrup', 'Aeropuerto Amerigo Vespucci', 'OY-TYU'),
(186, 2, 'Florencia', 'Copenhague', 1328, 135, 512000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Kastrup', 'OY-TYU'),
(187, 1, 'Copenhague', 'Helsinki', 883, 105, 401000, 'Aeropuerto de Kastrup', 'Aeropuerto de Helsinki-Vantaa', 'OY-KSW'),
(188, 2, 'Helsinki', 'Copenhague', 883, 105, 401000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Kastrup', 'OY-KSW'),
(189, 1, 'Copenhague', 'Niza', 1385, 140, 526000, 'Aeropuerto de Kastrup', 'Aeropuerto Costa Azul', 'OY-CTT'),
(190, 2, 'Niza', 'Copenhague', 1385, 140, 526000, 'Aeropuerto Costa Azul', 'Aeropuerto de Kastrup', 'OY-CTT'),
(191, 1, 'Copenhague', 'Reikiavik', 2107, 195, 707000, 'Aeropuerto de Kastrup', 'Aeropuerto de Keflavík', 'OY-WLS'),
(192, 2, 'Reikiavik', 'Copenhague', 2107, 195, 707000, 'Aeropuerto de Keflavík', 'Aeropuerto de Kastrup', 'OY-WLS'),
(193, 1, 'Copenhague', 'Venecia', 1139, 125, 465000, 'Aeropuerto de Kastrup', 'Aeropuerto Marco Polo', 'OY-LYL'),
(194, 2, 'Venecia', 'Copenhague', 1139, 125, 465000, 'Aeropuerto Marco Polo', 'Aeropuerto de Kastrup', 'OY-LYL'),
(195, 1, 'Dublín', 'Copenhague', 1239, 130, 490000, 'Aeropuerto de Dublín', 'Aeropuerto de Kastrup', 'EI-MFC'),
(196, 2, 'Copenhague', 'Dublín', 1239, 130, 490000, 'Aeropuerto de Kastrup', 'Aeropuerto de Dublín', 'EI-MFC'),
(197, 1, 'Dublín', 'Edimburgo', 350, 60, 268000, 'Aeropuerto de Dublín', 'Aeropuerto de Edimburgo', 'EI-UFJ'),
(198, 2, 'Edimburgo', 'Dublín', 350, 60, 268000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Dublín', 'EI-UFJ'),
(199, 1, 'Dublín', 'Estocolmo', 1628, 160, 587000, 'Aeropuerto de Dublín', 'Aeropuerto de Arlanda', 'EI-TTN'),
(200, 2, 'Estocolmo', 'Dublín', 1628, 160, 587000, 'Aeropuerto de Arlanda', 'Aeropuerto de Dublín', 'EI-TTN'),
(201, 1, 'Dublín', 'Florencia', 1665, 165, 596000, 'Aeropuerto de Dublín', 'Aeropuerto Amerigo Vespucci', 'EI-XDW'),
(202, 2, 'Florencia', 'Dublín', 1665, 165, 596000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Dublín', 'EI-XDW'),
(203, 1, 'Dublín', 'Helsinki', 2024, 190, 686000, 'Aeropuerto de Dublín', 'Aeropuerto de Helsinki-Vantaa', 'EI-GAR'),
(204, 2, 'Helsinki', 'Dublín', 2024, 190, 686000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Dublín', 'EI-GAR'),
(205, 1, 'Dublín', 'Mánchester', 267, 55, 247000, 'Aeropuerto de Dublín', 'Aeropuerto de Mánchester', 'EI-HGP'),
(206, 2, 'Mánchester', 'Dublín', 267, 55, 247000, 'Aeropuerto de Mánchester', 'Aeropuerto de Dublín', 'EI-HGP'),
(207, 1, 'Dublín', 'Niza', 1460, 145, 545000, 'Aeropuerto de Dublín', 'Aeropuerto Costa Azul', 'EI-TTE'),
(208, 2, 'Niza', 'Dublín', 1460, 145, 545000, 'Aeropuerto Costa Azul', 'Aeropuerto de Dublín', 'EI-TTE'),
(209, 1, 'Dublín', 'Oslo', 1265, 130, 496000, 'Aeropuerto de Dublín', 'Aeropuerto de Gardermoen', 'EI-TGJ'),
(210, 2, 'Oslo', 'Dublín', 1265, 130, 496000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Dublín', 'EI-TGJ'),
(211, 1, 'Dublín', 'Praga', 1466, 150, 546000, 'Aeropuerto de Dublín', 'Aeropuerto Václav Havel', 'EI-ZSE'),
(212, 2, 'Praga', 'Dublín', 1466, 150, 546000, 'Aeropuerto Václav Havel', 'Aeropuerto de Dublín', 'EI-ZSE'),
(213, 1, 'Dublín', 'Reikiavik', 1495, 150, 554000, 'Aeropuerto de Dublín', 'Aeropuerto de Keflavík', 'EI-UYL'),
(214, 2, 'Reikiavik', 'Dublín', 1495, 150, 554000, 'Aeropuerto de Keflavík', 'Aeropuerto de Dublín', 'EI-UYL'),
(215, 1, 'Dublín', 'Varsovia', 1827, 175, 637000, 'Aeropuerto de Dublín', 'Aeropuerto Chopin de Varsovia', 'EI-MXN'),
(216, 2, 'Varsovia', 'Dublín', 1827, 175, 637000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Dublín', 'EI-MXN'),
(217, 1, 'Dublín', 'Venecia', 1599, 160, 580000, 'Aeropuerto de Dublín', 'Aeropuerto Marco Polo', 'EI-UWU'),
(218, 2, 'Venecia', 'Dublín', 1599, 160, 580000, 'Aeropuerto Marco Polo', 'Aeropuerto de Dublín', 'EI-UWU'),
(219, 1, 'Estambul', 'Edimburgo', 2854, 255, 894000, 'Aeropuerto de Estambul', 'Aeropuerto de Edimburgo', 'TC-GEW'),
(220, 2, 'Edimburgo', 'Estambul', 2854, 255, 894000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Estambul', 'TC-GEW'),
(221, 1, 'Estambul', 'Florencia', 1484, 150, 551000, 'Aeropuerto de Estambul', 'Aeropuerto Amerigo Vespucci', 'TC-UZE'),
(222, 2, 'Florencia', 'Estambul', 1484, 150, 551000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Estambul', 'TC-UZE'),
(223, 1, 'Estambul', 'Niza', 1803, 175, 631000, 'Aeropuerto de Estambul', 'Aeropuerto Costa Azul', 'TC-EEY'),
(224, 2, 'Niza', 'Estambul', 1803, 175, 631000, 'Aeropuerto Costa Azul', 'Aeropuerto de Estambul', 'TC-EEY'),
(225, 1, 'Estambul', 'Venecia', 1434, 145, 538000, 'Aeropuerto de Estambul', 'Aeropuerto Marco Polo', 'TC-VBY'),
(226, 2, 'Venecia', 'Estambul', 1434, 145, 538000, 'Aeropuerto Marco Polo', 'Aeropuerto de Estambul', 'TC-VBY'),
(227, 1, 'Estocolmo', 'Edimburgo', 1313, 135, 508000, 'Aeropuerto de Arlanda', 'Aeropuerto de Edimburgo', 'SE-BJK'),
(228, 2, 'Edimburgo', 'Estocolmo', 1313, 135, 508000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Arlanda', 'SE-BJK'),
(229, 1, 'Estocolmo', 'Estambul', 2174, 200, 723000, 'Aeropuerto de Arlanda', 'Aeropuerto de Estambul', 'SE-FEJ'),
(230, 2, 'Estambul', 'Estocolmo', 2174, 200, 723000, 'Aeropuerto de Estambul', 'Aeropuerto de Arlanda', 'SE-FEJ'),
(231, 1, 'Estocolmo', 'Florencia', 1791, 175, 628000, 'Aeropuerto de Arlanda', 'Aeropuerto Amerigo Vespucci', 'SE-UZM'),
(232, 2, 'Florencia', 'Estocolmo', 1791, 175, 628000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Arlanda', 'SE-UZM'),
(233, 1, 'Estocolmo', 'Mánchester', 1401, 145, 530000, 'Aeropuerto de Arlanda', 'Aeropuerto de Mánchester', 'SE-ERF'),
(234, 2, 'Mánchester', 'Estocolmo', 1401, 145, 530000, 'Aeropuerto de Mánchester', 'Aeropuerto de Arlanda', 'SE-ERF'),
(235, 1, 'Estocolmo', 'Niza', 1886, 180, 652000, 'Aeropuerto de Arlanda', 'Aeropuerto Costa Azul', 'SE-FZB'),
(236, 2, 'Niza', 'Estocolmo', 1886, 180, 652000, 'Aeropuerto Costa Azul', 'Aeropuerto de Arlanda', 'SE-FZB'),
(237, 1, 'Estocolmo', 'Reikiavik', 2134, 200, 714000, 'Aeropuerto de Arlanda', 'Aeropuerto de Keflavík', 'SE-FYE'),
(238, 2, 'Reikiavik', 'Estocolmo', 2134, 200, 714000, 'Aeropuerto de Keflavík', 'Aeropuerto de Arlanda', 'SE-FYE'),
(239, 1, 'Estocolmo', 'Venecia', 1592, 155, 578000, 'Aeropuerto de Arlanda', 'Aeropuerto Marco Polo', 'SE-PXW'),
(240, 2, 'Venecia', 'Estocolmo', 1592, 155, 578000, 'Aeropuerto Marco Polo', 'Aeropuerto de Arlanda', 'SE-PXW'),
(241, 1, 'Florencia', 'Edimburgo', 1698, 165, 604000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Edimburgo', 'I-SUAK'),
(242, 2, 'Edimburgo', 'Florencia', 1698, 165, 604000, 'Aeropuerto de Edimburgo', 'Aeropuerto Amerigo Vespucci', 'I-SUAK'),
(243, 1, 'Florencia', 'Mánchester', 1462, 145, 545000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Mánchester', 'I-HTMU'),
(244, 2, 'Mánchester', 'Florencia', 1462, 145, 545000, 'Aeropuerto de Mánchester', 'Aeropuerto Amerigo Vespucci', 'I-HTMU'),
(245, 1, 'Fráncfort', 'Atenas', 1800, 175, 630000, 'Aeropuerto de Fráncfort', 'Aeropuerto Eleftherios Venizelos', 'D-CRYP'),
(246, 2, 'Atenas', 'Fráncfort', 1800, 175, 630000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Fráncfort', 'D-CRYP'),
(247, 1, 'Fráncfort', 'Dublín', 1088, 120, 452000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Dublín', 'D-ELEZ'),
(248, 2, 'Dublín', 'Fráncfort', 1088, 120, 452000, 'Aeropuerto de Dublín', 'Aeropuerto de Fráncfort', 'D-ELEZ'),
(249, 1, 'Fráncfort', 'Edimburgo', 1023, 115, 436000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Edimburgo', 'D-TBSA'),
(250, 2, 'Edimburgo', 'Fráncfort', 1023, 115, 436000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Fráncfort', 'D-TBSA'),
(251, 1, 'Fráncfort', 'Estambul', 1867, 180, 647000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Estambul', 'D-BDMV'),
(252, 2, 'Estambul', 'Fráncfort', 1867, 180, 647000, 'Aeropuerto de Estambul', 'Aeropuerto de Fráncfort', 'D-BDMV'),
(253, 1, 'Fráncfort', 'Estocolmo', 1187, 125, 477000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Arlanda', 'D-ZZJL'),
(254, 2, 'Estocolmo', 'Fráncfort', 1187, 125, 477000, 'Aeropuerto de Arlanda', 'Aeropuerto de Fráncfort', 'D-ZZJL'),
(255, 1, 'Fráncfort', 'Florencia', 732, 90, 363000, 'Aeropuerto de Fráncfort', 'Aeropuerto Amerigo Vespucci', 'D-AFTG'),
(256, 2, 'Florencia', 'Fráncfort', 732, 90, 363000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Fráncfort', 'D-AFTG'),
(257, 1, 'Fráncfort', 'Helsinki', 1515, 150, 559000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Helsinki-Vantaa', 'D-CPYP'),
(258, 2, 'Helsinki', 'Fráncfort', 1515, 150, 559000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Fráncfort', 'D-CPYP'),
(259, 1, 'Fráncfort', 'Mánchester', 838, 100, 390000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Mánchester', 'D-GFHH'),
(260, 2, 'Mánchester', 'Fráncfort', 838, 100, 390000, 'Aeropuerto de Mánchester', 'Aeropuerto de Fráncfort', 'D-GFHH'),
(261, 1, 'Fráncfort', 'Niza', 721, 90, 360000, 'Aeropuerto de Fráncfort', 'Aeropuerto Costa Azul', 'D-YSGM'),
(262, 2, 'Niza', 'Fráncfort', 721, 90, 360000, 'Aeropuerto Costa Azul', 'Aeropuerto de Fráncfort', 'D-YSGM'),
(263, 1, 'Fráncfort', 'Oporto', 1667, 165, 597000, 'Aeropuerto de Fráncfort', 'Aeropuerto Francisco Sá Carneiro', 'D-FUPP'),
(264, 2, 'Oporto', 'Fráncfort', 1667, 165, 597000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Fráncfort', 'D-FUPP'),
(265, 1, 'Fráncfort', 'Oslo', 1098, 120, 454000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Gardermoen', 'D-RMZG'),
(266, 2, 'Oslo', 'Fráncfort', 1098, 120, 454000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Fráncfort', 'D-RMZG'),
(267, 1, 'Fráncfort', 'Reikiavik', 2378, 220, 775000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Keflavík', 'D-KXRU'),
(268, 2, 'Reikiavik', 'Fráncfort', 2378, 220, 775000, 'Aeropuerto de Keflavík', 'Aeropuerto de Fráncfort', 'D-KXRU'),
(269, 1, 'Fráncfort', 'Varsovia', 890, 105, 403000, 'Aeropuerto de Fráncfort', 'Aeropuerto Chopin de Varsovia', 'D-WSCG'),
(270, 2, 'Varsovia', 'Fráncfort', 890, 105, 403000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Fráncfort', 'D-WSCG'),
(271, 1, 'Fráncfort', 'Viena', 597, 80, 329000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Viena-Schwechat', 'D-GDCM'),
(272, 2, 'Viena', 'Fráncfort', 597, 80, 329000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Fráncfort', 'D-GDCM'),
(273, 1, 'Ginebra', 'Atenas', 1709, 165, 607000, 'Aeropuerto de Ginebra', 'Aeropuerto Eleftherios Venizelos', 'HB-ZYW'),
(274, 2, 'Atenas', 'Ginebra', 1709, 165, 607000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Ginebra', 'HB-ZYW'),
(275, 1, 'Ginebra', 'Copenhague', 1145, 125, 466000, 'Aeropuerto de Ginebra', 'Aeropuerto de Kastrup', 'HB-ZFD'),
(276, 2, 'Copenhague', 'Ginebra', 1145, 125, 466000, 'Aeropuerto de Kastrup', 'Aeropuerto de Ginebra', 'HB-ZFD'),
(277, 1, 'Ginebra', 'Dublín', 1191, 125, 478000, 'Aeropuerto de Ginebra', 'Aeropuerto de Dublín', 'HB-FGX'),
(278, 2, 'Dublín', 'Ginebra', 1191, 125, 478000, 'Aeropuerto de Dublín', 'Aeropuerto de Ginebra', 'HB-FGX'),
(279, 1, 'Ginebra', 'Edimburgo', 1263, 130, 496000, 'Aeropuerto de Ginebra', 'Aeropuerto de Edimburgo', 'HB-PGP'),
(280, 2, 'Edimburgo', 'Ginebra', 1263, 130, 496000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Ginebra', 'HB-PGP'),
(281, 1, 'Ginebra', 'Estambul', 1920, 185, 660000, 'Aeropuerto de Ginebra', 'Aeropuerto de Estambul', 'HB-AUE'),
(282, 2, 'Estambul', 'Ginebra', 1920, 185, 660000, 'Aeropuerto de Estambul', 'Aeropuerto de Ginebra', 'HB-AUE'),
(283, 1, 'Ginebra', 'Florencia', 485, 70, 301000, 'Aeropuerto de Ginebra', 'Aeropuerto Amerigo Vespucci', 'HB-XXJ'),
(284, 2, 'Florencia', 'Ginebra', 485, 70, 301000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Ginebra', 'HB-XXJ'),
(285, 1, 'Ginebra', 'Helsinki', 1981, 185, 675000, 'Aeropuerto de Ginebra', 'Aeropuerto de Helsinki-Vantaa', 'HB-KMN'),
(286, 2, 'Helsinki', 'Ginebra', 1981, 185, 675000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Ginebra', 'HB-KMN'),
(287, 1, 'Ginebra', 'Mánchester', 1007, 110, 432000, 'Aeropuerto de Ginebra', 'Aeropuerto de Mánchester', 'HB-AST'),
(288, 2, 'Mánchester', 'Ginebra', 1007, 110, 432000, 'Aeropuerto de Mánchester', 'Aeropuerto de Ginebra', 'HB-AST'),
(289, 1, 'Ginebra', 'Oslo', 1554, 155, 569000, 'Aeropuerto de Ginebra', 'Aeropuerto de Gardermoen', 'HB-RJM'),
(290, 2, 'Oslo', 'Ginebra', 1554, 155, 569000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Ginebra', 'HB-RJM'),
(291, 1, 'Ginebra', 'Praga', 751, 95, 368000, 'Aeropuerto de Ginebra', 'Aeropuerto Václav Havel', 'HB-SED'),
(292, 2, 'Praga', 'Ginebra', 751, 95, 368000, 'Aeropuerto Václav Havel', 'Aeropuerto de Ginebra', 'HB-SED'),
(293, 1, 'Ginebra', 'Reikiavik', 2633, 240, 838000, 'Aeropuerto de Ginebra', 'Aeropuerto de Keflavík', 'HB-EUH'),
(294, 2, 'Reikiavik', 'Ginebra', 2633, 240, 838000, 'Aeropuerto de Keflavík', 'Aeropuerto de Ginebra', 'HB-EUH'),
(295, 1, 'Helsinki', 'Estambul', 2149, 200, 717000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Estambul', 'OH-MRN'),
(296, 2, 'Estambul', 'Helsinki', 2149, 200, 717000, 'Aeropuerto de Estambul', 'Aeropuerto de Helsinki-Vantaa', 'OH-MRN'),
(297, 1, 'Helsinki', 'Florencia', 2041, 190, 690000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Amerigo Vespucci', 'OH-WXV'),
(298, 2, 'Florencia', 'Helsinki', 2041, 190, 690000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Helsinki-Vantaa', 'OH-WXV'),
(299, 1, 'Helsinki', 'Mánchester', 1797, 175, 629000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Mánchester', 'OH-BZY'),
(300, 2, 'Mánchester', 'Helsinki', 1797, 175, 629000, 'Aeropuerto de Mánchester', 'Aeropuerto de Helsinki-Vantaa', 'OH-BZY'),
(301, 1, 'Helsinki', 'Niza', 2180, 205, 725000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Costa Azul', 'OH-ULJ'),
(302, 2, 'Niza', 'Helsinki', 2180, 205, 725000, 'Aeropuerto Costa Azul', 'Aeropuerto de Helsinki-Vantaa', 'OH-ULJ'),
(303, 1, 'Helsinki', 'Reikiavik', 2416, 220, 784000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Keflavík', 'OH-YCR'),
(304, 2, 'Reikiavik', 'Helsinki', 2416, 220, 784000, 'Aeropuerto de Keflavík', 'Aeropuerto de Helsinki-Vantaa', 'OH-YCR'),
(305, 1, 'Helsinki', 'Venecia', 1837, 175, 639000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Marco Polo', 'OH-RMU'),
(306, 2, 'Venecia', 'Helsinki', 1837, 175, 639000, 'Aeropuerto Marco Polo', 'Aeropuerto de Helsinki-Vantaa', 'OH-RMU'),
(307, 1, 'Lisboa', 'Atenas', 2852, 255, 893000, 'Aeropuerto Humberto Delgado', 'Aeropuerto Eleftherios Venizelos', 'CS-TTD'),
(308, 2, 'Atenas', 'Lisboa', 2852, 255, 893000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Humberto Delgado', 'CS-TTD'),
(309, 1, 'Lisboa', 'Bruselas', 1713, 165, 608000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Bruselas-Zaventem', 'CS-JKB'),
(310, 2, 'Bruselas', 'Lisboa', 1713, 165, 608000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Humberto Delgado', 'CS-JKB'),
(311, 1, 'Lisboa', 'Dublín', 1642, 160, 590000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Dublín', 'CS-CVB'),
(312, 2, 'Dublín', 'Lisboa', 1642, 160, 590000, 'Aeropuerto de Dublín', 'Aeropuerto Humberto Delgado', 'CS-CVB'),
(313, 1, 'Lisboa', 'Estocolmo', 2989, 265, 927000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Arlanda', 'CS-CTR'),
(314, 2, 'Estocolmo', 'Lisboa', 2989, 265, 927000, 'Aeropuerto de Arlanda', 'Aeropuerto Humberto Delgado', 'CS-CTR'),
(315, 1, 'Lisboa', 'Mánchester', 1723, 170, 611000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Mánchester', 'CS-VDN'),
(316, 2, 'Mánchester', 'Lisboa', 1723, 170, 611000, 'Aeropuerto de Mánchester', 'Aeropuerto Humberto Delgado', 'CS-VDN'),
(317, 1, 'Lisboa', 'Niza', 1477, 150, 549000, 'Aeropuerto Humberto Delgado', 'Aeropuerto Costa Azul', 'CS-XVR'),
(318, 2, 'Niza', 'Lisboa', 1477, 150, 549000, 'Aeropuerto Costa Azul', 'Aeropuerto Humberto Delgado', 'CS-XVR'),
(319, 1, 'Lisboa', 'Oslo', 2738, 245, 865000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Gardermoen', 'CS-WSU'),
(320, 2, 'Oslo', 'Lisboa', 2738, 245, 865000, 'Aeropuerto de Gardermoen', 'Aeropuerto Humberto Delgado', 'CS-WSU'),
(321, 1, 'Lisboa', 'Praga', 2244, 210, 741000, 'Aeropuerto Humberto Delgado', 'Aeropuerto Václav Havel', 'CS-AEL'),
(322, 2, 'Praga', 'Lisboa', 2244, 210, 741000, 'Aeropuerto Václav Havel', 'Aeropuerto Humberto Delgado', 'CS-AEL'),
(323, 1, 'Lisboa', 'Reikiavik', 2951, 260, 918000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Keflavík', 'CS-EAD'),
(324, 2, 'Reikiavik', 'Lisboa', 2951, 260, 918000, 'Aeropuerto de Keflavík', 'Aeropuerto Humberto Delgado', 'CS-EAD'),
(325, 1, 'Lisboa', 'Venecia', 1915, 180, 659000, 'Aeropuerto Humberto Delgado', 'Aeropuerto Marco Polo', 'CS-NCF'),
(326, 2, 'Venecia', 'Lisboa', 1915, 180, 659000, 'Aeropuerto Marco Polo', 'Aeropuerto Humberto Delgado', 'CS-NCF'),
(327, 1, 'Lisboa', 'Zúrich', 1723, 170, 611000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Zúrich', 'CS-UJA'),
(328, 2, 'Zúrich', 'Lisboa', 1723, 170, 611000, 'Aeropuerto de Zúrich', 'Aeropuerto Humberto Delgado', 'CS-UJA'),
(329, 1, 'Londres', 'Berlín', 931, 105, 413000, 'Aeropuerto de Heathrow', 'Aeropuerto de Berlín-Brandeburgo', 'G-BCWT'),
(330, 2, 'Berlín', 'Londres', 931, 105, 413000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Heathrow', 'G-BCWT'),
(331, 1, 'Londres', 'Budapest', 1449, 145, 542000, 'Aeropuerto de Heathrow', 'Aeropuerto Ferenc Liszt', 'G-DLZE'),
(332, 2, 'Budapest', 'Londres', 1449, 145, 542000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Heathrow', 'G-DLZE'),
(333, 1, 'Londres', 'Copenhague', 956, 110, 419000, 'Aeropuerto de Heathrow', 'Aeropuerto de Kastrup', 'G-KSHL'),
(334, 2, 'Copenhague', 'Londres', 956, 110, 419000, 'Aeropuerto de Kastrup', 'Aeropuerto de Heathrow', 'G-KSHL'),
(335, 1, 'Londres', 'Estocolmo', 1433, 145, 538000, 'Aeropuerto de Heathrow', 'Aeropuerto de Arlanda', 'G-ZRZH'),
(336, 2, 'Estocolmo', 'Londres', 1433, 145, 538000, 'Aeropuerto de Arlanda', 'Aeropuerto de Heathrow', 'G-ZRZH'),
(337, 1, 'Londres', 'Florencia', 1209, 130, 482000, 'Aeropuerto de Heathrow', 'Aeropuerto Amerigo Vespucci', 'G-NHTL'),
(338, 2, 'Florencia', 'Londres', 1209, 130, 482000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Heathrow', 'G-NHTL'),
(339, 1, 'Londres', 'Fráncfort', 638, 85, 339000, 'Aeropuerto de Heathrow', 'Aeropuerto de Fráncfort', 'G-PBSH'),
(340, 2, 'Fráncfort', 'Londres', 638, 85, 339000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Heathrow', 'G-PBSH'),
(341, 1, 'Londres', 'Ginebra', 747, 90, 367000, 'Aeropuerto de Heathrow', 'Aeropuerto de Ginebra', 'G-WFRG'),
(342, 2, 'Ginebra', 'Londres', 747, 90, 367000, 'Aeropuerto de Ginebra', 'Aeropuerto de Heathrow', 'G-WFRG'),
(343, 1, 'Londres', 'Helsinki', 1821, 175, 635000, 'Aeropuerto de Heathrow', 'Aeropuerto de Helsinki-Vantaa', 'G-DVSV'),
(344, 2, 'Helsinki', 'Londres', 1821, 175, 635000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Heathrow', 'G-DVSV'),
(345, 1, 'Londres', 'Lisboa', 1585, 155, 576000, 'Aeropuerto de Heathrow', 'Aeropuerto Humberto Delgado', 'G-NSTA'),
(346, 2, 'Lisboa', 'Londres', 1585, 155, 576000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Heathrow', 'G-NSTA'),
(347, 1, 'Londres', 'Milán', 960, 110, 420000, 'Aeropuerto de Heathrow', 'Aeropuerto de Malpensa', 'G-SGNB'),
(348, 2, 'Milán', 'Londres', 960, 110, 420000, 'Aeropuerto de Malpensa', 'Aeropuerto de Heathrow', 'G-SGNB'),
(349, 1, 'Londres', 'Múnich', 918, 105, 410000, 'Aeropuerto de Heathrow', 'Aeropuerto de Múnich', 'G-TREK'),
(350, 2, 'Múnich', 'Londres', 918, 105, 410000, 'Aeropuerto de Múnich', 'Aeropuerto de Heathrow', 'G-TREK'),
(351, 1, 'Londres', 'Niza', 1029, 115, 437000, 'Aeropuerto de Heathrow', 'Aeropuerto Costa Azul', 'G-HTLJ'),
(352, 2, 'Niza', 'Londres', 1029, 115, 437000, 'Aeropuerto Costa Azul', 'Aeropuerto de Heathrow', 'G-HTLJ'),
(353, 1, 'Londres', 'Oporto', 1321, 135, 510000, 'Aeropuerto de Heathrow', 'Aeropuerto Francisco Sá Carneiro', 'G-PVAM'),
(354, 2, 'Oporto', 'Londres', 1321, 135, 510000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Heathrow', 'G-PVAM'),
(355, 1, 'Londres', 'Praga', 1035, 115, 439000, 'Aeropuerto de Heathrow', 'Aeropuerto Václav Havel', 'G-WTFT'),
(356, 2, 'Praga', 'Londres', 1035, 115, 439000, 'Aeropuerto Václav Havel', 'Aeropuerto de Heathrow', 'G-WTFT'),
(357, 1, 'Londres', 'Roma', 1435, 145, 539000, 'Aeropuerto de Heathrow', 'Aeropuerto de Fiumicino', 'G-DVMG'),
(358, 2, 'Roma', 'Londres', 1435, 145, 539000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Heathrow', 'G-DVMG'),
(359, 1, 'Londres', 'Varsovia', 1448, 145, 542000, 'Aeropuerto de Heathrow', 'Aeropuerto Chopin de Varsovia', 'G-VCUF'),
(360, 2, 'Varsovia', 'Londres', 1448, 145, 542000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Heathrow', 'G-VCUF'),
(361, 1, 'Londres', 'Viena', 1235, 130, 489000, 'Aeropuerto de Heathrow', 'Aeropuerto de Viena-Schwechat', 'G-RNDD'),
(362, 2, 'Viena', 'Londres', 1235, 130, 489000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Heathrow', 'G-RNDD'),
(363, 1, 'Londres', 'Zúrich', 776, 95, 374000, 'Aeropuerto de Heathrow', 'Aeropuerto de Zúrich', 'G-PNAH'),
(364, 2, 'Zúrich', 'Londres', 776, 95, 374000, 'Aeropuerto de Zúrich', 'Aeropuerto de Heathrow', 'G-PNAH'),
(365, 1, 'Madrid', 'Atenas', 2370, 215, 772000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Eleftherios Venizelos', 'EC-GBJ'),
(366, 2, 'Atenas', 'Madrid', 2370, 215, 772000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Adolfo Suárez Barajas', 'EC-GBJ'),
(367, 1, 'Madrid', 'Berlín', 1868, 180, 647000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Berlín-Brandeburgo', 'EC-FTJ'),
(368, 2, 'Berlín', 'Madrid', 1868, 180, 647000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Adolfo Suárez Barajas', 'EC-FTJ'),
(369, 1, 'Madrid', 'Budapest', 1974, 185, 673000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Ferenc Liszt', 'EC-MCJ'),
(370, 2, 'Budapest', 'Madrid', 1974, 185, 673000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Adolfo Suárez Barajas', 'EC-MCJ'),
(371, 1, 'Madrid', 'Dublín', 1451, 145, 543000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Dublín', 'EC-FBG'),
(372, 2, 'Dublín', 'Madrid', 1451, 145, 543000, 'Aeropuerto de Dublín', 'Aeropuerto Adolfo Suárez Barajas', 'EC-FBG'),
(373, 1, 'Madrid', 'Edimburgo', 1727, 170, 612000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Edimburgo', 'EC-ZXZ'),
(374, 2, 'Edimburgo', 'Madrid', 1727, 170, 612000, 'Aeropuerto de Edimburgo', 'Aeropuerto Adolfo Suárez Barajas', 'EC-ZXZ'),
(375, 1, 'Madrid', 'Estambul', 2739, 245, 865000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Estambul', 'EC-UDR'),
(376, 2, 'Estambul', 'Madrid', 2739, 245, 865000, 'Aeropuerto de Estambul', 'Aeropuerto Adolfo Suárez Barajas', 'EC-UDR'),
(377, 1, 'Madrid', 'Florencia', 1287, 135, 502000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Amerigo Vespucci', 'EC-UYB'),
(378, 2, 'Florencia', 'Madrid', 1287, 135, 502000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Adolfo Suárez Barajas', 'EC-UYB'),
(379, 1, 'Madrid', 'Fráncfort', 1445, 145, 541000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Fráncfort', 'EC-MZR'),
(380, 2, 'Fráncfort', 'Madrid', 1445, 145, 541000, 'Aeropuerto de Fráncfort', 'Aeropuerto Adolfo Suárez Barajas', 'EC-MZR'),
(381, 1, 'Madrid', 'Ginebra', 1022, 115, 435000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Ginebra', 'EC-TXB'),
(382, 2, 'Ginebra', 'Madrid', 1022, 115, 435000, 'Aeropuerto de Ginebra', 'Aeropuerto Adolfo Suárez Barajas', 'EC-TXB'),
(383, 1, 'Madrid', 'Helsinki', 2948, 260, 917000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Helsinki-Vantaa', 'EC-GXX'),
(384, 2, 'Helsinki', 'Madrid', 2948, 260, 917000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Adolfo Suárez Barajas', 'EC-GXX'),
(385, 1, 'Madrid', 'Milán', 1188, 125, 477000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Malpensa', 'EC-SBK'),
(386, 2, 'Milán', 'Madrid', 1188, 125, 477000, 'Aeropuerto de Malpensa', 'Aeropuerto Adolfo Suárez Barajas', 'EC-SBK'),
(387, 1, 'Madrid', 'Mánchester', 1456, 145, 544000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Mánchester', 'EC-CFX'),
(388, 2, 'Mánchester', 'Madrid', 1456, 145, 544000, 'Aeropuerto de Mánchester', 'Aeropuerto Adolfo Suárez Barajas', 'EC-CFX'),
(389, 1, 'Madrid', 'Niza', 975, 110, 424000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Costa Azul', 'EC-CJG'),
(390, 2, 'Niza', 'Madrid', 975, 110, 424000, 'Aeropuerto Costa Azul', 'Aeropuerto Adolfo Suárez Barajas', 'EC-CJG'),
(391, 1, 'Madrid', 'Oslo', 2387, 220, 777000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Gardermoen', 'EC-AKW'),
(392, 2, 'Oslo', 'Madrid', 2387, 220, 777000, 'Aeropuerto de Gardermoen', 'Aeropuerto Adolfo Suárez Barajas', 'EC-AKW'),
(393, 1, 'Madrid', 'Praga', 1773, 170, 623000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Václav Havel', 'EC-XBS'),
(394, 2, 'Praga', 'Madrid', 1773, 170, 623000, 'Aeropuerto Václav Havel', 'Aeropuerto Adolfo Suárez Barajas', 'EC-XBS'),
(395, 1, 'Madrid', 'Reikiavik', 2891, 255, 903000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Keflavík', 'EC-JCZ'),
(396, 2, 'Reikiavik', 'Madrid', 2891, 255, 903000, 'Aeropuerto de Keflavík', 'Aeropuerto Adolfo Suárez Barajas', 'EC-JCZ'),
(397, 1, 'Madrid', 'Varsovia', 2289, 210, 752000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Chopin de Varsovia', 'EC-UFU'),
(398, 2, 'Varsovia', 'Madrid', 2289, 210, 752000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto Adolfo Suárez Barajas', 'EC-UFU'),
(399, 1, 'Madrid', 'Viena', 1809, 175, 632000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Viena-Schwechat', 'EC-XTV'),
(400, 2, 'Viena', 'Madrid', 1809, 175, 632000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Adolfo Suárez Barajas', 'EC-XTV'),
(401, 1, 'Madrid', 'Zúrich', 1247, 130, 492000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Zúrich', 'EC-UHS'),
(402, 2, 'Zúrich', 'Madrid', 1247, 130, 492000, 'Aeropuerto de Zúrich', 'Aeropuerto Adolfo Suárez Barajas', 'EC-UHS'),
(403, 1, 'Milán', 'Atenas', 1462, 145, 546000, 'Aeropuerto de Malpensa', 'Aeropuerto Eleftherios Venizelos', 'I-BUMH'),
(404, 2, 'Atenas', 'Milán', 1462, 145, 546000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Malpensa', 'I-BUMH'),
(405, 1, 'Milán', 'Berlín', 843, 100, 391000, 'Aeropuerto de Malpensa', 'Aeropuerto de Berlín-Brandeburgo', 'I-UNRC'),
(406, 2, 'Berlín', 'Milán', 843, 100, 391000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Malpensa', 'I-UNRC'),
(407, 1, 'Milán', 'Bruselas', 698, 90, 355000, 'Aeropuerto de Malpensa', 'Aeropuerto de Bruselas-Zaventem', 'I-HBAP'),
(408, 2, 'Bruselas', 'Milán', 698, 90, 355000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Malpensa', 'I-HBAP'),
(409, 1, 'Milán', 'Budapest', 787, 95, 377000, 'Aeropuerto de Malpensa', 'Aeropuerto Ferenc Liszt', 'I-NUCR'),
(410, 2, 'Budapest', 'Milán', 787, 95, 377000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Malpensa', 'I-NUCR'),
(411, 1, 'Milán', 'Copenhague', 1161, 125, 470000, 'Aeropuerto de Malpensa', 'Aeropuerto de Kastrup', 'I-YWFF'),
(412, 2, 'Copenhague', 'Milán', 1161, 125, 470000, 'Aeropuerto de Kastrup', 'Aeropuerto de Malpensa', 'I-YWFF'),
(413, 1, 'Milán', 'Dublín', 1416, 145, 534000, 'Aeropuerto de Malpensa', 'Aeropuerto de Dublín', 'I-HFUG'),
(414, 2, 'Dublín', 'Milán', 1416, 145, 534000, 'Aeropuerto de Dublín', 'Aeropuerto de Malpensa', 'I-HFUG'),
(415, 1, 'Milán', 'Edimburgo', 1452, 145, 543000, 'Aeropuerto de Malpensa', 'Aeropuerto de Edimburgo', 'I-TLNJ'),
(416, 2, 'Edimburgo', 'Milán', 1452, 145, 543000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Malpensa', 'I-TLNJ'),
(417, 1, 'Milán', 'Estambul', 1673, 165, 598000, 'Aeropuerto de Malpensa', 'Aeropuerto de Estambul', 'I-XXFR'),
(418, 2, 'Estambul', 'Milán', 1673, 165, 598000, 'Aeropuerto de Estambul', 'Aeropuerto de Malpensa', 'I-XXFR'),
(419, 1, 'Milán', 'Mánchester', 1213, 130, 483000, 'Aeropuerto de Malpensa', 'Aeropuerto de Mánchester', 'I-CAMC'),
(420, 2, 'Mánchester', 'Milán', 1213, 130, 483000, 'Aeropuerto de Mánchester', 'Aeropuerto de Malpensa', 'I-CAMC'),
(421, 1, 'Milán', 'Oslo', 1610, 160, 583000, 'Aeropuerto de Malpensa', 'Aeropuerto de Gardermoen', 'I-EVTH'),
(422, 2, 'Oslo', 'Milán', 1610, 160, 583000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Malpensa', 'I-EVTH'),
(423, 1, 'Milán', 'Reikiavik', 2826, 250, 886000, 'Aeropuerto de Malpensa', 'Aeropuerto de Keflavík', 'I-SZEY'),
(424, 2, 'Reikiavik', 'Milán', 2826, 250, 886000, 'Aeropuerto de Keflavík', 'Aeropuerto de Malpensa', 'I-SZEY'),
(425, 1, 'Milán', 'Varsovia', 1144, 125, 466000, 'Aeropuerto de Malpensa', 'Aeropuerto Chopin de Varsovia', 'I-LFSU'),
(426, 2, 'Varsovia', 'Milán', 1144, 125, 466000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Malpensa', 'I-LFSU'),
(427, 1, 'Milán', 'Viena', 626, 85, 336000, 'Aeropuerto de Malpensa', 'Aeropuerto de Viena-Schwechat', 'I-DEAN'),
(428, 2, 'Viena', 'Milán', 626, 85, 336000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Malpensa', 'I-DEAN'),
(429, 1, 'Múnich', 'Atenas', 1497, 150, 554000, 'Aeropuerto de Múnich', 'Aeropuerto Eleftherios Venizelos', 'D-CZSX'),
(430, 2, 'Atenas', 'Múnich', 1497, 150, 554000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Múnich', 'D-CZSX'),
(431, 1, 'Múnich', 'Budapest', 561, 80, 320000, 'Aeropuerto de Múnich', 'Aeropuerto Ferenc Liszt', 'D-PYVL'),
(432, 2, 'Budapest', 'Múnich', 561, 80, 320000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Múnich', 'D-PYVL'),
(433, 1, 'Múnich', 'Copenhague', 841, 100, 390000, 'Aeropuerto de Múnich', 'Aeropuerto de Kastrup', 'D-TMCY'),
(434, 2, 'Copenhague', 'Múnich', 841, 100, 390000, 'Aeropuerto de Kastrup', 'Aeropuerto de Múnich', 'D-TMCY'),
(435, 1, 'Múnich', 'Dublín', 1378, 140, 524000, 'Aeropuerto de Múnich', 'Aeropuerto de Dublín', 'D-VAXJ'),
(436, 2, 'Dublín', 'Múnich', 1378, 140, 524000, 'Aeropuerto de Dublín', 'Aeropuerto de Múnich', 'D-VAXJ'),
(437, 1, 'Múnich', 'Estocolmo', 1314, 135, 508000, 'Aeropuerto de Múnich', 'Aeropuerto de Arlanda', 'D-HDNH'),
(438, 2, 'Estocolmo', 'Múnich', 1314, 135, 508000, 'Aeropuerto de Arlanda', 'Aeropuerto de Múnich', 'D-HDNH'),
(439, 1, 'Múnich', 'Helsinki', 1589, 155, 577000, 'Aeropuerto de Múnich', 'Aeropuerto de Helsinki-Vantaa', 'D-KWYV'),
(440, 2, 'Helsinki', 'Múnich', 1589, 155, 577000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Múnich', 'D-KWYV'),
(441, 1, 'Múnich', 'Lisboa', 1964, 185, 671000, 'Aeropuerto de Múnich', 'Aeropuerto Humberto Delgado', 'D-YVFK'),
(442, 2, 'Lisboa', 'Múnich', 1964, 185, 671000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Múnich', 'D-YVFK'),
(443, 1, 'Múnich', 'Mánchester', 1135, 120, 464000, 'Aeropuerto de Múnich', 'Aeropuerto de Mánchester', 'D-UAAL'),
(444, 2, 'Mánchester', 'Múnich', 1135, 120, 464000, 'Aeropuerto de Mánchester', 'Aeropuerto de Múnich', 'D-UAAL'),
(445, 1, 'Múnich', 'Oporto', 1769, 170, 622000, 'Aeropuerto de Múnich', 'Aeropuerto Francisco Sá Carneiro', 'D-FZRV'),
(446, 2, 'Oporto', 'Múnich', 1769, 170, 622000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Múnich', 'D-FZRV'),
(447, 1, 'Múnich', 'Oslo', 1310, 135, 507000, 'Aeropuerto de Múnich', 'Aeropuerto de Gardermoen', 'D-DHYS'),
(448, 2, 'Oslo', 'Múnich', 1310, 135, 507000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Múnich', 'D-DHYS'),
(449, 1, 'Múnich', 'Reikiavik', 2680, 240, 850000, 'Aeropuerto de Múnich', 'Aeropuerto de Keflavík', 'D-FWTY'),
(450, 2, 'Reikiavik', 'Múnich', 2680, 240, 850000, 'Aeropuerto de Keflavík', 'Aeropuerto de Múnich', 'D-FWTY'),
(451, 1, 'Múnich', 'Varsovia', 810, 95, 382000, 'Aeropuerto de Múnich', 'Aeropuerto Chopin de Varsovia', 'D-SDCE'),
(452, 2, 'Varsovia', 'Múnich', 810, 95, 382000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Múnich', 'D-SDCE'),
(453, 1, 'Niza', 'Edimburgo', 1551, 155, 568000, 'Aeropuerto Costa Azul', 'Aeropuerto de Edimburgo', 'F-AWTK'),
(454, 2, 'Edimburgo', 'Niza', 1551, 155, 568000, 'Aeropuerto de Edimburgo', 'Aeropuerto Costa Azul', 'F-AWTK'),
(455, 1, 'Niza', 'Mánchester', 1291, 135, 503000, 'Aeropuerto Costa Azul', 'Aeropuerto de Mánchester', 'F-EPJA'),
(456, 2, 'Mánchester', 'Niza', 1291, 135, 503000, 'Aeropuerto de Mánchester', 'Aeropuerto Costa Azul', 'F-EPJA'),
(457, 1, 'Oporto', 'Atenas', 2778, 250, 875000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto Eleftherios Venizelos', 'CS-RWK'),
(458, 2, 'Atenas', 'Oporto', 2778, 250, 875000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Francisco Sá Carneiro', 'CS-RWK'),
(459, 1, 'Oporto', 'Bruselas', 1467, 150, 547000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Bruselas-Zaventem', 'CS-ENX'),
(460, 2, 'Bruselas', 'Oporto', 1467, 150, 547000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto Francisco Sá Carneiro', 'CS-ENX'),
(461, 1, 'Oporto', 'Budapest', 2296, 210, 754000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto Ferenc Liszt', 'CS-XAR'),
(462, 2, 'Budapest', 'Oporto', 2296, 210, 754000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Francisco Sá Carneiro', 'CS-XAR'),
(463, 1, 'Oporto', 'Copenhague', 2230, 205, 738000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Kastrup', 'CS-VDD'),
(464, 2, 'Copenhague', 'Oporto', 2230, 205, 738000, 'Aeropuerto de Kastrup', 'Aeropuerto Francisco Sá Carneiro', 'CS-VDD'),
(465, 1, 'Oporto', 'Dublín', 1368, 140, 522000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Dublín', 'CS-HLR'),
(466, 2, 'Dublín', 'Oporto', 1368, 140, 522000, 'Aeropuerto de Dublín', 'Aeropuerto Francisco Sá Carneiro', 'CS-HLR'),
(467, 1, 'Oporto', 'Edimburgo', 1692, 165, 603000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Edimburgo', 'CS-CNJ'),
(468, 2, 'Edimburgo', 'Oporto', 1692, 165, 603000, 'Aeropuerto de Edimburgo', 'Aeropuerto Francisco Sá Carneiro', 'CS-CNJ'),
(469, 1, 'Oporto', 'Estocolmo', 2736, 245, 864000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Arlanda', 'CS-RRK'),
(470, 2, 'Estocolmo', 'Oporto', 2736, 245, 864000, 'Aeropuerto de Arlanda', 'Aeropuerto Francisco Sá Carneiro', 'CS-RRK'),
(471, 1, 'Oporto', 'Ginebra', 1310, 135, 507000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Ginebra', 'CS-JRW'),
(472, 2, 'Ginebra', 'Oporto', 1310, 135, 507000, 'Aeropuerto de Ginebra', 'Aeropuerto Francisco Sá Carneiro', 'CS-JRW'),
(473, 1, 'Oporto', 'Mánchester', 1451, 145, 543000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Mánchester', 'CS-PSK'),
(474, 2, 'Mánchester', 'Oporto', 1451, 145, 543000, 'Aeropuerto de Mánchester', 'Aeropuerto Francisco Sá Carneiro', 'CS-PSK'),
(475, 1, 'Oporto', 'Oslo', 2474, 225, 799000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Gardermoen', 'CS-LFD'),
(476, 2, 'Oslo', 'Oporto', 2474, 225, 799000, 'Aeropuerto de Gardermoen', 'Aeropuerto Francisco Sá Carneiro', 'CS-LFD'),
(477, 1, 'Oporto', 'Praga', 2037, 190, 689000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto Václav Havel', 'CS-WUA'),
(478, 2, 'Praga', 'Oporto', 2037, 190, 689000, 'Aeropuerto Václav Havel', 'Aeropuerto Francisco Sá Carneiro', 'CS-WUA'),
(479, 1, 'Oporto', 'Reikiavik', 2698, 245, 855000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Keflavík', 'CS-GGZ'),
(480, 2, 'Reikiavik', 'Oporto', 2698, 245, 855000, 'Aeropuerto de Keflavík', 'Aeropuerto Francisco Sá Carneiro', 'CS-GGZ'),
(481, 1, 'Oporto', 'Varsovia', 2548, 230, 817000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto Chopin de Varsovia', 'CS-KZN'),
(482, 2, 'Varsovia', 'Oporto', 2548, 230, 817000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto Francisco Sá Carneiro', 'CS-KZN'),
(483, 1, 'Oporto', 'Venecia', 1754, 170, 619000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto Marco Polo', 'CS-HZY'),
(484, 2, 'Venecia', 'Oporto', 1754, 170, 619000, 'Aeropuerto Marco Polo', 'Aeropuerto Francisco Sá Carneiro', 'CS-HZY'),
(485, 1, 'Oporto', 'Viena', 2114, 200, 708000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Viena-Schwechat', 'CS-HNE'),
(486, 2, 'Viena', 'Oporto', 2114, 200, 708000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Francisco Sá Carneiro', 'CS-HNE'),
(487, 1, 'Oslo', 'Edimburgo', 931, 105, 413000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Edimburgo', 'LN-WLX'),
(488, 2, 'Edimburgo', 'Oslo', 931, 105, 413000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Gardermoen', 'LN-WLX'),
(489, 1, 'Oslo', 'Florencia', 1795, 175, 629000, 'Aeropuerto de Gardermoen', 'Aeropuerto Amerigo Vespucci', 'LN-UNG'),
(490, 2, 'Florencia', 'Oslo', 1795, 175, 629000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Gardermoen', 'LN-UNG'),
(491, 1, 'Oslo', 'Helsinki', 787, 95, 377000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Helsinki-Vantaa', 'LN-WUK'),
(492, 2, 'Helsinki', 'Oslo', 787, 95, 377000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Gardermoen', 'LN-WUK'),
(493, 1, 'Oslo', 'Mánchester', 1064, 115, 446000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Mánchester', 'LN-UUN'),
(494, 2, 'Mánchester', 'Oslo', 1064, 115, 446000, 'Aeropuerto de Mánchester', 'Aeropuerto de Gardermoen', 'LN-UUN'),
(495, 1, 'Oslo', 'Niza', 1818, 175, 634000, 'Aeropuerto de Gardermoen', 'Aeropuerto Costa Azul', 'LN-ZJM'),
(496, 2, 'Niza', 'Oslo', 1818, 175, 634000, 'Aeropuerto Costa Azul', 'Aeropuerto de Gardermoen', 'LN-ZJM'),
(497, 1, 'Oslo', 'Reikiavik', 1747, 170, 617000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Keflavík', 'LN-RHF'),
(498, 2, 'Reikiavik', 'Oslo', 1747, 170, 617000, 'Aeropuerto de Keflavík', 'Aeropuerto de Gardermoen', 'LN-RHF'),
(499, 1, 'Oslo', 'Venecia', 1612, 160, 583000, 'Aeropuerto de Gardermoen', 'Aeropuerto Marco Polo', 'LN-TKB'),
(500, 2, 'Venecia', 'Oslo', 1612, 160, 583000, 'Aeropuerto Marco Polo', 'Aeropuerto de Gardermoen', 'LN-TKB'),
(501, 1, 'París', 'Atenas', 2097, 195, 704000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Eleftherios Venizelos', 'F-WGKF'),
(502, 2, 'Atenas', 'París', 2097, 195, 704000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Charles de Gaulle', 'F-WGKF'),
(503, 1, 'París', 'Berlín', 877, 100, 399000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Berlín-Brandeburgo', 'F-PYVE'),
(504, 2, 'Berlín', 'París', 877, 100, 399000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto Charles de Gaulle', 'F-PYVE'),
(505, 1, 'París', 'Budapest', 1244, 130, 491000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Ferenc Liszt', 'F-RNXS'),
(506, 2, 'Budapest', 'París', 1244, 130, 491000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto Charles de Gaulle', 'F-RNXS'),
(507, 1, 'París', 'Copenhague', 1027, 115, 437000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Kastrup', 'F-YBWK'),
(508, 2, 'Copenhague', 'París', 1027, 115, 437000, 'Aeropuerto de Kastrup', 'Aeropuerto Charles de Gaulle', 'F-YBWK'),
(509, 1, 'París', 'Dublín', 781, 95, 375000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Dublín', 'F-BWTL'),
(510, 2, 'Dublín', 'París', 781, 95, 375000, 'Aeropuerto de Dublín', 'Aeropuerto Charles de Gaulle', 'F-BWTL'),
(511, 1, 'París', 'Estocolmo', 1543, 155, 566000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Arlanda', 'F-UKPH'),
(512, 2, 'Estocolmo', 'París', 1543, 155, 566000, 'Aeropuerto de Arlanda', 'Aeropuerto Charles de Gaulle', 'F-UKPH'),
(513, 1, 'París', 'Florencia', 887, 105, 402000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Amerigo Vespucci', 'F-ZXUT'),
(514, 2, 'Florencia', 'París', 887, 105, 402000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Charles de Gaulle', 'F-ZXUT'),
(515, 1, 'París', 'Helsinki', 1908, 180, 657000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Helsinki-Vantaa', 'F-KYWT'),
(516, 2, 'Helsinki', 'París', 1908, 180, 657000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Charles de Gaulle', 'F-KYWT'),
(517, 1, 'París', 'Lisboa', 1453, 145, 543000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Humberto Delgado', 'F-MMNT'),
(518, 2, 'Lisboa', 'París', 1453, 145, 543000, 'Aeropuerto Humberto Delgado', 'Aeropuerto Charles de Gaulle', 'F-MMNT'),
(519, 1, 'París', 'Mánchester', 605, 80, 331000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Mánchester', 'F-HAPT'),
(520, 2, 'Mánchester', 'París', 605, 80, 331000, 'Aeropuerto de Mánchester', 'Aeropuerto Charles de Gaulle', 'F-HAPT'),
(521, 1, 'París', 'Múnich', 684, 90, 351000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Múnich', 'F-PNWM'),
(522, 2, 'Múnich', 'París', 684, 90, 351000, 'Aeropuerto de Múnich', 'Aeropuerto Charles de Gaulle', 'F-PNWM'),
(523, 1, 'París', 'Niza', 687, 90, 352000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Costa Azul', 'F-HUVN'),
(524, 2, 'Niza', 'París', 687, 90, 352000, 'Aeropuerto Costa Azul', 'Aeropuerto Charles de Gaulle', 'F-HUVN'),
(525, 1, 'París', 'Oslo', 1341, 140, 515000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Gardermoen', 'F-MADH'),
(526, 2, 'Oslo', 'París', 1341, 140, 515000, 'Aeropuerto de Gardermoen', 'Aeropuerto Charles de Gaulle', 'F-MADH'),
(527, 1, 'París', 'Praga', 883, 105, 401000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Václav Havel', 'F-GFSW'),
(528, 2, 'Praga', 'París', 883, 105, 401000, 'Aeropuerto Václav Havel', 'Aeropuerto Charles de Gaulle', 'F-GFSW'),
(529, 1, 'París', 'Reikiavik', 2233, 205, 738000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Keflavík', 'F-NSFP'),
(530, 2, 'Reikiavik', 'París', 2233, 205, 738000, 'Aeropuerto de Keflavík', 'Aeropuerto Charles de Gaulle', 'F-NSFP'),
(531, 1, 'París', 'Roma', 1106, 120, 457000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Fiumicino', 'F-TGMA'),
(532, 2, 'Roma', 'París', 1106, 120, 457000, 'Aeropuerto de Fiumicino', 'Aeropuerto Charles de Gaulle', 'F-TGMA'),
(533, 1, 'París', 'Varsovia', 1366, 140, 522000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Chopin de Varsovia', 'F-UCDD'),
(534, 2, 'Varsovia', 'París', 1366, 140, 522000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto Charles de Gaulle', 'F-UCDD'),
(535, 1, 'París', 'Viena', 1033, 115, 438000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto de Viena-Schwechat', 'F-GNDR'),
(536, 2, 'Viena', 'París', 1033, 115, 438000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Charles de Gaulle', 'F-GNDR'),
(537, 1, 'Praga', 'Edimburgo', 1342, 140, 516000, 'Aeropuerto Václav Havel', 'Aeropuerto de Edimburgo', 'OK-WMX'),
(538, 2, 'Edimburgo', 'Praga', 1342, 140, 516000, 'Aeropuerto de Edimburgo', 'Aeropuerto Václav Havel', 'OK-WMX'),
(539, 1, 'Praga', 'Estambul', 1512, 150, 558000, 'Aeropuerto Václav Havel', 'Aeropuerto de Estambul', 'OK-TXD'),
(540, 2, 'Estambul', 'Praga', 1512, 150, 558000, 'Aeropuerto de Estambul', 'Aeropuerto Václav Havel', 'OK-TXD'),
(541, 1, 'Praga', 'Florencia', 742, 90, 365000, 'Aeropuerto Václav Havel', 'Aeropuerto Amerigo Vespucci', 'OK-PHL'),
(542, 2, 'Florencia', 'Praga', 742, 90, 365000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Václav Havel', 'OK-PHL'),
(543, 1, 'Praga', 'Mánchester', 1205, 130, 481000, 'Aeropuerto Václav Havel', 'Aeropuerto de Mánchester', 'OK-KAD'),
(544, 2, 'Mánchester', 'Praga', 1205, 130, 481000, 'Aeropuerto de Mánchester', 'Aeropuerto Václav Havel', 'OK-KAD'),
(545, 1, 'Praga', 'Niza', 894, 105, 403000, 'Aeropuerto Václav Havel', 'Aeropuerto Costa Azul', 'OK-CLY'),
(546, 2, 'Niza', 'Praga', 894, 105, 403000, 'Aeropuerto Costa Azul', 'Aeropuerto Václav Havel', 'OK-CLY'),
(547, 1, 'Praga', 'Oslo', 1118, 120, 459000, 'Aeropuerto Václav Havel', 'Aeropuerto de Gardermoen', 'OK-ERX'),
(548, 2, 'Oslo', 'Praga', 1118, 120, 459000, 'Aeropuerto de Gardermoen', 'Aeropuerto Václav Havel', 'OK-ERX'),
(549, 1, 'Praga', 'Reikiavik', 2638, 240, 840000, 'Aeropuerto Václav Havel', 'Aeropuerto de Keflavík', 'OK-PDX'),
(550, 2, 'Reikiavik', 'Praga', 2638, 240, 840000, 'Aeropuerto de Keflavík', 'Aeropuerto Václav Havel', 'OK-PDX'),
(551, 1, 'Reikiavik', 'Edimburgo', 1374, 140, 524000, 'Aeropuerto de Keflavík', 'Aeropuerto de Edimburgo', 'TF-DGU'),
(552, 2, 'Edimburgo', 'Reikiavik', 1374, 140, 524000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Keflavík', 'TF-DGU'),
(553, 1, 'Reikiavik', 'Mánchester', 1628, 160, 587000, 'Aeropuerto de Keflavík', 'Aeropuerto de Mánchester', 'TF-SLW'),
(554, 2, 'Mánchester', 'Reikiavik', 1628, 160, 587000, 'Aeropuerto de Mánchester', 'Aeropuerto de Keflavík', 'TF-SLW'),
(555, 1, 'Reikiavik', 'Niza', 2919, 260, 910000, 'Aeropuerto de Keflavík', 'Aeropuerto Costa Azul', 'TF-FDC'),
(556, 2, 'Niza', 'Reikiavik', 2919, 260, 910000, 'Aeropuerto Costa Azul', 'Aeropuerto de Keflavík', 'TF-FDC'),
(557, 1, 'Roma', 'Atenas', 1051, 115, 443000, 'Aeropuerto de Fiumicino', 'Aeropuerto Eleftherios Venizelos', 'I-MHER'),
(558, 2, 'Atenas', 'Roma', 1051, 115, 443000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Fiumicino', 'I-MHER'),
(559, 1, 'Roma', 'Berlín', 1183, 125, 476000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Berlín-Brandeburgo', 'I-RDUP'),
(560, 2, 'Berlín', 'Roma', 1183, 125, 476000, 'Aeropuerto de Berlín-Brandeburgo', 'Aeropuerto de Fiumicino', 'I-RDUP'),
(561, 1, 'Roma', 'Bruselas', 1174, 125, 473000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Bruselas-Zaventem', 'I-ECAY'),
(562, 2, 'Bruselas', 'Roma', 1174, 125, 473000, 'Aeropuerto de Bruselas-Zaventem', 'Aeropuerto de Fiumicino', 'I-ECAY'),
(563, 1, 'Roma', 'Budapest', 809, 95, 382000, 'Aeropuerto de Fiumicino', 'Aeropuerto Ferenc Liszt', 'I-RPCR'),
(564, 2, 'Budapest', 'Roma', 809, 95, 382000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Fiumicino', 'I-RPCR'),
(565, 1, 'Roma', 'Copenhague', 1532, 155, 563000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Kastrup', 'I-SBRS'),
(566, 2, 'Copenhague', 'Roma', 1532, 155, 563000, 'Aeropuerto de Kastrup', 'Aeropuerto de Fiumicino', 'I-SBRS'),
(567, 1, 'Roma', 'Dublín', 1886, 180, 652000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Dublín', 'I-MDBP'),
(568, 2, 'Dublín', 'Roma', 1886, 180, 652000, 'Aeropuerto de Dublín', 'Aeropuerto de Fiumicino', 'I-MDBP'),
(569, 1, 'Roma', 'Edimburgo', 1928, 185, 662000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Edimburgo', 'I-FPXC'),
(570, 2, 'Edimburgo', 'Roma', 1928, 185, 662000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Fiumicino', 'I-FPXC'),
(571, 1, 'Roma', 'Estambul', 1375, 140, 524000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Estambul', 'I-JZUT'),
(572, 2, 'Estambul', 'Roma', 1375, 140, 524000, 'Aeropuerto de Estambul', 'Aeropuerto de Fiumicino', 'I-JZUT'),
(573, 1, 'Roma', 'Estocolmo', 1976, 185, 674000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Arlanda', 'I-HPDM'),
(574, 2, 'Estocolmo', 'Roma', 1976, 185, 674000, 'Aeropuerto de Arlanda', 'Aeropuerto de Fiumicino', 'I-HPDM'),
(575, 1, 'Roma', 'Fráncfort', 959, 110, 420000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Fráncfort', 'I-FAEW'),
(576, 2, 'Fráncfort', 'Roma', 959, 110, 420000, 'Aeropuerto de Fráncfort', 'Aeropuerto de Fiumicino', 'I-FAEW'),
(577, 1, 'Roma', 'Ginebra', 697, 90, 354000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Ginebra', 'I-WKJP'),
(578, 2, 'Ginebra', 'Roma', 697, 90, 354000, 'Aeropuerto de Ginebra', 'Aeropuerto de Fiumicino', 'I-WKJP'),
(579, 1, 'Roma', 'Lisboa', 1863, 180, 646000, 'Aeropuerto de Fiumicino', 'Aeropuerto Humberto Delgado', 'I-DZHM'),
(580, 2, 'Lisboa', 'Roma', 1863, 180, 646000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Fiumicino', 'I-DZHM'),
(581, 1, 'Roma', 'Mánchester', 1689, 165, 602000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Mánchester', 'I-CPXC'),
(582, 2, 'Mánchester', 'Roma', 1689, 165, 602000, 'Aeropuerto de Mánchester', 'Aeropuerto de Fiumicino', 'I-CPXC'),
(583, 1, 'Roma', 'Múnich', 698, 90, 354000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Múnich', 'I-VWJL'),
(584, 2, 'Múnich', 'Roma', 698, 90, 354000, 'Aeropuerto de Múnich', 'Aeropuerto de Fiumicino', 'I-VWJL'),
(585, 1, 'Roma', 'Oporto', 1755, 170, 619000, 'Aeropuerto de Fiumicino', 'Aeropuerto Francisco Sá Carneiro', 'I-PWVK'),
(586, 2, 'Oporto', 'Roma', 1755, 170, 619000, 'Aeropuerto Francisco Sá Carneiro', 'Aeropuerto de Fiumicino', 'I-PWVK'),
(587, 1, 'Roma', 'Oslo', 2006, 190, 682000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Gardermoen', 'I-HWTF'),
(588, 2, 'Oslo', 'Roma', 2006, 190, 682000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Fiumicino', 'I-HWTF'),
(589, 1, 'Roma', 'Praga', 922, 105, 410000, 'Aeropuerto de Fiumicino', 'Aeropuerto Václav Havel', 'I-TPJB'),
(590, 2, 'Praga', 'Roma', 922, 105, 410000, 'Aeropuerto Václav Havel', 'Aeropuerto de Fiumicino', 'I-TPJB'),
(591, 1, 'Roma', 'Varsovia', 1315, 135, 509000, 'Aeropuerto de Fiumicino', 'Aeropuerto Chopin de Varsovia', 'I-UWJT'),
(592, 2, 'Varsovia', 'Roma', 1315, 135, 509000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Fiumicino', 'I-UWJT'),
(593, 1, 'Roma', 'Viena', 764, 95, 371000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Viena-Schwechat', 'I-RKPJ'),
(594, 2, 'Viena', 'Roma', 764, 95, 371000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Fiumicino', 'I-RKPJ'),
(595, 1, 'Roma', 'Zúrich', 685, 90, 351000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Zúrich', 'I-GZVR'),
(596, 2, 'Zúrich', 'Roma', 685, 90, 351000, 'Aeropuerto de Zúrich', 'Aeropuerto de Fiumicino', 'I-GZVR'),
(597, 1, 'Roma', 'Ámsterdam', 1297, 135, 504000, 'Aeropuerto de Fiumicino', 'Aeropuerto de Schiphol', 'I-FMTK'),
(598, 2, 'Ámsterdam', 'Roma', 1297, 135, 504000, 'Aeropuerto de Schiphol', 'Aeropuerto de Fiumicino', 'I-FMTK'),
(599, 1, 'Varsovia', 'Estambul', 1386, 140, 527000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Estambul', 'SP-JKD'),
(600, 2, 'Estambul', 'Varsovia', 1386, 140, 527000, 'Aeropuerto de Estambul', 'Aeropuerto Chopin de Varsovia', 'SP-JKD'),
(601, 1, 'Varsovia', 'Estocolmo', 810, 95, 383000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Arlanda', 'SP-WHA'),
(602, 2, 'Estocolmo', 'Varsovia', 810, 95, 383000, 'Aeropuerto de Arlanda', 'Aeropuerto Chopin de Varsovia', 'SP-WHA'),
(603, 1, 'Varsovia', 'Florencia', 1186, 125, 476000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto Amerigo Vespucci', 'SP-GWW'),
(604, 2, 'Florencia', 'Varsovia', 1186, 125, 476000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto Chopin de Varsovia', 'SP-GWW'),
(605, 1, 'Varsovia', 'Helsinki', 915, 105, 409000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Helsinki-Vantaa', 'SP-LZL'),
(606, 2, 'Helsinki', 'Varsovia', 915, 105, 409000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto Chopin de Varsovia', 'SP-LZL'),
(607, 1, 'Varsovia', 'Oslo', 1063, 115, 446000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Gardermoen', 'SP-XDY'),
(608, 2, 'Oslo', 'Varsovia', 1063, 115, 446000, 'Aeropuerto de Gardermoen', 'Aeropuerto Chopin de Varsovia', 'SP-XDY'),
(609, 1, 'Varsovia', 'Reikiavik', 2772, 250, 873000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Keflavík', 'SP-LNX'),
(610, 2, 'Reikiavik', 'Varsovia', 2772, 250, 873000, 'Aeropuerto de Keflavík', 'Aeropuerto Chopin de Varsovia', 'SP-LNX'),
(611, 1, 'Venecia', 'Edimburgo', 1593, 160, 578000, 'Aeropuerto Marco Polo', 'Aeropuerto de Edimburgo', 'I-PWRW'),
(612, 2, 'Edimburgo', 'Venecia', 1593, 160, 578000, 'Aeropuerto de Edimburgo', 'Aeropuerto Marco Polo', 'I-PWRW'),
(613, 1, 'Viena', 'Atenas', 1284, 135, 501000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Eleftherios Venizelos', 'OE-NYD'),
(614, 2, 'Atenas', 'Viena', 1284, 135, 501000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Viena-Schwechat', 'OE-NYD'),
(615, 1, 'Viena', 'Copenhague', 870, 100, 398000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Kastrup', 'OE-MPY'),
(616, 2, 'Copenhague', 'Viena', 870, 100, 398000, 'Aeropuerto de Kastrup', 'Aeropuerto de Viena-Schwechat', 'OE-MPY'),
(617, 1, 'Viena', 'Estocolmo', 1241, 130, 490000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Arlanda', 'OE-RVL'),
(618, 2, 'Estocolmo', 'Viena', 1241, 130, 490000, 'Aeropuerto de Arlanda', 'Aeropuerto de Viena-Schwechat', 'OE-RVL'),
(619, 1, 'Viena', 'Florencia', 632, 85, 338000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Amerigo Vespucci', 'OE-VNM'),
(620, 2, 'Florencia', 'Viena', 632, 85, 338000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Viena-Schwechat', 'OE-VNM'),
(621, 1, 'Viena', 'Ginebra', 804, 95, 381000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Ginebra', 'OE-AMN'),
(622, 2, 'Ginebra', 'Viena', 804, 95, 381000, 'Aeropuerto de Ginebra', 'Aeropuerto de Viena-Schwechat', 'OE-AMN'),
(623, 1, 'Viena', 'Helsinki', 1439, 145, 540000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Helsinki-Vantaa', 'OE-MJS'),
(624, 2, 'Helsinki', 'Viena', 1439, 145, 540000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Viena-Schwechat', 'OE-MJS'),
(625, 1, 'Viena', 'Mánchester', 1427, 145, 537000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Mánchester', 'OE-AMP'),
(626, 2, 'Mánchester', 'Viena', 1427, 145, 537000, 'Aeropuerto de Mánchester', 'Aeropuerto de Viena-Schwechat', 'OE-AMP'),
(627, 1, 'Viena', 'Niza', 863, 100, 396000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto Costa Azul', 'OE-RBW'),
(628, 2, 'Niza', 'Viena', 863, 100, 396000, 'Aeropuerto Costa Azul', 'Aeropuerto de Viena-Schwechat', 'OE-RBW'),
(629, 1, 'Viena', 'Oslo', 1351, 140, 518000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Gardermoen', 'OE-AZK'),
(630, 2, 'Oslo', 'Viena', 1351, 140, 518000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Viena-Schwechat', 'OE-AZK'),
(631, 1, 'Viena', 'Reikiavik', 2888, 255, 902000, 'Aeropuerto de Viena-Schwechat', 'Aeropuerto de Keflavík', 'OE-UUB'),
(632, 2, 'Reikiavik', 'Viena', 2888, 255, 902000, 'Aeropuerto de Keflavík', 'Aeropuerto de Viena-Schwechat', 'OE-UUB'),
(633, 1, 'Zúrich', 'Atenas', 1618, 160, 584000, 'Aeropuerto de Zúrich', 'Aeropuerto Eleftherios Venizelos', 'HB-DLP'),
(634, 2, 'Atenas', 'Zúrich', 1618, 160, 584000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Zúrich', 'HB-DLP'),
(635, 1, 'Zúrich', 'Budapest', 789, 95, 377000, 'Aeropuerto de Zúrich', 'Aeropuerto Ferenc Liszt', 'HB-JNK'),
(636, 2, 'Budapest', 'Zúrich', 789, 95, 377000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Zúrich', 'HB-JNK'),
(637, 1, 'Zúrich', 'Dublín', 1238, 130, 490000, 'Aeropuerto de Zúrich', 'Aeropuerto de Dublín', 'HB-ZJA'),
(638, 2, 'Dublín', 'Zúrich', 1238, 130, 490000, 'Aeropuerto de Dublín', 'Aeropuerto de Zúrich', 'HB-ZJA'),
(639, 1, 'Zúrich', 'Edimburgo', 1247, 130, 492000, 'Aeropuerto de Zúrich', 'Aeropuerto de Edimburgo', 'HB-GGB'),
(640, 2, 'Edimburgo', 'Zúrich', 1247, 130, 492000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Zúrich', 'HB-GGB'),
(641, 1, 'Zúrich', 'Estambul', 1770, 170, 623000, 'Aeropuerto de Zúrich', 'Aeropuerto de Estambul', 'HB-CHF'),
(642, 2, 'Estambul', 'Zúrich', 1770, 170, 623000, 'Aeropuerto de Estambul', 'Aeropuerto de Zúrich', 'HB-CHF'),
(643, 1, 'Zúrich', 'Estocolmo', 1468, 150, 547000, 'Aeropuerto de Zúrich', 'Aeropuerto de Arlanda', 'HB-BZM'),
(644, 2, 'Estocolmo', 'Zúrich', 1468, 150, 547000, 'Aeropuerto de Arlanda', 'Aeropuerto de Zúrich', 'HB-BZM'),
(645, 1, 'Zúrich', 'Helsinki', 1774, 170, 623000, 'Aeropuerto de Zúrich', 'Aeropuerto de Helsinki-Vantaa', 'HB-WUR'),
(646, 2, 'Helsinki', 'Zúrich', 1774, 170, 623000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Zúrich', 'HB-WUR'),
(647, 1, 'Zúrich', 'Oslo', 1401, 145, 530000, 'Aeropuerto de Zúrich', 'Aeropuerto de Gardermoen', 'HB-DYW'),
(648, 2, 'Oslo', 'Zúrich', 1401, 145, 530000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Zúrich', 'HB-DYW'),
(649, 1, 'Zúrich', 'Reikiavik', 2619, 235, 835000, 'Aeropuerto de Zúrich', 'Aeropuerto de Keflavík', 'HB-UTX'),
(650, 2, 'Reikiavik', 'Zúrich', 2619, 235, 835000, 'Aeropuerto de Keflavík', 'Aeropuerto de Zúrich', 'HB-UTX'),
(651, 1, 'Zúrich', 'Varsovia', 1043, 115, 441000, 'Aeropuerto de Zúrich', 'Aeropuerto Chopin de Varsovia', 'HB-WBK'),
(652, 2, 'Varsovia', 'Zúrich', 1043, 115, 441000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Zúrich', 'HB-WBK'),
(653, 1, 'Ámsterdam', 'Atenas', 2164, 200, 721000, 'Aeropuerto de Schiphol', 'Aeropuerto Eleftherios Venizelos', 'PH-EDP'),
(654, 2, 'Atenas', 'Ámsterdam', 2164, 200, 721000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Schiphol', 'PH-EDP'),
(655, 1, 'Ámsterdam', 'Budapest', 1145, 125, 466000, 'Aeropuerto de Schiphol', 'Aeropuerto Ferenc Liszt', 'PH-HDZ'),
(656, 2, 'Budapest', 'Ámsterdam', 1145, 125, 466000, 'Aeropuerto Ferenc Liszt', 'Aeropuerto de Schiphol', 'PH-HDZ'),
(657, 1, 'Ámsterdam', 'Dublín', 756, 95, 369000, 'Aeropuerto de Schiphol', 'Aeropuerto de Dublín', 'PH-CDC'),
(658, 2, 'Dublín', 'Ámsterdam', 756, 95, 369000, 'Aeropuerto de Dublín', 'Aeropuerto de Schiphol', 'PH-CDC'),
(659, 1, 'Ámsterdam', 'Edimburgo', 660, 85, 345000, 'Aeropuerto de Schiphol', 'Aeropuerto de Edimburgo', 'PH-VNB'),
(660, 2, 'Edimburgo', 'Ámsterdam', 660, 85, 345000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Schiphol', 'PH-VNB'),
(661, 1, 'Ámsterdam', 'Estambul', 2212, 205, 733000, 'Aeropuerto de Schiphol', 'Aeropuerto de Estambul', 'PH-ERU'),
(662, 2, 'Estambul', 'Ámsterdam', 2212, 205, 733000, 'Aeropuerto de Estambul', 'Aeropuerto de Schiphol', 'PH-ERU'),
(663, 1, 'Ámsterdam', 'Estocolmo', 1125, 120, 461000, 'Aeropuerto de Schiphol', 'Aeropuerto de Arlanda', 'PH-AKP'),
(664, 2, 'Estocolmo', 'Ámsterdam', 1125, 120, 461000, 'Aeropuerto de Arlanda', 'Aeropuerto de Schiphol', 'PH-AKP'),
(665, 1, 'Ámsterdam', 'Florencia', 1066, 115, 446000, 'Aeropuerto de Schiphol', 'Aeropuerto Amerigo Vespucci', 'PH-MUY'),
(666, 2, 'Florencia', 'Ámsterdam', 1066, 115, 446000, 'Aeropuerto Amerigo Vespucci', 'Aeropuerto de Schiphol', 'PH-MUY'),
(667, 1, 'Ámsterdam', 'Ginebra', 692, 90, 353000, 'Aeropuerto de Schiphol', 'Aeropuerto de Ginebra', 'PH-RGB'),
(668, 2, 'Ginebra', 'Ámsterdam', 692, 90, 353000, 'Aeropuerto de Ginebra', 'Aeropuerto de Schiphol', 'PH-RGB'),
(669, 1, 'Ámsterdam', 'Helsinki', 1501, 150, 555000, 'Aeropuerto de Schiphol', 'Aeropuerto de Helsinki-Vantaa', 'PH-PKE'),
(670, 2, 'Helsinki', 'Ámsterdam', 1501, 150, 555000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Schiphol', 'PH-PKE'),
(671, 1, 'Ámsterdam', 'Lisboa', 1864, 180, 646000, 'Aeropuerto de Schiphol', 'Aeropuerto Humberto Delgado', 'PH-JXU'),
(672, 2, 'Lisboa', 'Ámsterdam', 1864, 180, 646000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Schiphol', 'PH-JXU'),
(673, 1, 'Ámsterdam', 'Mánchester', 494, 75, 304000, 'Aeropuerto de Schiphol', 'Aeropuerto de Mánchester', 'PH-FWN'),
(674, 2, 'Mánchester', 'Ámsterdam', 494, 75, 304000, 'Aeropuerto de Mánchester', 'Aeropuerto de Schiphol', 'PH-FWN'),
(675, 1, 'Ámsterdam', 'Múnich', 668, 85, 347000, 'Aeropuerto de Schiphol', 'Aeropuerto de Múnich', 'PH-ZBC'),
(676, 2, 'Múnich', 'Ámsterdam', 668, 85, 347000, 'Aeropuerto de Múnich', 'Aeropuerto de Schiphol', 'PH-ZBC'),
(677, 1, 'Ámsterdam', 'Niza', 980, 110, 425000, 'Aeropuerto de Schiphol', 'Aeropuerto Costa Azul', 'PH-RVP'),
(678, 2, 'Niza', 'Ámsterdam', 980, 110, 425000, 'Aeropuerto Costa Azul', 'Aeropuerto de Schiphol', 'PH-RVP'),
(679, 1, 'Ámsterdam', 'Oslo', 913, 105, 408000, 'Aeropuerto de Schiphol', 'Aeropuerto de Gardermoen', 'PH-SRX'),
(680, 2, 'Oslo', 'Ámsterdam', 913, 105, 408000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Schiphol', 'PH-SRX'),
(681, 1, 'Ámsterdam', 'Praga', 711, 90, 358000, 'Aeropuerto de Schiphol', 'Aeropuerto Václav Havel', 'PH-XEA'),
(682, 2, 'Praga', 'Ámsterdam', 711, 90, 358000, 'Aeropuerto Václav Havel', 'Aeropuerto de Schiphol', 'PH-XEA'),
(683, 1, 'Ámsterdam', 'Reikiavik', 2018, 190, 684000, 'Aeropuerto de Schiphol', 'Aeropuerto de Keflavík', 'PH-KDE'),
(684, 2, 'Reikiavik', 'Ámsterdam', 2018, 190, 684000, 'Aeropuerto de Keflavík', 'Aeropuerto de Schiphol', 'PH-KDE'),
(685, 1, 'Ámsterdam', 'Varsovia', 1093, 120, 453000, 'Aeropuerto de Schiphol', 'Aeropuerto Chopin de Varsovia', 'PH-PGJ'),
(686, 2, 'Varsovia', 'Ámsterdam', 1093, 120, 453000, 'Aeropuerto Chopin de Varsovia', 'Aeropuerto de Schiphol', 'PH-PGJ'),
(687, 1, 'Ámsterdam', 'Venecia', 941, 105, 415000, 'Aeropuerto de Schiphol', 'Aeropuerto Marco Polo', 'PH-RTW'),
(688, 2, 'Venecia', 'Ámsterdam', 941, 105, 415000, 'Aeropuerto Marco Polo', 'Aeropuerto de Schiphol', 'PH-RTW'),
(689, 1, 'Berlín', 'Copenhague', 444, 180, 224000, 'Estación Berlin Hbf', 'Estación København H', 'ICE-110'),
(690, 2, 'Copenhague', 'Berlín', 444, 180, 224000, 'Estación København H', 'Estación Berlin Hbf', 'ICE-110'),
(691, 1, 'Berlín', 'Praga', 351, 145, 190000, 'Estación Berlin Hbf', 'Estación Praha hlavní nádraží', 'ICE-124'),
(692, 2, 'Praga', 'Berlín', 351, 145, 190000, 'Estación Praha hlavní nádraží', 'Estación Berlin Hbf', 'ICE-124'),
(693, 1, 'Florencia', 'Niza', 401, 165, 208000, 'Estación Florencia Santa María Novella', 'Estación Nice-Ville', 'FR-121'),
(694, 2, 'Niza', 'Florencia', 401, 165, 208000, 'Estación Nice-Ville', 'Estación Florencia Santa María Novella', 'FR-121'),
(695, 1, 'Fráncfort', 'Berlín', 529, 215, 256000, 'Estación Frankfurt Hbf', 'Estación Berlin Hbf', 'ICE-135'),
(696, 2, 'Berlín', 'Fráncfort', 529, 215, 256000, 'Estación Berlin Hbf', 'Estación Frankfurt Hbf', 'ICE-135'),
(697, 1, 'Lisboa', 'Oporto', 342, 145, 187000, 'Estación Lisboa Santa Apolónia', 'Estación Porto Campanhã', 'AP-119'),
(698, 2, 'Oporto', 'Lisboa', 342, 145, 187000, 'Estación Porto Campanhã', 'Estación Lisboa Santa Apolónia', 'AP-119'),
(699, 1, 'Londres', 'Bruselas', 401, 175, 208000, 'Estación Londres St Pancras', 'Estación Bruselas-Midi', 'LNER-102'),
(700, 2, 'Bruselas', 'Londres', 401, 175, 208000, 'Estación Bruselas-Midi', 'Estación Londres St Pancras', 'LNER-102'),
(701, 1, 'Londres', 'Ámsterdam', 447, 195, 225000, 'Estación Londres St Pancras', 'Estación Amsterdam Centraal', 'LNER-109'),
(702, 2, 'Ámsterdam', 'Londres', 447, 195, 225000, 'Estación Amsterdam Centraal', 'Estación Londres St Pancras', 'LNER-109'),
(703, 1, 'Madrid', 'Oporto', 527, 210, 255000, 'Estación Madrid Puerta de Atocha', 'Estación Porto Campanhã', 'AVE-113'),
(704, 2, 'Oporto', 'Madrid', 527, 210, 255000, 'Estación Porto Campanhã', 'Estación Madrid Puerta de Atocha', 'AVE-113'),
(705, 1, 'Milán', 'Florencia', 312, 130, 175000, 'Estación Milano Centrale', 'Estación Florencia Santa María Novella', 'FR-151'),
(706, 2, 'Florencia', 'Milán', 312, 130, 175000, 'Estación Florencia Santa María Novella', 'Estación Milano Centrale', 'FR-151'),
(707, 1, 'Milán', 'Ginebra', 313, 130, 176000, 'Estación Milano Centrale', 'Estación Genève-Cornavin', 'FR-176'),
(708, 2, 'Ginebra', 'Milán', 313, 130, 176000, 'Estación Genève-Cornavin', 'Estación Milano Centrale', 'FR-176'),
(709, 1, 'Milán', 'Zúrich', 274, 120, 161000, 'Estación Milano Centrale', 'Estación Zürich HB', 'FR-181'),
(710, 2, 'Zúrich', 'Milán', 274, 120, 161000, 'Estación Zürich HB', 'Estación Milano Centrale', 'FR-181'),
(711, 1, 'Múnich', 'Ginebra', 580, 235, 275000, 'Estación München Hbf', 'Estación Genève-Cornavin', 'ICE-159'),
(712, 2, 'Ginebra', 'Múnich', 580, 235, 275000, 'Estación Genève-Cornavin', 'Estación München Hbf', 'ICE-159'),
(713, 1, 'Múnich', 'Venecia', 382, 160, 201000, 'Estación München Hbf', 'Estación Venecia Santa Lucía', 'ICE-185'),
(714, 2, 'Venecia', 'Múnich', 382, 160, 201000, 'Estación Venecia Santa Lucía', 'Estación München Hbf', 'ICE-185'),
(715, 1, 'Múnich', 'Zúrich', 303, 130, 172000, 'Estación München Hbf', 'Estación Zürich HB', 'ICE-190'),
(716, 2, 'Zúrich', 'Múnich', 303, 130, 172000, 'Estación Zürich HB', 'Estación München Hbf', 'ICE-190'),
(717, 1, 'París', 'Ginebra', 513, 205, 250000, 'Estación París Gare de Lyon', 'Estación Genève-Cornavin', 'TGV-101'),
(718, 2, 'Ginebra', 'París', 513, 205, 250000, 'Estación Genève-Cornavin', 'Estación París Gare de Lyon', 'TGV-101'),
(719, 1, 'Praga', 'Budapest', 553, 220, 265000, 'Estación Praha hlavní nádraží', 'Estación Budapest-Keleti', 'CD-124'),
(720, 2, 'Budapest', 'Praga', 553, 220, 265000, 'Estación Budapest-Keleti', 'Estación Praha hlavní nádraží', 'CD-124'),
(721, 1, 'Roma', 'Niza', 589, 235, 278000, 'Estación Roma Termini', 'Estación Nice-Ville', 'FR-218'),
(722, 2, 'Niza', 'Roma', 589, 235, 278000, 'Estación Nice-Ville', 'Estación Roma Termini', 'FR-218'),
(723, 1, 'Viena', 'Venecia', 544, 220, 261000, 'Estación Wien Hauptbahnhof', 'Estación Venecia Santa Lucía', 'RJ-106'),
(724, 2, 'Venecia', 'Viena', 544, 220, 261000, 'Estación Venecia Santa Lucía', 'Estación Wien Hauptbahnhof', 'RJ-106'),
(725, 1, 'Zúrich', 'Ginebra', 281, 120, 164000, 'Estación Zürich HB', 'Estación Genève-Cornavin', 'SBB-118'),
(726, 2, 'Ginebra', 'Zúrich', 281, 120, 164000, 'Estación Genève-Cornavin', 'Estación Zürich HB', 'SBB-118'),
(727, 1, 'Zúrich', 'Niza', 526, 210, 255000, 'Estación Zürich HB', 'Estación Nice-Ville', 'SBB-154'),
(728, 2, 'Niza', 'Zúrich', 526, 210, 255000, 'Estación Nice-Ville', 'Estación Zürich HB', 'SBB-154'),
(729, 1, 'Barcelona', 'Ginebra', 778, 640, 166000, 'Estación de Autobuses Barcelona Nord', 'Terminal de autobuses de Ginebra', 'FLX-8015'),
(730, 2, 'Ginebra', 'Barcelona', 778, 640, 166000, 'Terminal de autobuses de Ginebra', 'Estación de Autobuses Barcelona Nord', 'FLX-8015'),
(731, 1, 'Berlín', 'Bruselas', 813, 670, 172000, 'Terminal Berlin ZOB', 'Terminal Bruselas-Norte', 'FLX-8028'),
(732, 2, 'Bruselas', 'Berlín', 813, 670, 172000, 'Terminal Bruselas-Norte', 'Terminal Berlin ZOB', 'FLX-8028'),
(733, 1, 'Berlín', 'Viena', 654, 545, 144000, 'Terminal Berlin ZOB', 'Terminal Wien Erdberg', 'FLX-8050'),
(734, 2, 'Viena', 'Berlín', 654, 545, 144000, 'Terminal Wien Erdberg', 'Terminal Berlin ZOB', 'FLX-8050'),
(735, 1, 'Berlín', 'Zúrich', 836, 690, 176000, 'Terminal Berlin ZOB', 'Terminal Zürich Sihlquai', 'FLX-8067'),
(736, 2, 'Zúrich', 'Berlín', 836, 690, 176000, 'Terminal Zürich Sihlquai', 'Terminal Berlin ZOB', 'FLX-8067'),
(737, 1, 'Copenhague', 'Estocolmo', 652, 540, 144000, 'Terminal Copenhague Ingerslevsgade', 'Terminal Cityterminalen', 'FLX-8073'),
(738, 2, 'Estocolmo', 'Copenhague', 652, 540, 144000, 'Terminal Cityterminalen', 'Terminal Copenhague Ingerslevsgade', 'FLX-8073'),
(739, 1, 'Edimburgo', 'Mánchester', 352, 300, 92000, 'Edinburgh Bus Station', 'Manchester Coach Station', 'FLX-8093'),
(740, 2, 'Mánchester', 'Edimburgo', 352, 300, 92000, 'Manchester Coach Station', 'Edinburgh Bus Station', 'FLX-8093'),
(741, 1, 'Estocolmo', 'Oslo', 521, 435, 121000, 'Terminal Cityterminalen', 'Terminal Oslo Bussterminal', 'FLX-8128'),
(742, 2, 'Oslo', 'Estocolmo', 521, 435, 121000, 'Terminal Oslo Bussterminal', 'Terminal Cityterminalen', 'FLX-8128'),
(743, 1, 'Fráncfort', 'Copenhague', 840, 690, 177000, 'Terminal Frankfurt Hbf Süd', 'Terminal Copenhague Ingerslevsgade', 'FLX-8164'),
(744, 2, 'Copenhague', 'Fráncfort', 840, 690, 177000, 'Terminal Copenhague Ingerslevsgade', 'Terminal Frankfurt Hbf Süd', 'FLX-8164'),
(745, 1, 'Fráncfort', 'Ginebra', 592, 495, 134000, 'Terminal Frankfurt Hbf Süd', 'Terminal de autobuses de Ginebra', 'FLX-8172'),
(746, 2, 'Ginebra', 'Fráncfort', 592, 495, 134000, 'Terminal de autobuses de Ginebra', 'Terminal Frankfurt Hbf Süd', 'FLX-8172'),
(747, 1, 'Fráncfort', 'Praga', 513, 430, 120000, 'Terminal Frankfurt Hbf Süd', 'Terminal Praha Florenc', 'FLX-8207'),
(748, 2, 'Praga', 'Fráncfort', 513, 430, 120000, 'Terminal Praha Florenc', 'Terminal Frankfurt Hbf Süd', 'FLX-8207'),
(749, 1, 'Fráncfort', 'Venecia', 733, 605, 158000, 'Terminal Frankfurt Hbf Süd', 'Terminal Piazzale Roma', 'FLX-8212'),
(750, 2, 'Venecia', 'Fráncfort', 733, 605, 158000, 'Terminal Piazzale Roma', 'Terminal Frankfurt Hbf Süd', 'FLX-8212'),
(751, 1, 'Ginebra', 'Niza', 365, 310, 94000, 'Terminal de autobuses de Ginebra', 'Estación de autobuses de Niza', 'FLX-8227'),
(752, 2, 'Niza', 'Ginebra', 365, 310, 94000, 'Estación de autobuses de Niza', 'Terminal de autobuses de Ginebra', 'FLX-8227'),
(753, 1, 'Ginebra', 'Venecia', 608, 505, 136000, 'Terminal de autobuses de Ginebra', 'Terminal Piazzale Roma', 'FLX-8257'),
(754, 2, 'Venecia', 'Ginebra', 608, 505, 136000, 'Terminal Piazzale Roma', 'Terminal de autobuses de Ginebra', 'FLX-8257'),
(755, 1, 'Londres', 'Edimburgo', 666, 555, 147000, 'Victoria Coach Station', 'Edinburgh Bus Station', 'FLX-8293'),
(756, 2, 'Edimburgo', 'Londres', 666, 555, 147000, 'Edinburgh Bus Station', 'Victoria Coach Station', 'FLX-8293'),
(757, 1, 'Milán', 'Múnich', 436, 370, 106000, 'Terminal Milano Lampugnano', 'Terminal München ZOB', 'FLX-8316'),
(758, 2, 'Múnich', 'Milán', 436, 370, 106000, 'Terminal München ZOB', 'Terminal Milano Lampugnano', 'FLX-8316'),
(759, 1, 'Milán', 'Niza', 310, 270, 84000, 'Terminal Milano Lampugnano', 'Estación de autobuses de Niza', 'FLX-8325'),
(760, 2, 'Niza', 'Milán', 310, 270, 84000, 'Estación de autobuses de Niza', 'Terminal Milano Lampugnano', 'FLX-8325'),
(761, 1, 'Milán', 'Praga', 808, 665, 171000, 'Terminal Milano Lampugnano', 'Terminal Praha Florenc', 'FLX-8336'),
(762, 2, 'Praga', 'Milán', 808, 665, 171000, 'Terminal Praha Florenc', 'Terminal Milano Lampugnano', 'FLX-8336'),
(763, 1, 'Múnich', 'Berlín', 630, 525, 140000, 'Terminal München ZOB', 'Terminal Berlin ZOB', 'FLX-8346'),
(764, 2, 'Berlín', 'Múnich', 630, 525, 140000, 'Terminal Berlin ZOB', 'Terminal München ZOB', 'FLX-8346'),
(765, 1, 'Múnich', 'Florencia', 608, 505, 136000, 'Terminal München ZOB', 'Terminal Villa Costanza', 'FLX-8359'),
(766, 2, 'Florencia', 'Múnich', 608, 505, 136000, 'Terminal Villa Costanza', 'Terminal München ZOB', 'FLX-8359'),
(767, 1, 'París', 'Fráncfort', 597, 500, 135000, 'Terminal París Bercy', 'Terminal Frankfurt Hbf Süd', 'FLX-8395'),
(768, 2, 'Fráncfort', 'París', 597, 500, 135000, 'Terminal Frankfurt Hbf Süd', 'Terminal París Bercy', 'FLX-8395'),
(769, 1, 'París', 'Milán', 800, 660, 170000, 'Terminal París Bercy', 'Terminal Milano Lampugnano', 'FLX-8412'),
(770, 2, 'Milán', 'París', 800, 660, 170000, 'Terminal Milano Lampugnano', 'Terminal París Bercy', 'FLX-8412'),
(771, 1, 'París', 'Zúrich', 610, 510, 137000, 'Terminal París Bercy', 'Terminal Zürich Sihlquai', 'FLX-8420'),
(772, 2, 'Zúrich', 'París', 610, 510, 137000, 'Terminal Zürich Sihlquai', 'Terminal París Bercy', 'FLX-8420'),
(773, 1, 'Praga', 'Copenhague', 794, 655, 169000, 'Terminal Praha Florenc', 'Terminal Copenhague Ingerslevsgade', 'FLX-8445'),
(774, 2, 'Copenhague', 'Praga', 794, 655, 169000, 'Terminal Copenhague Ingerslevsgade', 'Terminal Praha Florenc', 'FLX-8445'),
(775, 1, 'Praga', 'Varsovia', 646, 535, 143000, 'Terminal Praha Florenc', 'Terminal Warszawa Zachodnia', 'FLX-8462'),
(776, 2, 'Varsovia', 'Praga', 646, 535, 143000, 'Terminal Warszawa Zachodnia', 'Terminal Praha Florenc', 'FLX-8462'),
(777, 1, 'Praga', 'Venecia', 675, 560, 148000, 'Terminal Praha Florenc', 'Terminal Piazzale Roma', 'FLX-8493'),
(778, 2, 'Venecia', 'Praga', 675, 560, 148000, 'Terminal Piazzale Roma', 'Terminal Praha Florenc', 'FLX-8493'),
(779, 1, 'Roma', 'Milán', 596, 495, 134000, 'Terminal Roma Tiburtina', 'Terminal Milano Lampugnano', 'FLX-8517'),
(780, 2, 'Milán', 'Roma', 596, 495, 134000, 'Terminal Milano Lampugnano', 'Terminal Roma Tiburtina', 'FLX-8517'),
(781, 1, 'Roma', 'Venecia', 492, 415, 116000, 'Terminal Roma Tiburtina', 'Terminal Piazzale Roma', 'FLX-8530'),
(782, 2, 'Venecia', 'Roma', 492, 415, 116000, 'Terminal Piazzale Roma', 'Terminal Roma Tiburtina', 'FLX-8530'),
(783, 1, 'Varsovia', 'Copenhague', 840, 690, 177000, 'Terminal Warszawa Zachodnia', 'Terminal Copenhague Ingerslevsgade', 'FLX-8543'),
(784, 2, 'Copenhague', 'Varsovia', 840, 690, 177000, 'Terminal Copenhague Ingerslevsgade', 'Terminal Warszawa Zachodnia', 'FLX-8543'),
(785, 1, 'Venecia', 'Florencia', 255, 225, 75000, 'Terminal Piazzale Roma', 'Terminal Villa Costanza', 'FLX-8555'),
(786, 2, 'Florencia', 'Venecia', 255, 225, 75000, 'Terminal Villa Costanza', 'Terminal Piazzale Roma', 'FLX-8555'),
(787, 1, 'Viena', 'Praga', 314, 270, 85000, 'Terminal Wien Erdberg', 'Terminal Praha Florenc', 'FLX-8579'),
(788, 2, 'Praga', 'Viena', 314, 270, 85000, 'Terminal Praha Florenc', 'Terminal Wien Erdberg', 'FLX-8579'),
(789, 1, 'Viena', 'Varsovia', 694, 575, 152000, 'Terminal Wien Erdberg', 'Terminal Warszawa Zachodnia', 'FLX-8610'),
(790, 2, 'Varsovia', 'Viena', 694, 575, 152000, 'Terminal Warszawa Zachodnia', 'Terminal Wien Erdberg', 'FLX-8610'),
(791, 1, 'Viena', 'Zúrich', 740, 610, 159000, 'Terminal Wien Erdberg', 'Terminal Zürich Sihlquai', 'FLX-8615'),
(792, 2, 'Zúrich', 'Viena', 740, 610, 159000, 'Terminal Zürich Sihlquai', 'Terminal Wien Erdberg', 'FLX-8615'),
(793, 1, 'Zúrich', 'Praga', 658, 545, 145000, 'Terminal Zürich Sihlquai', 'Terminal Praha Florenc', 'FLX-8624'),
(794, 2, 'Praga', 'Zúrich', 658, 545, 145000, 'Terminal Praha Florenc', 'Terminal Zürich Sihlquai', 'FLX-8624'),
(795, 1, 'Ámsterdam', 'Bruselas', 217, 195, 68000, 'Terminal Amsterdam Sloterdijk', 'Terminal Bruselas-Norte', 'FLX-8643'),
(796, 2, 'Bruselas', 'Ámsterdam', 217, 195, 68000, 'Terminal Bruselas-Norte', 'Terminal Amsterdam Sloterdijk', 'FLX-8643'),
(797, 1, 'Ámsterdam', 'Zúrich', 766, 635, 164000, 'Terminal Amsterdam Sloterdijk', 'Terminal Zürich Sihlquai', 'FLX-8678'),
(798, 2, 'Zúrich', 'Ámsterdam', 766, 635, 164000, 'Terminal Zürich Sihlquai', 'Terminal Amsterdam Sloterdijk', 'FLX-8678');

INSERT INTO Ruta (distancia, duracionEstimada, precioBase, idCiudadOrigen, direccionOrigen, idCiudadDestino, direccionDestino)
SELECT t.distancia, t.duracion, t.precio, co.idCiudad, t.dirOrigen, cd.idCiudad, t.dirDestino
FROM tmp_ruta t
JOIN Ciudad co ON co.nombreCiudad = t.origen
JOIN Ciudad cd ON cd.nombreCiudad = t.destino
ORDER BY t.orden;

INSERT INTO RutaVehiculo (idRuta, idVehiculo)
SELECT r.idRuta, v.idVehiculo
FROM tmp_ruta t
JOIN Ciudad co ON co.nombreCiudad = t.origen
JOIN Ciudad cd ON cd.nombreCiudad = t.destino
JOIN Ruta r ON r.idCiudadOrigen = co.idCiudad AND r.idCiudadDestino = cd.idCiudad
           AND r.direccionOrigen = t.dirOrigen
JOIN Vehiculo v ON v.codigoIdentificacion = t.vehiculo;

-- ---------------- Programación de viajes (01-dic-2026 al 31-ene-2027) ----------------
-- Cada ruta sale todos los días. Como un vehículo cubre ida y vuelta, los horarios
-- se alternan para que nunca esté en dos lugares a la vez (la ruta más larga de cada
-- tipo cabe entre una salida y la siguiente):
--   AVION: ida 06:30 y 15:30 / vuelta 11:00 y 20:00
--   TREN : ida 07:00 y 15:00 / vuelta 11:00 y 19:00
--   BUS  : ida 07:00        / vuelta 19:00
-- Para cambiar el periodo u horas, edita "dias" y "horarios" y vuelve a ejecutar este bloque.
INSERT INTO ProgramacionViaje (fechaHoraSalida, idRuta)
WITH RECURSIVE dias (dia) AS (
    SELECT DATE '2026-12-01'
    UNION ALL
    SELECT dia + INTERVAL 1 DAY FROM dias WHERE dia < DATE '2027-01-31'
),
horarios (tipo, sentido, hora) AS (
              SELECT 'AVION', 1, TIME '06:30:00'
    UNION ALL SELECT 'AVION', 1, TIME '15:30:00'
    UNION ALL SELECT 'AVION', 2, TIME '11:00:00'
    UNION ALL SELECT 'AVION', 2, TIME '20:00:00'
    UNION ALL SELECT 'TREN',  1, TIME '07:00:00'
    UNION ALL SELECT 'TREN',  1, TIME '15:00:00'
    UNION ALL SELECT 'TREN',  2, TIME '11:00:00'
    UNION ALL SELECT 'TREN',  2, TIME '19:00:00'
    UNION ALL SELECT 'BUS',   1, TIME '07:00:00'
    UNION ALL SELECT 'BUS',   2, TIME '19:00:00'
)
SELECT TIMESTAMP(d.dia, h.hora), r.idRuta
FROM tmp_ruta t
JOIN Ciudad co        ON co.nombreCiudad = t.origen
JOIN Ciudad cd        ON cd.nombreCiudad = t.destino
JOIN Ruta r           ON r.idCiudadOrigen = co.idCiudad AND r.idCiudadDestino = cd.idCiudad
                     AND r.direccionOrigen = t.dirOrigen
JOIN Vehiculo v       ON v.codigoIdentificacion = t.vehiculo
JOIN TipoTransporte tt ON tt.idTipoTransporte = v.idTipoTransporte
JOIN horarios h       ON h.tipo = tt.nombreTipoTransporte AND h.sentido = t.sentido
CROSS JOIN dias d
ORDER BY 1, 2;

DROP TEMPORARY TABLE tmp_ruta;

-- ---------------- Plantilla: repetir UNA ruta cada N horas ----------------
-- Cambia el id de la ruta, el inicio, el fin y el intervalo. Ejemplo: ruta 1,
-- cada 8 horas durante febrero. (También existe el endpoint
-- POST /api/programaciones/recurrente que hace lo mismo desde la API.)
--
-- INSERT INTO ProgramacionViaje (fechaHoraSalida, idRuta)
-- WITH RECURSIVE salidas (salida) AS (
--     SELECT TIMESTAMP '2027-02-01 06:00:00'
--     UNION ALL
--     SELECT salida + INTERVAL 8 HOUR FROM salidas
--     WHERE salida + INTERVAL 8 HOUR <= TIMESTAMP '2027-02-28 23:59:00'
-- )
-- SELECT salida, 1 FROM salidas;

-- Resumen esperado:
--   SELECT COUNT(*) FROM Ciudad;             -- 190
--   SELECT COUNT(*) FROM Sede;               -- 10
--   SELECT COUNT(*) FROM Empleado;           -- 10 (+ la admin cuando arranque la app)
--   SELECT COUNT(*) FROM Cliente;            -- 4
--   SELECT COUNT(*) FROM Ruta;               -- 798
--   SELECT COUNT(*) FROM ProgramacionViaje;  -- 93868 (1514 salidas/día x 62 días)