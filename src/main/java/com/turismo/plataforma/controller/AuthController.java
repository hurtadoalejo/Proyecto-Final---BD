package com.turismo.plataforma.controller;

import com.turismo.plataforma.dto.auth.LoginRequest;
import com.turismo.plataforma.dto.auth.LoginResponse;
import com.turismo.plataforma.dto.auth.RegistroRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** Registro de un cliente con el JSON del formulario del frontend. */
    @PostMapping("/registro")
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        ClienteResponse creado = authService.registrar(request);
        return ResponseEntity.created(URI.create("/api/clientes/" + creado.idUsuario())).body(creado);
    }

    /** {"correo": "...", "contrasenia": "..."} -> 200 con el usuario, o 401. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
