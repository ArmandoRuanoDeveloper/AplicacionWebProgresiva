package com.proyecto.servicios.repositorys.cliente;

import com.proyecto.servicios.entity.cliente.Cliente;
import com.proyecto.servicios.entity.cuenta.Cuenta;
import com.proyecto.servicios.model.onboarding.ClienteBusquedaRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ClienteSpecification {

    private ClienteSpecification() {}

    public static Specification<Cliente> desde(ClienteBusquedaRequest f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();

            if (f.getId() != null) p.add(cb.equal(root.get("id"), f.getId()));
            if (f.getCurp() != null) p.add(cb.equal(root.get("curp"), f.getCurp()));
            if (f.getRfc() != null) p.add(cb.equal(root.get("rfc"), f.getRfc()));
            if (f.getCorreo() != null) p.add(cb.equal(root.get("correo"), f.getCorreo()));
            if (f.getActivo() != null) p.add(cb.equal(root.get("activo"), f.getActivo()));
            if (f.getNombre() != null) p.add(contiene(cb, root.get("nombre"), f.getNombre()));
            if (f.getApellidoPaterno() != null) p.add(contiene(cb, root.get("apellidoPaterno"), f.getApellidoPaterno()));
            if (f.getApellidoMaterno() != null) p.add(contiene(cb, root.get("apellidoMaterno"), f.getApellidoMaterno()));

            if (f.getFechaRegistroDesde() != null) {
                p.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("fechaCreacion"),
                        f.getFechaRegistroDesde().atStartOfDay()));
            }
            if (f.getFechaRegistroHasta() != null) {
                p.add(cb.lessThan(root.<LocalDateTime>get("fechaCreacion"),
                        f.getFechaRegistroHasta().plusDays(1).atStartOfDay()));
            }

            if (f.tieneFiltrosDeCuenta()) {
                Join<Cliente, Cuenta> cuenta = root.join("cuentas", JoinType.INNER);
                query.distinct(true);
                if (f.getNumeroCuenta() != null) p.add(cb.equal(cuenta.get("numeroCuenta"), f.getNumeroCuenta()));
                if (f.getEstatusCuenta() != null) p.add(cb.equal(cuenta.get("estatus"), f.getEstatusCuenta()));
                if (f.getSaldoMinimo() != null) p.add(cb.greaterThanOrEqualTo(cuenta.<java.math.BigDecimal>get("saldo"), f.getSaldoMinimo()));
                if (f.getSaldoMaximo() != null) p.add(cb.lessThanOrEqualTo(cuenta.<java.math.BigDecimal>get("saldo"), f.getSaldoMaximo()));
            }

            return cb.and(p.toArray(new Predicate[0]));
        };
    }

    private static Predicate contiene(CriteriaBuilder cb, Expression<String> campo, String valor) {
        return cb.like(cb.lower(campo), "%" + valor.toLowerCase() + "%");
    }
}