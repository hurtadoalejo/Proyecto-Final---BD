-- =====================================================================
--  Datos iniciales. Ejecutar DESPUÉS de 01_esquema.sql.
--  Usuarios: no se insertan aquí. Al arrancar, la aplicación crea la
--  administradora (admin@viajacol.com); los
--  clientes se registran desde el frontend (POST /api/auth/registro).
-- =====================================================================
USE turismo_db;

-- ---------------- Catálogos ----------------
INSERT INTO Rol (nombreRol) VALUES ('ADMIN'), ('EMPLEADO'), ('CLIENTE');
INSERT INTO TipoIdentificacion (nombreIdentificacion) VALUES ('CC'), ('CE'), ('TI'), ('PA'), ('PEP');
INSERT INTO Cargo (nombreCargo) VALUES ('Administrador del sistema'), ('Asesor de ventas'), ('Agente de servicio al cliente'), ('Coordinador de operaciones');
INSERT INTO EstadoNotificacion (nombreEstadoNotificacion) VALUES ('PENDIENTE'), ('ENVIADO'), ('FALLIDO');
INSERT INTO EstadoReserva (nombreEstadoReserva) VALUES ('PENDIENTE'), ('CONFIRMADA'), ('CANCELADA');
INSERT INTO EstadoTransaccion (nombreEstadoTransaccion) VALUES ('PENDIENTE'), ('APROBADA'), ('RECHAZADA');
INSERT INTO MetodoPago (nombreMetodoPago) VALUES ('TARJETA_CREDITO'), ('TARJETA_DEBITO'), ('PSE');
INSERT INTO TipoTransporte (nombreTipoTransporte) VALUES ('AVION'), ('BUS'), ('TREN');
INSERT INTO TipoTrayecto (nombreTipoTrayecto) VALUES ('IDA'), ('VUELTA');

-- ---------------- Ubicación ----------------
-- Incluye los 8 países, 80 departamentos y 160 ciudades del formulario de registro del frontend.
INSERT INTO Pais (nombrePais, indicativo) VALUES ('Colombia', 57), ('España', 34), ('Francia', 33), ('Reino Unido', 44), ('Italia', 39), ('Países Bajos', 31), ('Alemania', 49), ('Portugal', 351), ('Bélgica', 32), ('Austria', 43), ('Suiza', 41), ('Grecia', 30), ('Irlanda', 353), ('República Checa', 420), ('Hungría', 36), ('Polonia', 48), ('Dinamarca', 45), ('Suecia', 46), ('Noruega', 47), ('Finlandia', 358), ('Turquía', 90), ('Islandia', 354), ('Estados Unidos', 1), ('México', 52), ('Argentina', 54), ('Chile', 56), ('Perú', 51), ('Ecuador', 593);
INSERT INTO Departamento (nombreDepartamento, idPais)
SELECT x.nombre, p.idPais FROM (
SELECT 'Amazonas' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Antioquia' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Arauca' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Atlántico' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Bogotá D.C.' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Bolívar' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Boyacá' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Caldas' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Caquetá' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Casanare' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Cauca' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Cesar' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Chocó' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Córdoba' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Cundinamarca' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Guainía' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Guaviare' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Huila' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'La Guajira' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Magdalena' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Meta' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Nariño' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Norte de Santander' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Putumayo' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Quindío' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Risaralda' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'San Andrés y Providencia' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Santander' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Sucre' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Tolima' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Valle del Cauca' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Vaupés' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Vichada' AS nombre, 'Colombia' AS pais
  UNION ALL SELECT 'Comunidad de Madrid' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Cataluña' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Isla de Francia' AS nombre, 'Francia' AS pais
  UNION ALL SELECT 'Provenza-Alpes-Costa Azul' AS nombre, 'Francia' AS pais
  UNION ALL SELECT 'Inglaterra' AS nombre, 'Reino Unido' AS pais
  UNION ALL SELECT 'Escocia' AS nombre, 'Reino Unido' AS pais
  UNION ALL SELECT 'Lacio' AS nombre, 'Italia' AS pais
  UNION ALL SELECT 'Lombardía' AS nombre, 'Italia' AS pais
  UNION ALL SELECT 'Véneto' AS nombre, 'Italia' AS pais
  UNION ALL SELECT 'Toscana' AS nombre, 'Italia' AS pais
  UNION ALL SELECT 'Holanda Septentrional' AS nombre, 'Países Bajos' AS pais
  UNION ALL SELECT 'Hesse' AS nombre, 'Alemania' AS pais
  UNION ALL SELECT 'Baviera' AS nombre, 'Alemania' AS pais
  UNION ALL SELECT 'Berlín' AS nombre, 'Alemania' AS pais
  UNION ALL SELECT 'Lisboa' AS nombre, 'Portugal' AS pais
  UNION ALL SELECT 'Norte' AS nombre, 'Portugal' AS pais
  UNION ALL SELECT 'Región de Bruselas' AS nombre, 'Bélgica' AS pais
  UNION ALL SELECT 'Viena' AS nombre, 'Austria' AS pais
  UNION ALL SELECT 'Zúrich' AS nombre, 'Suiza' AS pais
  UNION ALL SELECT 'Ginebra' AS nombre, 'Suiza' AS pais
  UNION ALL SELECT 'Ática' AS nombre, 'Grecia' AS pais
  UNION ALL SELECT 'Leinster' AS nombre, 'Irlanda' AS pais
  UNION ALL SELECT 'Praga' AS nombre, 'República Checa' AS pais
  UNION ALL SELECT 'Budapest' AS nombre, 'Hungría' AS pais
  UNION ALL SELECT 'Mazovia' AS nombre, 'Polonia' AS pais
  UNION ALL SELECT 'Región Capital' AS nombre, 'Dinamarca' AS pais
  UNION ALL SELECT 'Estocolmo' AS nombre, 'Suecia' AS pais
  UNION ALL SELECT 'Oslo' AS nombre, 'Noruega' AS pais
  UNION ALL SELECT 'Uusimaa' AS nombre, 'Finlandia' AS pais
  UNION ALL SELECT 'Estambul' AS nombre, 'Turquía' AS pais
  UNION ALL SELECT 'Región Capital' AS nombre, 'Islandia' AS pais
  UNION ALL SELECT 'California' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Texas' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Florida' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Nueva York' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Illinois' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Pensilvania' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Ohio' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Georgia' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Carolina del Norte' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Míchigan' AS nombre, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Andalucía' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Comunidad Valenciana' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Galicia' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Castilla y León' AS nombre, 'España' AS pais
  UNION ALL SELECT 'País Vasco' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Canarias' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Castilla-La Mancha' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Aragón' AS nombre, 'España' AS pais
  UNION ALL SELECT 'Ciudad de México' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Jalisco' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Nuevo León' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Puebla' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Guanajuato' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Estado de México' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Veracruz' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Yucatán' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Quintana Roo' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Baja California' AS nombre, 'México' AS pais
  UNION ALL SELECT 'Buenos Aires' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Ciudad Autónoma de Buenos Aires' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Córdoba' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Santa Fe' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Mendoza' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Tucumán' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Salta' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Entre Ríos' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Misiones' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Chaco' AS nombre, 'Argentina' AS pais
  UNION ALL SELECT 'Región Metropolitana' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Valparaíso' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Biobío' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'La Araucanía' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Los Lagos' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Antofagasta' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Coquimbo' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'O''Higgins' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Maule' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Tarapacá' AS nombre, 'Chile' AS pais
  UNION ALL SELECT 'Lima' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Arequipa' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'La Libertad' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Piura' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Lambayeque' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Cusco' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Junín' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Áncash' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Loreto' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Ica' AS nombre, 'Perú' AS pais
  UNION ALL SELECT 'Pichincha' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Guayas' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Azuay' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Manabí' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'El Oro' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Tungurahua' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Loja' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Imbabura' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Chimborazo' AS nombre, 'Ecuador' AS pais
  UNION ALL SELECT 'Esmeraldas' AS nombre, 'Ecuador' AS pais
) x JOIN Pais p ON p.nombrePais = x.pais;
INSERT INTO Ciudad (nombreCiudad, codigoCiudad, idDepartamento)
SELECT x.nombre, x.codigo, d.idDepartamento FROM (
SELECT 'Leticia' AS nombre, 'LET' AS codigo, 'Amazonas' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Medellín' AS nombre, 'MDE' AS codigo, 'Antioquia' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Envigado' AS nombre, NULL AS codigo, 'Antioquia' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Bello' AS nombre, NULL AS codigo, 'Antioquia' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Rionegro' AS nombre, NULL AS codigo, 'Antioquia' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Arauca' AS nombre, 'AUC' AS codigo, 'Arauca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Barranquilla' AS nombre, 'BAQ' AS codigo, 'Atlántico' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Soledad' AS nombre, NULL AS codigo, 'Atlántico' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Bogotá' AS nombre, 'BOG' AS codigo, 'Bogotá D.C.' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Cartagena' AS nombre, 'CTG' AS codigo, 'Bolívar' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Tunja' AS nombre, NULL AS codigo, 'Boyacá' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Duitama' AS nombre, NULL AS codigo, 'Boyacá' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Manizales' AS nombre, 'MZL' AS codigo, 'Caldas' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Florencia' AS nombre, 'FLA' AS codigo, 'Caquetá' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Yopal' AS nombre, 'EYP' AS codigo, 'Casanare' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Popayán' AS nombre, 'PPN' AS codigo, 'Cauca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Valledupar' AS nombre, 'VUP' AS codigo, 'Cesar' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Quibdó' AS nombre, 'UIB' AS codigo, 'Chocó' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Montería' AS nombre, 'MTR' AS codigo, 'Córdoba' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Soacha' AS nombre, NULL AS codigo, 'Cundinamarca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Zipaquirá' AS nombre, NULL AS codigo, 'Cundinamarca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Chía' AS nombre, NULL AS codigo, 'Cundinamarca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Girardot' AS nombre, NULL AS codigo, 'Cundinamarca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Inírida' AS nombre, 'PDA' AS codigo, 'Guainía' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'San José del Guaviare' AS nombre, 'SJE' AS codigo, 'Guaviare' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Neiva' AS nombre, 'NVA' AS codigo, 'Huila' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Riohacha' AS nombre, 'RCH' AS codigo, 'La Guajira' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Santa Marta' AS nombre, 'SMR' AS codigo, 'Magdalena' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Villavicencio' AS nombre, 'VVC' AS codigo, 'Meta' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Pasto' AS nombre, 'PSO' AS codigo, 'Nariño' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Cúcuta' AS nombre, 'CUC' AS codigo, 'Norte de Santander' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Mocoa' AS nombre, NULL AS codigo, 'Putumayo' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Armenia' AS nombre, 'AXM' AS codigo, 'Quindío' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Calarcá' AS nombre, NULL AS codigo, 'Quindío' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Montenegro' AS nombre, NULL AS codigo, 'Quindío' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Pereira' AS nombre, 'PEI' AS codigo, 'Risaralda' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Dosquebradas' AS nombre, NULL AS codigo, 'Risaralda' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'San Andrés' AS nombre, 'ADZ' AS codigo, 'San Andrés y Providencia' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Bucaramanga' AS nombre, 'BGA' AS codigo, 'Santander' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Floridablanca' AS nombre, NULL AS codigo, 'Santander' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Sincelejo' AS nombre, NULL AS codigo, 'Sucre' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Ibagué' AS nombre, 'IBE' AS codigo, 'Tolima' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Cali' AS nombre, 'CLO' AS codigo, 'Valle del Cauca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Palmira' AS nombre, NULL AS codigo, 'Valle del Cauca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Buenaventura' AS nombre, NULL AS codigo, 'Valle del Cauca' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Mitú' AS nombre, 'MVP' AS codigo, 'Vaupés' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Puerto Carreño' AS nombre, 'PCR' AS codigo, 'Vichada' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Madrid' AS nombre, 'MAD' AS codigo, 'Comunidad de Madrid' AS depto, 'España' AS pais
  UNION ALL SELECT 'Barcelona' AS nombre, 'BCN' AS codigo, 'Cataluña' AS depto, 'España' AS pais
  UNION ALL SELECT 'París' AS nombre, 'PAR' AS codigo, 'Isla de Francia' AS depto, 'Francia' AS pais
  UNION ALL SELECT 'Niza' AS nombre, 'NCE' AS codigo, 'Provenza-Alpes-Costa Azul' AS depto, 'Francia' AS pais
  UNION ALL SELECT 'Londres' AS nombre, 'LON' AS codigo, 'Inglaterra' AS depto, 'Reino Unido' AS pais
  UNION ALL SELECT 'Mánchester' AS nombre, 'MAN' AS codigo, 'Inglaterra' AS depto, 'Reino Unido' AS pais
  UNION ALL SELECT 'Edimburgo' AS nombre, 'EDI' AS codigo, 'Escocia' AS depto, 'Reino Unido' AS pais
  UNION ALL SELECT 'Roma' AS nombre, 'ROM' AS codigo, 'Lacio' AS depto, 'Italia' AS pais
  UNION ALL SELECT 'Milán' AS nombre, 'MIL' AS codigo, 'Lombardía' AS depto, 'Italia' AS pais
  UNION ALL SELECT 'Venecia' AS nombre, 'VCE' AS codigo, 'Véneto' AS depto, 'Italia' AS pais
  UNION ALL SELECT 'Florencia' AS nombre, 'FLR' AS codigo, 'Toscana' AS depto, 'Italia' AS pais
  UNION ALL SELECT 'Ámsterdam' AS nombre, 'AMS' AS codigo, 'Holanda Septentrional' AS depto, 'Países Bajos' AS pais
  UNION ALL SELECT 'Fráncfort' AS nombre, 'FRA' AS codigo, 'Hesse' AS depto, 'Alemania' AS pais
  UNION ALL SELECT 'Múnich' AS nombre, 'MUC' AS codigo, 'Baviera' AS depto, 'Alemania' AS pais
  UNION ALL SELECT 'Berlín' AS nombre, 'BER' AS codigo, 'Berlín' AS depto, 'Alemania' AS pais
  UNION ALL SELECT 'Lisboa' AS nombre, 'LIS' AS codigo, 'Lisboa' AS depto, 'Portugal' AS pais
  UNION ALL SELECT 'Oporto' AS nombre, 'OPO' AS codigo, 'Norte' AS depto, 'Portugal' AS pais
  UNION ALL SELECT 'Bruselas' AS nombre, 'BRU' AS codigo, 'Región de Bruselas' AS depto, 'Bélgica' AS pais
  UNION ALL SELECT 'Viena' AS nombre, 'VIE' AS codigo, 'Viena' AS depto, 'Austria' AS pais
  UNION ALL SELECT 'Zúrich' AS nombre, 'ZRH' AS codigo, 'Zúrich' AS depto, 'Suiza' AS pais
  UNION ALL SELECT 'Ginebra' AS nombre, 'GVA' AS codigo, 'Ginebra' AS depto, 'Suiza' AS pais
  UNION ALL SELECT 'Atenas' AS nombre, 'ATH' AS codigo, 'Ática' AS depto, 'Grecia' AS pais
  UNION ALL SELECT 'Dublín' AS nombre, 'DUB' AS codigo, 'Leinster' AS depto, 'Irlanda' AS pais
  UNION ALL SELECT 'Praga' AS nombre, 'PRG' AS codigo, 'Praga' AS depto, 'República Checa' AS pais
  UNION ALL SELECT 'Budapest' AS nombre, 'BUD' AS codigo, 'Budapest' AS depto, 'Hungría' AS pais
  UNION ALL SELECT 'Varsovia' AS nombre, 'WAW' AS codigo, 'Mazovia' AS depto, 'Polonia' AS pais
  UNION ALL SELECT 'Copenhague' AS nombre, 'CPH' AS codigo, 'Región Capital' AS depto, 'Dinamarca' AS pais
  UNION ALL SELECT 'Estocolmo' AS nombre, 'STO' AS codigo, 'Estocolmo' AS depto, 'Suecia' AS pais
  UNION ALL SELECT 'Oslo' AS nombre, 'OSL' AS codigo, 'Oslo' AS depto, 'Noruega' AS pais
  UNION ALL SELECT 'Helsinki' AS nombre, 'HEL' AS codigo, 'Uusimaa' AS depto, 'Finlandia' AS pais
  UNION ALL SELECT 'Estambul' AS nombre, 'IST' AS codigo, 'Estambul' AS depto, 'Turquía' AS pais
  UNION ALL SELECT 'Reikiavik' AS nombre, 'REK' AS codigo, 'Región Capital' AS depto, 'Islandia' AS pais
  UNION ALL SELECT 'Usaquén' AS nombre, NULL AS codigo, 'Bogotá D.C.' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Magangué' AS nombre, NULL AS codigo, 'Bolívar' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Villamaría' AS nombre, NULL AS codigo, 'Caldas' AS depto, 'Colombia' AS pais
  UNION ALL SELECT 'Los Ángeles' AS nombre, NULL AS codigo, 'California' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'San Francisco' AS nombre, NULL AS codigo, 'California' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Houston' AS nombre, NULL AS codigo, 'Texas' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Austin' AS nombre, NULL AS codigo, 'Texas' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Miami' AS nombre, NULL AS codigo, 'Florida' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Orlando' AS nombre, NULL AS codigo, 'Florida' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Nueva York' AS nombre, NULL AS codigo, 'Nueva York' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Búfalo' AS nombre, NULL AS codigo, 'Nueva York' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Chicago' AS nombre, NULL AS codigo, 'Illinois' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Aurora' AS nombre, NULL AS codigo, 'Illinois' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Filadelfia' AS nombre, NULL AS codigo, 'Pensilvania' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Pittsburgh' AS nombre, NULL AS codigo, 'Pensilvania' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Columbus' AS nombre, NULL AS codigo, 'Ohio' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Cleveland' AS nombre, NULL AS codigo, 'Ohio' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Atlanta' AS nombre, NULL AS codigo, 'Georgia' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Savannah' AS nombre, NULL AS codigo, 'Georgia' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Charlotte' AS nombre, NULL AS codigo, 'Carolina del Norte' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Raleigh' AS nombre, NULL AS codigo, 'Carolina del Norte' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Detroit' AS nombre, NULL AS codigo, 'Míchigan' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Grand Rapids' AS nombre, NULL AS codigo, 'Míchigan' AS depto, 'Estados Unidos' AS pais
  UNION ALL SELECT 'Sevilla' AS nombre, NULL AS codigo, 'Andalucía' AS depto, 'España' AS pais
  UNION ALL SELECT 'Málaga' AS nombre, NULL AS codigo, 'Andalucía' AS depto, 'España' AS pais
  UNION ALL SELECT 'Girona' AS nombre, NULL AS codigo, 'Cataluña' AS depto, 'España' AS pais
  UNION ALL SELECT 'Alcalá de Henares' AS nombre, NULL AS codigo, 'Comunidad de Madrid' AS depto, 'España' AS pais
  UNION ALL SELECT 'Valencia' AS nombre, NULL AS codigo, 'Comunidad Valenciana' AS depto, 'España' AS pais
  UNION ALL SELECT 'Alicante' AS nombre, NULL AS codigo, 'Comunidad Valenciana' AS depto, 'España' AS pais
  UNION ALL SELECT 'Vigo' AS nombre, NULL AS codigo, 'Galicia' AS depto, 'España' AS pais
  UNION ALL SELECT 'A Coruña' AS nombre, NULL AS codigo, 'Galicia' AS depto, 'España' AS pais
  UNION ALL SELECT 'Valladolid' AS nombre, NULL AS codigo, 'Castilla y León' AS depto, 'España' AS pais
  UNION ALL SELECT 'Salamanca' AS nombre, NULL AS codigo, 'Castilla y León' AS depto, 'España' AS pais
  UNION ALL SELECT 'Bilbao' AS nombre, NULL AS codigo, 'País Vasco' AS depto, 'España' AS pais
  UNION ALL SELECT 'San Sebastián' AS nombre, NULL AS codigo, 'País Vasco' AS depto, 'España' AS pais
  UNION ALL SELECT 'Las Palmas' AS nombre, NULL AS codigo, 'Canarias' AS depto, 'España' AS pais
  UNION ALL SELECT 'Santa Cruz de Tenerife' AS nombre, NULL AS codigo, 'Canarias' AS depto, 'España' AS pais
  UNION ALL SELECT 'Toledo' AS nombre, NULL AS codigo, 'Castilla-La Mancha' AS depto, 'España' AS pais
  UNION ALL SELECT 'Albacete' AS nombre, NULL AS codigo, 'Castilla-La Mancha' AS depto, 'España' AS pais
  UNION ALL SELECT 'Zaragoza' AS nombre, NULL AS codigo, 'Aragón' AS depto, 'España' AS pais
  UNION ALL SELECT 'Huesca' AS nombre, NULL AS codigo, 'Aragón' AS depto, 'España' AS pais
  UNION ALL SELECT 'Ciudad de México' AS nombre, NULL AS codigo, 'Ciudad de México' AS depto, 'México' AS pais
  UNION ALL SELECT 'Coyoacán' AS nombre, NULL AS codigo, 'Ciudad de México' AS depto, 'México' AS pais
  UNION ALL SELECT 'Guadalajara' AS nombre, NULL AS codigo, 'Jalisco' AS depto, 'México' AS pais
  UNION ALL SELECT 'Zapopan' AS nombre, NULL AS codigo, 'Jalisco' AS depto, 'México' AS pais
  UNION ALL SELECT 'Monterrey' AS nombre, NULL AS codigo, 'Nuevo León' AS depto, 'México' AS pais
  UNION ALL SELECT 'San Pedro Garza García' AS nombre, NULL AS codigo, 'Nuevo León' AS depto, 'México' AS pais
  UNION ALL SELECT 'Puebla' AS nombre, NULL AS codigo, 'Puebla' AS depto, 'México' AS pais
  UNION ALL SELECT 'Tehuacán' AS nombre, NULL AS codigo, 'Puebla' AS depto, 'México' AS pais
  UNION ALL SELECT 'León' AS nombre, NULL AS codigo, 'Guanajuato' AS depto, 'México' AS pais
  UNION ALL SELECT 'Irapuato' AS nombre, NULL AS codigo, 'Guanajuato' AS depto, 'México' AS pais
  UNION ALL SELECT 'Toluca' AS nombre, NULL AS codigo, 'Estado de México' AS depto, 'México' AS pais
  UNION ALL SELECT 'Ecatepec' AS nombre, NULL AS codigo, 'Estado de México' AS depto, 'México' AS pais
  UNION ALL SELECT 'Veracruz' AS nombre, NULL AS codigo, 'Veracruz' AS depto, 'México' AS pais
  UNION ALL SELECT 'Xalapa' AS nombre, NULL AS codigo, 'Veracruz' AS depto, 'México' AS pais
  UNION ALL SELECT 'Mérida' AS nombre, NULL AS codigo, 'Yucatán' AS depto, 'México' AS pais
  UNION ALL SELECT 'Valladolid' AS nombre, NULL AS codigo, 'Yucatán' AS depto, 'México' AS pais
  UNION ALL SELECT 'Cancún' AS nombre, NULL AS codigo, 'Quintana Roo' AS depto, 'México' AS pais
  UNION ALL SELECT 'Playa del Carmen' AS nombre, NULL AS codigo, 'Quintana Roo' AS depto, 'México' AS pais
  UNION ALL SELECT 'Tijuana' AS nombre, NULL AS codigo, 'Baja California' AS depto, 'México' AS pais
  UNION ALL SELECT 'Mexicali' AS nombre, NULL AS codigo, 'Baja California' AS depto, 'México' AS pais
  UNION ALL SELECT 'La Plata' AS nombre, NULL AS codigo, 'Buenos Aires' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Mar del Plata' AS nombre, NULL AS codigo, 'Buenos Aires' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Buenos Aires' AS nombre, NULL AS codigo, 'Ciudad Autónoma de Buenos Aires' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Palermo' AS nombre, NULL AS codigo, 'Ciudad Autónoma de Buenos Aires' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Córdoba' AS nombre, NULL AS codigo, 'Córdoba' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Villa Carlos Paz' AS nombre, NULL AS codigo, 'Córdoba' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Rosario' AS nombre, NULL AS codigo, 'Santa Fe' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Santa Fe' AS nombre, NULL AS codigo, 'Santa Fe' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Mendoza' AS nombre, NULL AS codigo, 'Mendoza' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'San Rafael' AS nombre, NULL AS codigo, 'Mendoza' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'San Miguel de Tucumán' AS nombre, NULL AS codigo, 'Tucumán' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Yerba Buena' AS nombre, NULL AS codigo, 'Tucumán' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Salta' AS nombre, NULL AS codigo, 'Salta' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'San Ramón de la Nueva Orán' AS nombre, NULL AS codigo, 'Salta' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Paraná' AS nombre, NULL AS codigo, 'Entre Ríos' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Concordia' AS nombre, NULL AS codigo, 'Entre Ríos' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Posadas' AS nombre, NULL AS codigo, 'Misiones' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Puerto Iguazú' AS nombre, NULL AS codigo, 'Misiones' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Resistencia' AS nombre, NULL AS codigo, 'Chaco' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Presidencia Roque Sáenz Peña' AS nombre, NULL AS codigo, 'Chaco' AS depto, 'Argentina' AS pais
  UNION ALL SELECT 'Santiago' AS nombre, NULL AS codigo, 'Región Metropolitana' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Puente Alto' AS nombre, NULL AS codigo, 'Región Metropolitana' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Valparaíso' AS nombre, NULL AS codigo, 'Valparaíso' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Viña del Mar' AS nombre, NULL AS codigo, 'Valparaíso' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Concepción' AS nombre, NULL AS codigo, 'Biobío' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Talcahuano' AS nombre, NULL AS codigo, 'Biobío' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Temuco' AS nombre, NULL AS codigo, 'La Araucanía' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Villarrica' AS nombre, NULL AS codigo, 'La Araucanía' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Puerto Montt' AS nombre, NULL AS codigo, 'Los Lagos' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Osorno' AS nombre, NULL AS codigo, 'Los Lagos' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Antofagasta' AS nombre, NULL AS codigo, 'Antofagasta' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Calama' AS nombre, NULL AS codigo, 'Antofagasta' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'La Serena' AS nombre, NULL AS codigo, 'Coquimbo' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Coquimbo' AS nombre, NULL AS codigo, 'Coquimbo' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Rancagua' AS nombre, NULL AS codigo, 'O''Higgins' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'San Fernando' AS nombre, NULL AS codigo, 'O''Higgins' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Talca' AS nombre, NULL AS codigo, 'Maule' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Curicó' AS nombre, NULL AS codigo, 'Maule' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Iquique' AS nombre, NULL AS codigo, 'Tarapacá' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Alto Hospicio' AS nombre, NULL AS codigo, 'Tarapacá' AS depto, 'Chile' AS pais
  UNION ALL SELECT 'Lima' AS nombre, NULL AS codigo, 'Lima' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Miraflores' AS nombre, NULL AS codigo, 'Lima' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Arequipa' AS nombre, NULL AS codigo, 'Arequipa' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Camaná' AS nombre, NULL AS codigo, 'Arequipa' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Trujillo' AS nombre, NULL AS codigo, 'La Libertad' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Chepén' AS nombre, NULL AS codigo, 'La Libertad' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Piura' AS nombre, NULL AS codigo, 'Piura' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Sullana' AS nombre, NULL AS codigo, 'Piura' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Chiclayo' AS nombre, NULL AS codigo, 'Lambayeque' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Lambayeque' AS nombre, NULL AS codigo, 'Lambayeque' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Cusco' AS nombre, NULL AS codigo, 'Cusco' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Urubamba' AS nombre, NULL AS codigo, 'Cusco' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Huancayo' AS nombre, NULL AS codigo, 'Junín' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Tarma' AS nombre, NULL AS codigo, 'Junín' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Chimbote' AS nombre, NULL AS codigo, 'Áncash' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Huaraz' AS nombre, NULL AS codigo, 'Áncash' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Iquitos' AS nombre, NULL AS codigo, 'Loreto' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Yurimaguas' AS nombre, NULL AS codigo, 'Loreto' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Ica' AS nombre, NULL AS codigo, 'Ica' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Chincha Alta' AS nombre, NULL AS codigo, 'Ica' AS depto, 'Perú' AS pais
  UNION ALL SELECT 'Quito' AS nombre, NULL AS codigo, 'Pichincha' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Sangolquí' AS nombre, NULL AS codigo, 'Pichincha' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Guayaquil' AS nombre, NULL AS codigo, 'Guayas' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Durán' AS nombre, NULL AS codigo, 'Guayas' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Cuenca' AS nombre, NULL AS codigo, 'Azuay' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Gualaceo' AS nombre, NULL AS codigo, 'Azuay' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Portoviejo' AS nombre, NULL AS codigo, 'Manabí' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Manta' AS nombre, NULL AS codigo, 'Manabí' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Machala' AS nombre, NULL AS codigo, 'El Oro' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Pasaje' AS nombre, NULL AS codigo, 'El Oro' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Ambato' AS nombre, NULL AS codigo, 'Tungurahua' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Baños' AS nombre, NULL AS codigo, 'Tungurahua' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Loja' AS nombre, NULL AS codigo, 'Loja' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Catamayo' AS nombre, NULL AS codigo, 'Loja' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Ibarra' AS nombre, NULL AS codigo, 'Imbabura' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Otavalo' AS nombre, NULL AS codigo, 'Imbabura' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Riobamba' AS nombre, NULL AS codigo, 'Chimborazo' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Guano' AS nombre, NULL AS codigo, 'Chimborazo' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Esmeraldas' AS nombre, NULL AS codigo, 'Esmeraldas' AS depto, 'Ecuador' AS pais
  UNION ALL SELECT 'Atacames' AS nombre, NULL AS codigo, 'Esmeraldas' AS depto, 'Ecuador' AS pais
) x JOIN Pais p ON p.nombrePais = x.pais JOIN Departamento d ON d.nombreDepartamento = x.depto AND d.idPais = p.idPais;

-- ---------------- Sedes, planes y ofertas ----------------
INSERT INTO Sede (nombreSede, direccion, idCiudad)
SELECT x.nombre, x.direccion, c.idCiudad FROM (
SELECT 'Sede Principal Bogotá' AS nombre, 'Calle 26 # 92-32, Fontibón' AS direccion, 'BOG' AS codigo
  UNION ALL SELECT 'Sede Medellín' AS nombre, 'Carrera 43A # 1-50, El Poblado' AS direccion, 'MDE' AS codigo
  UNION ALL SELECT 'Sede Armenia' AS nombre, 'Carrera 14 # 18-56, Centro' AS direccion, 'AXM' AS codigo
  UNION ALL SELECT 'Sede Madrid' AS nombre, 'Calle de Alcalá 45' AS direccion, 'MAD' AS codigo
) x JOIN Ciudad c ON c.codigoCiudad = x.codigo;
INSERT INTO Plan (nombrePlan, descripcionPlan, pesoMaxEquipaje, incluyeCheckIn, precioExtra) VALUES
('Básico', 'Solo equipaje de mano', 10.00, FALSE, 0.00),
('Clásico', 'Equipaje de mano y una maleta de bodega', 23.00, FALSE, 120000.00),
('Flexible', 'Maleta de bodega, check-in prioritario y cambios sin costo', 32.00, TRUE, 260000.00),
('Premium', 'Dos maletas, sala VIP, check-in prioritario y asiento preferente', 46.00, TRUE, 480000.00);
INSERT INTO Oferta (titulo, descripcion, porcentajeDescuento, fechaInicio, fechaFin) VALUES
('Bienvenida', '10% en tu primera reserva', 10.00, '2026-01-01', '2026-12-31'),
('Navidad en Europa', '15% en viajes por Europa en diciembre', 15.00, '2026-12-01', '2026-12-31'),
('Semana Santa 2027', '20% en rutas nacionales', 20.00, '2027-03-15', '2027-04-05');

-- ---------------- Vehículos ----------------
INSERT INTO Vehiculo (codigoIdentificacion, idTipoTransporte, capacidad)
SELECT x.codigo, t.idTipoTransporte, x.capacidad FROM (
SELECT 'HK-5120' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'HK-5288' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'HK-5301' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'HK-5317' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'EC-NVJ' AS codigo, 'AVION' AS tipo, 300 AS capacidad
  UNION ALL SELECT 'EC-MXV' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'G-EUYR' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'I-RIBN' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'F-HBNK' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'EI-DVM' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'PH-BXA' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'LN-RKF' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'OH-LXF' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'TF-ICE' AS codigo, 'AVION' AS tipo, 160 AS capacidad
  UNION ALL SELECT 'TC-JTA' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'D-AIZQ' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'SP-LWA' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'G-EZWA' AS codigo, 'AVION' AS tipo, 180 AS capacidad
  UNION ALL SELECT 'AVE-103' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'EST-3201' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'THA-4532' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'THA-4534' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'FR-1000A' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'FR-1000B' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'ITA-0912' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'ICE-4711' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'ICE-4712' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'EC-171' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'RJ-0075' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'RJ-0061' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'IC-0712' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'EC-19' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'AP-0131' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'SJ-0393' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'TGV-6173' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'RN-0332' AS codigo, 'TREN' AS tipo, 400 AS capacidad
  UNION ALL SELECT 'TSX-482' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'WPK-318' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'SMB-702' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'VCX-215' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'ALS-2210' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'FLX-7731' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'NEX-0451' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'NEX-0452' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'FLX-7740' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'FLX-7752' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'FLX-7763' AS codigo, 'BUS' AS tipo, 40 AS capacidad
  UNION ALL SELECT 'FLX-7771' AS codigo, 'BUS' AS tipo, 40 AS capacidad
) x JOIN TipoTransporte t ON t.nombreTipoTransporte = x.tipo;

-- ---------------- Rutas (ida y vuelta) y su vehículo ----------------
CREATE TEMPORARY TABLE tmp_ruta (orden INT, origen VARCHAR(3), destino VARCHAR(3), distancia DECIMAL(10,2), duracion INT, precio DECIMAL(12,2), dirOrigen VARCHAR(40), dirDestino VARCHAR(40), vehiculo VARCHAR(25));
INSERT INTO tmp_ruta VALUES
(1, 'BOG', 'MDE', 215, 55, 280000, 'Aeropuerto El Dorado', 'Aeropuerto José María Córdova', 'HK-5120'),
(2, 'MDE', 'BOG', 215, 55, 280000, 'Aeropuerto José María Córdova', 'Aeropuerto El Dorado', 'HK-5120'),
(3, 'BOG', 'CTG', 660, 95, 420000, 'Aeropuerto El Dorado', 'Aeropuerto Rafael Núñez', 'HK-5288'),
(4, 'CTG', 'BOG', 660, 95, 420000, 'Aeropuerto Rafael Núñez', 'Aeropuerto El Dorado', 'HK-5288'),
(5, 'BOG', 'CLO', 300, 60, 260000, 'Aeropuerto El Dorado', 'Aeropuerto Alfonso Bonilla Aragón', 'HK-5301'),
(6, 'CLO', 'BOG', 300, 60, 260000, 'Aeropuerto Alfonso Bonilla Aragón', 'Aeropuerto El Dorado', 'HK-5301'),
(7, 'BOG', 'AXM', 190, 50, 240000, 'Aeropuerto El Dorado', 'Aeropuerto El Edén', 'HK-5317'),
(8, 'AXM', 'BOG', 190, 50, 240000, 'Aeropuerto El Edén', 'Aeropuerto El Dorado', 'HK-5317'),
(9, 'BOG', 'MAD', 8030, 600, 3200000, 'Aeropuerto El Dorado', 'Aeropuerto Adolfo Suárez Barajas', 'EC-NVJ'),
(10, 'MAD', 'BOG', 8030, 600, 3200000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto El Dorado', 'EC-NVJ'),
(11, 'MAD', 'PAR', 1050, 120, 520000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto Charles de Gaulle', 'EC-MXV'),
(12, 'PAR', 'MAD', 1050, 120, 520000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Adolfo Suárez Barajas', 'EC-MXV'),
(13, 'MAD', 'LON', 1260, 140, 560000, 'Aeropuerto Adolfo Suárez Barajas', 'Aeropuerto de Heathrow', 'G-EUYR'),
(14, 'LON', 'MAD', 1260, 140, 560000, 'Aeropuerto de Heathrow', 'Aeropuerto Adolfo Suárez Barajas', 'G-EUYR'),
(15, 'BCN', 'ROM', 860, 115, 480000, 'Aeropuerto de El Prat', 'Aeropuerto de Fiumicino', 'I-RIBN'),
(16, 'ROM', 'BCN', 860, 115, 480000, 'Aeropuerto de Fiumicino', 'Aeropuerto de El Prat', 'I-RIBN'),
(17, 'PAR', 'ATH', 2100, 205, 780000, 'Aeropuerto Charles de Gaulle', 'Aeropuerto Eleftherios Venizelos', 'F-HBNK'),
(18, 'ATH', 'PAR', 2100, 205, 780000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto Charles de Gaulle', 'F-HBNK'),
(19, 'LON', 'DUB', 460, 80, 390000, 'Aeropuerto de Heathrow', 'Aeropuerto de Dublín', 'EI-DVM'),
(20, 'DUB', 'LON', 460, 80, 390000, 'Aeropuerto de Dublín', 'Aeropuerto de Heathrow', 'EI-DVM'),
(21, 'AMS', 'CPH', 620, 85, 450000, 'Aeropuerto de Schiphol', 'Aeropuerto de Kastrup', 'PH-BXA'),
(22, 'CPH', 'AMS', 620, 85, 450000, 'Aeropuerto de Kastrup', 'Aeropuerto de Schiphol', 'PH-BXA'),
(23, 'CPH', 'OSL', 520, 70, 410000, 'Aeropuerto de Kastrup', 'Aeropuerto de Gardermoen', 'LN-RKF'),
(24, 'OSL', 'CPH', 520, 70, 410000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Kastrup', 'LN-RKF'),
(25, 'STO', 'HEL', 400, 60, 380000, 'Aeropuerto de Arlanda', 'Aeropuerto de Helsinki-Vantaa', 'OH-LXF'),
(26, 'HEL', 'STO', 400, 60, 380000, 'Aeropuerto de Helsinki-Vantaa', 'Aeropuerto de Arlanda', 'OH-LXF'),
(27, 'OSL', 'REK', 1750, 180, 690000, 'Aeropuerto de Gardermoen', 'Aeropuerto de Keflavík', 'TF-ICE'),
(28, 'REK', 'OSL', 1750, 180, 690000, 'Aeropuerto de Keflavík', 'Aeropuerto de Gardermoen', 'TF-ICE'),
(29, 'IST', 'ATH', 560, 75, 420000, 'Aeropuerto de Estambul', 'Aeropuerto Eleftherios Venizelos', 'TC-JTA'),
(30, 'ATH', 'IST', 560, 75, 420000, 'Aeropuerto Eleftherios Venizelos', 'Aeropuerto de Estambul', 'TC-JTA'),
(31, 'FRA', 'LIS', 1890, 185, 650000, 'Aeropuerto de Fráncfort', 'Aeropuerto Humberto Delgado', 'D-AIZQ'),
(32, 'LIS', 'FRA', 1890, 185, 650000, 'Aeropuerto Humberto Delgado', 'Aeropuerto de Fráncfort', 'D-AIZQ'),
(33, 'WAW', 'BER', 520, 75, 360000, 'Aeropuerto Chopin', 'Aeropuerto BER Brandeburgo', 'SP-LWA'),
(34, 'BER', 'WAW', 520, 75, 360000, 'Aeropuerto BER Brandeburgo', 'Aeropuerto Chopin', 'SP-LWA'),
(35, 'EDI', 'MAN', 290, 60, 300000, 'Aeropuerto de Edimburgo', 'Aeropuerto de Mánchester', 'G-EZWA'),
(36, 'MAN', 'EDI', 290, 60, 300000, 'Aeropuerto de Mánchester', 'Aeropuerto de Edimburgo', 'G-EZWA'),
(37, 'MAD', 'BCN', 621, 150, 290000, 'Estación Madrid Puerta de Atocha', 'Estación Barcelona Sants', 'AVE-103'),
(38, 'BCN', 'MAD', 621, 150, 290000, 'Estación Barcelona Sants', 'Estación Madrid Puerta de Atocha', 'AVE-103'),
(39, 'PAR', 'LON', 460, 135, 420000, 'Estación París Gare du Nord', 'Estación Londres St Pancras', 'EST-3201'),
(40, 'LON', 'PAR', 460, 135, 420000, 'Estación Londres St Pancras', 'Estación París Gare du Nord', 'EST-3201'),
(41, 'PAR', 'BRU', 315, 82, 230000, 'Estación París Gare du Nord', 'Estación Bruselas-Midi', 'THA-4532'),
(42, 'BRU', 'PAR', 315, 82, 230000, 'Estación Bruselas-Midi', 'Estación París Gare du Nord', 'THA-4532'),
(43, 'BRU', 'AMS', 210, 110, 190000, 'Estación Bruselas-Midi', 'Estación Ámsterdam Centraal', 'THA-4534'),
(44, 'AMS', 'BRU', 210, 110, 190000, 'Estación Ámsterdam Centraal', 'Estación Bruselas-Midi', 'THA-4534'),
(45, 'ROM', 'FLR', 275, 95, 170000, 'Estación Roma Termini', 'Estación Florencia Santa María Novella', 'FR-1000A'),
(46, 'FLR', 'ROM', 275, 95, 170000, 'Estación Florencia Santa María Novella', 'Estación Roma Termini', 'FR-1000A'),
(47, 'FLR', 'VCE', 255, 125, 160000, 'Estación Florencia Santa María Novella', 'Estación Venecia Santa Lucía', 'FR-1000B'),
(48, 'VCE', 'FLR', 255, 125, 160000, 'Estación Venecia Santa Lucía', 'Estación Florencia Santa María Novella', 'FR-1000B'),
(49, 'MIL', 'VCE', 270, 145, 150000, 'Estación Milano Centrale', 'Estación Venecia Santa Lucía', 'ITA-0912'),
(50, 'VCE', 'MIL', 270, 145, 150000, 'Estación Venecia Santa Lucía', 'Estación Milano Centrale', 'ITA-0912'),
(51, 'FRA', 'MUC', 390, 190, 210000, 'Estación Frankfurt Hbf', 'Estación München Hbf', 'ICE-4711'),
(52, 'MUC', 'FRA', 390, 190, 210000, 'Estación München Hbf', 'Estación Frankfurt Hbf', 'ICE-4711'),
(53, 'BER', 'MUC', 585, 240, 240000, 'Estación Berlin Hbf', 'Estación München Hbf', 'ICE-4712'),
(54, 'MUC', 'BER', 585, 240, 240000, 'Estación München Hbf', 'Estación Berlin Hbf', 'ICE-4712'),
(55, 'BER', 'PRG', 350, 255, 160000, 'Estación Berlin Hbf', 'Estación Praha hlavní nádraží', 'EC-171'),
(56, 'PRG', 'BER', 350, 255, 160000, 'Estación Praha hlavní nádraží', 'Estación Berlin Hbf', 'EC-171'),
(57, 'PRG', 'VIE', 330, 240, 150000, 'Estación Praha hlavní nádraží', 'Estación Wien Hauptbahnhof', 'RJ-0075'),
(58, 'VIE', 'PRG', 330, 240, 150000, 'Estación Wien Hauptbahnhof', 'Estación Praha hlavní nádraží', 'RJ-0075'),
(59, 'VIE', 'BUD', 245, 160, 120000, 'Estación Wien Hauptbahnhof', 'Estación Budapest-Keleti', 'RJ-0061'),
(60, 'BUD', 'VIE', 245, 160, 120000, 'Estación Budapest-Keleti', 'Estación Wien Hauptbahnhof', 'RJ-0061'),
(61, 'ZRH', 'GVA', 280, 165, 230000, 'Estación Zúrich HB', 'Estación Ginebra-Cornavin', 'IC-0712'),
(62, 'GVA', 'ZRH', 280, 165, 230000, 'Estación Ginebra-Cornavin', 'Estación Zúrich HB', 'IC-0712'),
(63, 'ZRH', 'MIL', 280, 200, 210000, 'Estación Zúrich HB', 'Estación Milano Centrale', 'EC-19'),
(64, 'MIL', 'ZRH', 280, 200, 210000, 'Estación Milano Centrale', 'Estación Zúrich HB', 'EC-19'),
(65, 'LIS', 'OPO', 315, 175, 110000, 'Estación Lisboa Santa Apolónia', 'Estación Oporto Campanhã', 'AP-0131'),
(66, 'OPO', 'LIS', 315, 175, 110000, 'Estación Oporto Campanhã', 'Estación Lisboa Santa Apolónia', 'AP-0131'),
(67, 'STO', 'OSL', 520, 330, 180000, 'Estación Estocolmo Central', 'Estación Oslo Sentralstasjon', 'SJ-0393'),
(68, 'OSL', 'STO', 520, 330, 180000, 'Estación Oslo Sentralstasjon', 'Estación Estocolmo Central', 'SJ-0393'),
(69, 'PAR', 'NCE', 930, 345, 340000, 'Estación París Gare de Lyon', 'Estación Niza Ville', 'TGV-6173'),
(70, 'NCE', 'PAR', 930, 345, 340000, 'Estación Niza Ville', 'Estación París Gare de Lyon', 'TGV-6173'),
(71, 'MAD', 'OPO', 590, 420, 190000, 'Estación Madrid Chamartín', 'Estación Oporto Campanhã', 'RN-0332'),
(72, 'OPO', 'MAD', 590, 420, 190000, 'Estación Oporto Campanhã', 'Estación Madrid Chamartín', 'RN-0332'),
(73, 'BOG', 'MDE', 415, 540, 95000, 'Terminal de Transporte Salitre', 'Terminal del Norte de Medellín', 'TSX-482'),
(74, 'MDE', 'BOG', 415, 540, 95000, 'Terminal del Norte de Medellín', 'Terminal de Transporte Salitre', 'TSX-482'),
(75, 'BOG', 'AXM', 290, 480, 75000, 'Terminal de Transporte Salitre', 'Terminal de Transporte de Armenia', 'WPK-318'),
(76, 'AXM', 'BOG', 290, 480, 75000, 'Terminal de Transporte de Armenia', 'Terminal de Transporte Salitre', 'WPK-318'),
(77, 'MDE', 'AXM', 250, 360, 65000, 'Terminal del Sur de Medellín', 'Terminal de Transporte de Armenia', 'SMB-702'),
(78, 'AXM', 'MDE', 250, 360, 65000, 'Terminal de Transporte de Armenia', 'Terminal del Sur de Medellín', 'SMB-702'),
(79, 'CLO', 'AXM', 185, 240, 45000, 'Terminal de Transportes de Cali', 'Terminal de Transporte de Armenia', 'VCX-215'),
(80, 'AXM', 'CLO', 185, 240, 45000, 'Terminal de Transporte de Armenia', 'Terminal de Transportes de Cali', 'VCX-215'),
(81, 'MAD', 'LIS', 625, 540, 140000, 'Estación Sur de Autobuses', 'Terminal Rodoviário Oriente', 'ALS-2210'),
(82, 'LIS', 'MAD', 625, 540, 140000, 'Terminal Rodoviário Oriente', 'Estación Sur de Autobuses', 'ALS-2210'),
(83, 'BCN', 'NCE', 660, 600, 160000, 'Estación de Autobuses Barcelona Nord', 'Estación de autobuses de Niza', 'FLX-7731'),
(84, 'NCE', 'BCN', 660, 600, 160000, 'Estación de autobuses de Niza', 'Estación de Autobuses Barcelona Nord', 'FLX-7731'),
(85, 'LON', 'MAN', 335, 270, 90000, 'Victoria Coach Station', 'Manchester Coach Station', 'NEX-0451'),
(86, 'MAN', 'LON', 335, 270, 90000, 'Manchester Coach Station', 'Victoria Coach Station', 'NEX-0451'),
(87, 'LON', 'EDI', 650, 570, 140000, 'Victoria Coach Station', 'Edinburgh Bus Station', 'NEX-0452'),
(88, 'EDI', 'LON', 650, 570, 140000, 'Edinburgh Bus Station', 'Victoria Coach Station', 'NEX-0452'),
(89, 'AMS', 'BER', 655, 450, 150000, 'Terminal Amsterdam Sloterdijk', 'Terminal Berlin ZOB', 'FLX-7740'),
(90, 'BER', 'AMS', 655, 450, 150000, 'Terminal Berlin ZOB', 'Terminal Amsterdam Sloterdijk', 'FLX-7740'),
(91, 'BUD', 'WAW', 820, 630, 170000, 'Terminal Budapest Népliget', 'Terminal Warszawa Zachodnia', 'FLX-7752'),
(92, 'WAW', 'BUD', 820, 630, 170000, 'Terminal Warszawa Zachodnia', 'Terminal Budapest Népliget', 'FLX-7752'),
(93, 'MUC', 'VIE', 435, 330, 110000, 'Terminal München ZOB', 'Terminal Wien Erdberg', 'FLX-7763'),
(94, 'VIE', 'MUC', 435, 330, 110000, 'Terminal Wien Erdberg', 'Terminal München ZOB', 'FLX-7763'),
(95, 'PAR', 'AMS', 500, 480, 120000, 'Terminal París Bercy', 'Terminal Amsterdam Sloterdijk', 'FLX-7771'),
(96, 'AMS', 'PAR', 500, 480, 120000, 'Terminal Amsterdam Sloterdijk', 'Terminal París Bercy', 'FLX-7771');
INSERT INTO Ruta (distancia, duracionEstimada, precioBase, idCiudadOrigen, direccionOrigen, idCiudadDestino, direccionDestino)
SELECT t.distancia, t.duracion, t.precio, co.idCiudad, t.dirOrigen, cd.idCiudad, t.dirDestino
FROM tmp_ruta t
JOIN Ciudad co ON co.codigoCiudad = t.origen
JOIN Ciudad cd ON cd.codigoCiudad = t.destino
ORDER BY t.orden;
INSERT INTO RutaVehiculo (idRuta, idVehiculo)
SELECT r.idRuta, v.idVehiculo
FROM tmp_ruta t
JOIN Ciudad co ON co.codigoCiudad = t.origen
JOIN Ciudad cd ON cd.codigoCiudad = t.destino
JOIN Ruta r ON r.idCiudadOrigen = co.idCiudad AND r.idCiudadDestino = cd.idCiudad AND r.direccionOrigen = t.dirOrigen
JOIN Vehiculo v ON v.codigoIdentificacion = t.vehiculo;
DROP TEMPORARY TABLE tmp_ruta;

-- ---------------- Programación de viajes que se repiten ----------------
-- Cada ruta sale todos los días entre fechaDesde y fechaHasta, a las horas
-- de su tipo de transporte. Para cambiar el periodo o las horas, edita los
-- valores de "dias" y "horarios" y vuelve a ejecutar solo este bloque.
INSERT INTO ProgramacionViaje (fechaHoraSalida, idRuta)
WITH RECURSIVE dias (dia) AS (
    SELECT DATE '2026-11-13'
    UNION ALL
    SELECT dia + INTERVAL 1 DAY FROM dias WHERE dia < DATE '2027-01-31'
),
horarios (tipo, hora) AS (
              SELECT 'AVION', TIME '06:30:00'
    UNION ALL SELECT 'AVION', TIME '18:15:00'
    UNION ALL SELECT 'TREN',  TIME '07:00:00'
    UNION ALL SELECT 'TREN',  TIME '13:30:00'
    UNION ALL SELECT 'TREN',  TIME '19:45:00'
    UNION ALL SELECT 'BUS',   TIME '08:00:00'
    UNION ALL SELECT 'BUS',   TIME '22:30:00'
)
SELECT TIMESTAMP(d.dia, h.hora), r.idRuta
FROM Ruta r
JOIN RutaVehiculo rv   ON rv.idRuta = r.idRuta
JOIN Vehiculo v        ON v.idVehiculo = rv.idVehiculo
JOIN TipoTransporte t  ON t.idTipoTransporte = v.idTipoTransporte
JOIN horarios h        ON h.tipo = t.nombreTipoTransporte
CROSS JOIN dias d
ORDER BY 1, 2;

-- ---------------- Plantilla: repetir UNA ruta cada N horas ----------------
-- Cambia el id de la ruta, el inicio, el fin y el intervalo. Ejemplo: ruta 1,
-- cada 8 horas durante diciembre. (También existe el endpoint
-- POST /api/programaciones/recurrente que hace lo mismo desde la API.)
--
-- INSERT INTO ProgramacionViaje (fechaHoraSalida, idRuta)
-- WITH RECURSIVE salidas (salida) AS (
--     SELECT TIMESTAMP '2026-12-01 06:00:00'
--     UNION ALL
--     SELECT salida + INTERVAL 8 HOUR FROM salidas
--     WHERE salida + INTERVAL 8 HOUR <= TIMESTAMP '2026-12-31 23:59:00'
-- )
-- SELECT salida, 1 FROM salidas;

-- Resumen: SELECT COUNT(*) FROM Ruta;  -- 96 rutas
