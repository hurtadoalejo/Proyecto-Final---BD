package com.turismo.plataforma.service;

import com.turismo.plataforma.dto.cliente.ClienteActualizarRequest;
import com.turismo.plataforma.dto.cliente.ClienteRequest;
import com.turismo.plataforma.dto.cliente.ClienteResponse;
import com.turismo.plataforma.exception.RecursoNoEncontradoException;
import com.turismo.plataforma.model.Cliente;
import com.turismo.plataforma.model.enums.Rol;
import com.turismo.plataforma.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    public ClienteService(ClienteRepository clienteRepository, UsuarioService usuarioService) {
        this.clienteRepository = clienteRepository;
        this.usuarioService = usuarioService;
    }

    public List<ClienteResponse> listar(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return clienteRepository.buscar(filtro).stream()
                .map(ClienteResponse::from)
                .toList();
    }

    public ClienteResponse obtener(Long id) {
        return ClienteResponse.from(buscarEntidad(id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        usuarioService.validarDatosUnicos(request, null);
        Cliente cliente = new Cliente();
        usuarioService.aplicarDatosPersonales(cliente, request);
        usuarioService.asignarContrasenia(cliente, request.contrasenia());
        cliente.setRol(Rol.CLIENTE);
        return ClienteResponse.from(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteActualizarRequest request) {
        Cliente cliente = buscarEntidad(id);
        usuarioService.validarDatosUnicos(request, id);
        usuarioService.aplicarDatosPersonales(cliente, request);
        return ClienteResponse.from(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarEntidad(id);
        clienteRepository.delete(cliente);
        clienteRepository.flush();
    }

    private Cliente buscarEntidad(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente con id " + id));
    }
}