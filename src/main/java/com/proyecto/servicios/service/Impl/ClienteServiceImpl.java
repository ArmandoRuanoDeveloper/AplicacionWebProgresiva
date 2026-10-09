package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.cliente.Domicilio;
import com.proyecto.servicios.entity.usuario.Rol;
import com.proyecto.servicios.entity.usuario.Usuario;
import com.proyecto.servicios.exception.*;
import com.proyecto.servicios.model.onboarding.ClienteActualizacionRequest;
import com.proyecto.servicios.model.onboarding.ClienteRegistroRequest;
import com.proyecto.servicios.model.onboarding.DomicilioRequest;
import com.proyecto.servicios.repositorys.cliente.ClienteRepository;
import com.proyecto.servicios.repositorys.cliente.DomicilioRepository;
import com.proyecto.servicios.repositorys.usuario.RolRepository;
import com.proyecto.servicios.repositorys.usuario.UsuarioRepository;
import com.proyecto.servicios.service.ClienteService;
import com.proyecto.servicios.service.CuentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private static final String ROL_CLIENTE = "CLIENTE";

    private final ClienteRepository clienteRepository;
    private final DomicilioRepository domicilioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CuentaService cuentaService;
    private final PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(ClienteRepository clienteRepository, DomicilioRepository domicilioRepository,
                               UsuarioRepository usuarioRepository, RolRepository rolRepository,
                               CuentaService cuentaService, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.domicilioRepository = domicilioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.cuentaService = cuentaService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Cliente registrarCliente(ClienteRegistroRequest request) {
        if (clienteRepository.existsByCurp(request.getCurp())) {
            throw new CurpDuplicadaException("Ya existe un cliente registrado con esa CURP");
        }
        if (clienteRepository.existsByRfc(request.getRfc())) {
            throw new RfcDuplicadoException("Ya existe un cliente registrado con ese RFC");
        }
        if (clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new CorreoDuplicadoException("Ya existe un cliente registrado con ese correo");
        }

        Domicilio domicilio = mapearDomicilio(request.getDomicilio());
        domicilioRepository.save(domicilio);

        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setSegundoNombre(request.getSegundoNombre());
        cliente.setApellidoPaterno(request.getApellidoPaterno());
        cliente.setApellidoMaterno(request.getApellidoMaterno());
        cliente.setFechaNacimiento(request.getFechaNacimiento());
        cliente.setCurp(request.getCurp());
        cliente.setRfc(request.getRfc());
        cliente.setSexo(request.getSexo());
        cliente.setNacionalidad(request.getNacionalidad());
        cliente.setEstadoCivil(request.getEstadoCivil());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefonoMovil(request.getTelefonoMovil());
        cliente.setTelefonoAlternativo(request.getTelefonoAlternativo());
        cliente.setOcupacion(request.getOcupacion());
        cliente.setEmpresa(request.getEmpresa());
        cliente.setIngresoMensual(request.getIngresoMensual());
        cliente.setDomicilio(domicilio);
        clienteRepository.save(cliente);

        cuentaService.crearCuentaParaCliente(cliente);

        Rol rolCliente = rolRepository.findByNombre(ROL_CLIENTE)
                .orElseThrow(() -> new IllegalStateException("No existe el rol CLIENTE en la base de datos"));

        Usuario usuario = new Usuario();
        usuario.setCliente(cliente);
        usuario.setRol(rolCliente);
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuarioRepository.save(usuario);

        log.info("Cliente registrado correctamente, id={}", cliente.getId());
        return cliente;
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con id " + id));
    }

    @Override
    public Cliente obtenerPorCurp(String curp) {
        return clienteRepository.findByCurp(curp)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con esa CURP"));
    }

    @Override
    public Cliente obtenerPorRfc(String rfc) {
        return clienteRepository.findByRfc(rfc)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con ese RFC"));
    }

    @Override
    public Cliente obtenerPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con ese correo"));
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> listarActivos() {
        return clienteRepository.findByActivoTrue();
    }

    @Override
    public List<Cliente> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        return clienteRepository.findByFechaCreacionBetween(desde, hasta);
    }

    @Override
    @Transactional
    public Cliente actualizarCliente(Integer id, ClienteActualizacionRequest request) {
        Cliente cliente = obtenerPorId(id);

        if (!cliente.getCorreo().equals(request.getCorreo()) && clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new CorreoDuplicadoException("Ya existe un cliente registrado con ese correo");
        }

        Domicilio domicilio = cliente.getDomicilio();
        DomicilioRequest dReq = request.getDomicilio();
        domicilio.setCalle(dReq.getCalle());
        domicilio.setNumeroExterior(dReq.getNumeroExterior());
        domicilio.setNumeroInterior(dReq.getNumeroInterior());
        domicilio.setColonia(dReq.getColonia());
        domicilio.setMunicipio(dReq.getMunicipio());
        domicilio.setEstado(dReq.getEstado());
        domicilio.setCodigoPostal(dReq.getCodigoPostal());
        domicilio.setPais(dReq.getPais());

        cliente.setNombre(request.getNombre());
        cliente.setSegundoNombre(request.getSegundoNombre());
        cliente.setApellidoPaterno(request.getApellidoPaterno());
        cliente.setApellidoMaterno(request.getApellidoMaterno());
        cliente.setSexo(request.getSexo());
        cliente.setNacionalidad(request.getNacionalidad());
        cliente.setEstadoCivil(request.getEstadoCivil());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefonoMovil(request.getTelefonoMovil());
        cliente.setTelefonoAlternativo(request.getTelefonoAlternativo());
        cliente.setOcupacion(request.getOcupacion());
        cliente.setEmpresa(request.getEmpresa());
        cliente.setIngresoMensual(request.getIngresoMensual());
        // curp, rfc y numeroCuenta nunca se tocan aquí: el DTO no los trae

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void darDeBajaCliente(Integer id) {
        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);

        usuarioRepository.findByClienteId(id).ifPresent(usuario -> {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        });

        log.info("Cliente id={} dado de baja; usuario asociado desactivado", id);
    }

    private Domicilio mapearDomicilio(DomicilioRequest r) {
        Domicilio d = new Domicilio();
        d.setCalle(r.getCalle());
        d.setNumeroExterior(r.getNumeroExterior());
        d.setNumeroInterior(r.getNumeroInterior());
        d.setColonia(r.getColonia());
        d.setMunicipio(r.getMunicipio());
        d.setEstado(r.getEstado());
        d.setCodigoPostal(r.getCodigoPostal());
        d.setPais(r.getPais());
        return d;
    }
}