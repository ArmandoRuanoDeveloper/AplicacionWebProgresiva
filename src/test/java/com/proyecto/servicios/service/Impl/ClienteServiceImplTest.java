package com.proyecto.servicios.service.Impl;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.usuario.Rol;
import com.proyecto.servicios.exception.ClienteNoEncontradoException;
import com.proyecto.servicios.exception.CorreoDuplicadoException;
import com.proyecto.servicios.exception.CurpDuplicadaException;
import com.proyecto.servicios.exception.RfcDuplicadoException;
import com.proyecto.servicios.model.onboarding.ClienteRegistroRequest;
import com.proyecto.servicios.model.onboarding.DomicilioRequest;
import com.proyecto.servicios.repositorys.cliente.ClienteRepository;
import com.proyecto.servicios.repositorys.cliente.DomicilioRepository;
import com.proyecto.servicios.repositorys.usuario.RolRepository;
import com.proyecto.servicios.repositorys.usuario.UsuarioRepository;
import com.proyecto.servicios.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private DomicilioRepository domicilioRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RolRepository rolRepository;
    @Mock private CuentaService cuentaService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteRegistroRequest request;

    @BeforeEach
    void setUp() {
        request = new ClienteRegistroRequest();
        request.setNombre("Juan");
        request.setApellidoPaterno("Pérez");
        request.setApellidoMaterno("López");
        request.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        request.setCurp("PELJ000101HDFRPN01");
        request.setRfc("PELJ000101AB1");
        request.setSexo("M");
        request.setNacionalidad("Mexicana");
        request.setEstadoCivil("Soltero");
        request.setCorreo("juan@test.com");
        request.setTelefonoMovil("5512345678");
        request.setOcupacion("Ingeniero");
        request.setEmpresa("ACME");
        request.setIngresoMensual(new BigDecimal("15000.00"));
        request.setPassword("Clave123!");

        DomicilioRequest domicilio = new DomicilioRequest();
        domicilio.setCalle("Calle 1");
        domicilio.setNumeroExterior("10");
        domicilio.setColonia("Centro");
        domicilio.setMunicipio("CDMX");
        domicilio.setEstado("CDMX");
        domicilio.setCodigoPostal("01000");
        domicilio.setPais("México");
        request.setDomicilio(domicilio);
    }

    @Test
    void registrarCliente_exitoso_creaClienteCuentaYUsuario() {
        when(clienteRepository.existsByCurp(any())).thenReturn(false);
        when(clienteRepository.existsByRfc(any())).thenReturn(false);
        when(clienteRepository.existsByCorreo(any())).thenReturn(false);

        Rol rolCliente = new Rol();
        rolCliente.setNombre("CLIENTE");
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(passwordEncoder.encode("Clave123!")).thenReturn("hash-cifrado");

        Cliente resultado = clienteService.registrarCliente(request);

        assertEquals("Juan", resultado.getNombre());
        verify(domicilioRepository).save(any());
        verify(clienteRepository).save(any());
        verify(cuentaService).crearCuentaParaCliente(resultado);
        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("hash-cifrado")));
    }

    @Test
    void registrarCliente_curpDuplicada_lanzaExcepcion() {
        when(clienteRepository.existsByCurp(any())).thenReturn(true);

        assertThrows(CurpDuplicadaException.class, () -> clienteService.registrarCliente(request));
        verify(clienteRepository, never()).save(any());
        verify(cuentaService, never()).crearCuentaParaCliente(any());
    }

    @Test
    void registrarCliente_rfcDuplicado_lanzaExcepcion() {
        when(clienteRepository.existsByCurp(any())).thenReturn(false);
        when(clienteRepository.existsByRfc(any())).thenReturn(true);

        assertThrows(RfcDuplicadoException.class, () -> clienteService.registrarCliente(request));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void registrarCliente_correoDuplicado_lanzaExcepcion() {
        when(clienteRepository.existsByCurp(any())).thenReturn(false);
        when(clienteRepository.existsByRfc(any())).thenReturn(false);
        when(clienteRepository.existsByCorreo(any())).thenReturn(true);

        assertThrows(CorreoDuplicadoException.class, () -> clienteService.registrarCliente(request));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ClienteNoEncontradoException.class, () -> clienteService.obtenerPorId(99));
    }

    @Test
    void darDeBajaCliente_desactivaClienteYUsuario() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setActivo(true);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        com.proyecto.servicios.entity.usuario.Usuario usuario = new com.proyecto.servicios.entity.usuario.Usuario();
        usuario.setActivo(true);
        when(usuarioRepository.findByClienteId(1)).thenReturn(Optional.of(usuario));

        clienteService.darDeBajaCliente(1);

        assertFalse(cliente.getActivo());
        assertFalse(usuario.getActivo());
        verify(clienteRepository).save(cliente);
        verify(usuarioRepository).save(usuario);
    }
}