package com.proyecto.servicios.controller;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.mapper.ClienteMapper;
import com.proyecto.servicios.model.ApiResponse;
import com.proyecto.servicios.model.onboarding.ClienteActualizacionRequest;
import com.proyecto.servicios.model.onboarding.ClienteBajaRequest;
import com.proyecto.servicios.model.onboarding.ClienteBusquedaRequest;
import com.proyecto.servicios.model.onboarding.ClienteRegistroRequest;
import com.proyecto.servicios.model.onboarding.ClienteResponse;
import com.proyecto.servicios.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> registrar(@Valid @RequestBody ClienteRegistroRequest request) {
        Cliente cliente = clienteService.registrarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.exito(HttpStatus.CREATED, "Cliente registrado correctamente", clienteMapper.aResponse(cliente)));
    }

    @PostMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> buscar(@Valid @RequestBody ClienteBusquedaRequest request) {
        List<ClienteResponse> resultado = clienteMapper.aResponseLista(clienteService.buscar(request));
        String mensaje = resultado.isEmpty()
                ? "No se encontraron clientes con los filtros indicados"
                : "Se encontraron " + resultado.size() + " cliente(s)";
        return ResponseEntity.ok(ApiResponse.exito(HttpStatus.OK, mensaje, resultado));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizar(@Valid @RequestBody ClienteActualizacionRequest request) {
        Cliente cliente = clienteService.actualizarCliente(request.getId(), request);
        return ResponseEntity.ok(ApiResponse.exito(HttpStatus.OK, "Cliente actualizado correctamente",
                clienteMapper.aResponse(cliente)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> darDeBaja(@Valid @RequestBody ClienteBajaRequest request) {
        clienteService.darDeBajaCliente(request.getId());
        return ResponseEntity.ok(ApiResponse.<Void>exito(HttpStatus.OK,
                "Cliente dado de baja correctamente; su usuario fue desactivado", null));
    }
}