-- =====================================================================
--  Datos iniciales. Ejecutar DESPUÉS de 01_esquema.sql.
--  Incluye: catálogos, ubicación (ciudades de UBICACIONES + EUROPEAN_CITIES),
--  10 sedes en Colombia (central en Medellín, idSede = 1), 10 asesores de
--  ventas (uno por sede), 4 clientes de prueba, vehículos, 44 rutas europeas
--  (22 pares ida/vuelta) y su programación del 01-dic-2026 al 31-ene-2027.
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
-- Europa (EUROPEAN_CITIES; Madrid y Barcelona ya están arriba en España)
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
) x JOIN TipoTransporte t ON t.nombreTipoTransporte = x.tipo;

-- ---------------- Rutas europeas (22 pares ida/vuelta = 44 rutas) ----------------
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
(44, 2, 'Niza', 'Barcelona', 660, 600, 160000, 'Estación de autobuses de Niza', 'Estación de Autobuses Barcelona Nord', 'FLX-7731');

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
--   SELECT COUNT(*) FROM Ruta;               -- 44
--   SELECT COUNT(*) FROM ProgramacionViaje;  -- 4712 (76 salidas/día x 62 días)