package com.proyecto.servicios.entity.biometria;

import com.proyecto.servicios.entity.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "verificaciones_faciales")
@Getter
@Setter
public class VerificacionFacial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "resultado", nullable = false, length = 10)
    private String resultado;

    @Column(name = "score_similitud", precision = 5, scale = 4)
    private BigDecimal scoreSimilitud;

    @Column(name = "contexto", nullable = false, length = 50)
    private String contexto;

    @Column(name = "referencia_id")
    private Integer referenciaId;

    @Column(name = "fecha_verificacion", nullable = false, updatable = false)
    private LocalDateTime fechaVerificacion;

    @PrePersist
    void onCreate() {
        fechaVerificacion = LocalDateTime.now();
    }
}