package com.turismo.plataforma.config;

import com.turismo.plataforma.model.Empleado;
import com.turismo.plataforma.model.catalogo.Rol;
import com.turismo.plataforma.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Al arrancar, si no hay ningún ADMIN, crea el administrador inicial.
 * Necesita que ya se hayan ejecutado 01_esquema.sql y 02_datos.sql; si falta algo, solo avisa en el log.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String CORREO_ADMIN = "admin@viajacol.com";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final CargoRepository cargoRepository;
    private final SedeRepository sedeRepository;
    private final CiudadRepository ciudadRepository;
    private final String contraseniaAdmin;

    public DataInitializer(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                           TipoIdentificacionRepository tipoIdentificacionRepository,
                           CargoRepository cargoRepository, SedeRepository sedeRepository,
                           CiudadRepository ciudadRepository,
                           @Value("${app.admin.contrasenia}") String contraseniaAdmin) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.cargoRepository = cargoRepository;
        this.sedeRepository = sedeRepository;
        this.ciudadRepository = ciudadRepository;
        this.contraseniaAdmin = contraseniaAdmin;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarioRepository.existsByRolNombreRol(Rol.ADMIN)) {
            return;
        }
        var rol = rolRepository.findByNombreRolIgnoreCase(Rol.ADMIN);
        var tipo = tipoIdentificacionRepository.findByNombreIdentificacionIgnoreCase("CC");
        var cargo = cargoRepository.findByNombreCargoIgnoreCase("Administrador del sistema");
        var sede = sedeRepository.findByNombreSedeIgnoreCase("Sede Principal Bogotá");
        var ciudad = ciudadRepository.findByCodigoCiudadIgnoreCase("BOG");
        if (rol.isEmpty() || tipo.isEmpty() || cargo.isEmpty() || sede.isEmpty() || ciudad.isEmpty()) {
            log.warn("No se creó el administrador inicial: ejecuta primero db/01_esquema.sql y db/02_datos.sql");
            return;
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(CORREO_ADMIN)) {
            log.warn("Ya existe un usuario con el correo {} pero no es ADMIN", CORREO_ADMIN);
            return;
        }

        Empleado admin = new Empleado();
        admin.getNombre().setPrimerNombre("Laura");
        admin.getNombre().setPrimerApellido("Restrepo");
        admin.setTipoIdentificacion(tipo.get());
        admin.setIdentificacion("1020456789");
        admin.setFechaNacimiento(LocalDate.of(1988, 4, 12));
        admin.setTelefono("+57 3104567890");
        admin.setCorreo(CORREO_ADMIN);
        admin.setContrasenia(contraseniaAdmin);
        admin.setDireccion("Calle 26 # 92-32");
        admin.setCiudad(ciudad.get());
        admin.setRol(rol.get());
        admin.setCargo(cargo.get());
        admin.setSede(sede.get());
        admin.setSalario(new BigDecimal("6500000.00"));
        admin.setFechaContratacion(LocalDate.of(2024, 1, 15));
        usuarioRepository.save(admin);
        log.info("Administrador inicial creado: {} (cambia la contraseña después de entrar)", CORREO_ADMIN);
    }
}
