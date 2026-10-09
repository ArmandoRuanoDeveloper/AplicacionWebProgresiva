package com.proyecto.servicios.controller;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.model.onboarding.ClienteActualizacionRequest;
import com.proyecto.servicios.model.onboarding.ClienteRegistroRequest;
import com.proyecto.servicios.model.onboarding.ClienteResponse;
import com.proyecto.servicios.model.onboarding.CuentaResponse;
import com.proyecto.servicios.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRegistroRequest request) {
        Cliente cliente = clienteService.registrarCliente(request);
        return new ResponseEntity<>(aResponse(cliente), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos().stream().map(this::aResponse).toList());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ClienteResponse>> listarActivos() {
        return ResponseEntity.ok(clienteService.listarActivos().stream().map(this::aResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(aResponse(clienteService.obtenerPorId(id)));
    }

    @GetMapping("/curp/{curp}")
    public ResponseEntity<ClienteResponse> obtenerPorCurp(@PathVariable String curp) {
        return ResponseEntity.ok(aResponse(clienteService.obtenerPorCurp(curp)));
    }

    @GetMapping("/rfc/{rfc}")
    public ResponseEntity<ClienteResponse> obtenerPorRfc(@PathVariable String rfc) {
        return ResponseEntity.ok(aResponse(clienteService.obtenerPorRfc(rfc)));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<ClienteResponse> obtenerPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(aResponse(clienteService.obtenerPorCorreo(correo)));
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<ClienteResponse>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(clienteService.listarPorRangoFechas(desde, hasta).stream().map(this::aResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Integer id,
                                                        @Valid @RequestBody ClienteActualizacionRequest request) {
        return ResponseEntity.ok(aResponse(clienteService.actualizarCliente(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Integer id) {
        clienteService.darDeBajaCliente(id);
        return ResponseEntity.noContent().build();
    }

    private ClienteResponse aResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setSegundoNombre(cliente.getSegundoNombre());
        response.setApellidoPaterno(cliente.getApellidoPaterno());
        response.setApellidoMaterno(cliente.getApellidoMaterno());
        response.setFechaNacimiento(cliente.getFechaNacimiento());
        response.setCurp(cliente.getCurp());
        response.setRfc(cliente.getRfc());
        response.setCorreo(cliente.getCorreo());
        response.setTelefonoMovil(cliente.getTelefonoMovil());
        response.setActivo(cliente.getActivo());
        response.setCuentas(
                cliente.getCuentas() == null ? List.of() :
                cliente.getCuentas().stream().map(c -> {
                    CuentaResponse cr = new CuentaResponse();
                    cr.setNumeroCuenta(c.getNumeroCuenta());
                    cr.setSaldo(c.getSaldo());
                    cr.setEstatus(c.getEstatus());
                    return cr;
                }).toList()
        );
        return response;
    }
}