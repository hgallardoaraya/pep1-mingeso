package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "razon")
@NoArgsConstructor
@Data
public class Razon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "numero_razon")
    Integer numero;
    @Column(name = "monto")
    Integer monto;
    @Column(name = "fecha_inicio")
    LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    LocalDate fechaFin;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_razon", referencedColumnName = "id")
    @JsonManagedReference
    TipoRazon tipo;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estado_razon", referencedColumnName = "id")
    @JsonManagedReference
    EstadoRazon estado;
    @ManyToOne
    @JoinColumn(name="id_estudiante", nullable=false)
    @JsonBackReference(value = "estudiante_razones")
    Estudiante estudiante;
    @ManyToMany(mappedBy = "razones")
    @JsonBackReference
    Set<Pago> pagos;

    public Razon(Integer numero, Integer monto, LocalDate fechaInicio, LocalDate fechaFin, TipoRazon tipo, EstadoRazon estado, Estudiante estudiante){
        this.numero = numero;
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
        this.estado = estado;
        this.estudiante = estudiante;
    }
}
