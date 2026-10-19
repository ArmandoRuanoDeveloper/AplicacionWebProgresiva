package com.proyecto.servicios.mapper;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.cliente.Domicilio;
import com.proyecto.servicios.entity.cuenta.Cuenta;
import com.proyecto.servicios.model.onboarding.ClienteResponse;
import com.proyecto.servicios.model.onboarding.CuentaResponse;
import com.proyecto.servicios.model.onboarding.DomicilioResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClienteMapper {

    public ClienteResponse aResponse(Cliente c) {
        ClienteResponse r = new ClienteResponse();
        r.setId(c.getId());
        r.setNombre(c.getNombre());
        r.setSegundoNombre(c.getSegundoNombre());
        r.setApellidoPaterno(c.getApellidoPaterno());
        r.setApellidoMaterno(c.getApellidoMaterno());
        r.setFechaNacimiento(c.getFechaNacimiento());
        r.setCurp(c.getCurp());
        r.setRfc(c.getRfc());
        r.setSexo(c.getSexo());
        r.setNacionalidad(c.getNacionalidad());
        r.setEstadoCivil(c.getEstadoCivil());
        r.setCorreo(c.getCorreo());
        r.setTelefonoMovil(c.getTelefonoMovil());
        r.setTelefonoAlternativo(c.getTelefonoAlternativo());
        r.setOcupacion(c.getOcupacion());
        r.setEmpresa(c.getEmpresa());
        r.setIngresoMensual(c.getIngresoMensual());
        r.setActivo(c.getActivo());
        r.setFechaCreacion(c.getFechaCreacion());

        Domicilio d = c.getDomicilio();
        if (d != null) {
            DomicilioResponse dr = new DomicilioResponse();
            dr.setCalle(d.getCalle());
            dr.setNumeroExterior(d.getNumeroExterior());
            dr.setNumeroInterior(d.getNumeroInterior());
            dr.setColonia(d.getColonia());
            dr.setMunicipio(d.getMunicipio());
            dr.setEstado(d.getEstado());
            dr.setCodigoPostal(d.getCodigoPostal());
            dr.setPais(d.getPais());
            r.setDomicilio(dr);
        }

        List<CuentaResponse> cuentas = new ArrayList<>();
        if (c.getCuentas() != null) {
            for (Cuenta cu : c.getCuentas()) {
                CuentaResponse cr = new CuentaResponse();
                cr.setNumeroCuenta(cu.getNumeroCuenta());
                cr.setSaldo(cu.getSaldo());
                cr.setEstatus(cu.getEstatus());
                cuentas.add(cr);
            }
        }
        r.setCuentas(cuentas);
        return r;
    }

    public List<ClienteResponse> aResponseLista(List<Cliente> clientes) {
        return clientes.stream().map(c -> aResponse(c)).toList();
    }
}