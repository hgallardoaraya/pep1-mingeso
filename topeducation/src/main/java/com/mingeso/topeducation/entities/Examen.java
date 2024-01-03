package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "examen")
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "fecha")
    LocalDate fecha;
    @Column(name = "puntaje")
    Integer puntaje;
    @Column(name = "revision")
    int revision;
    @ManyToOne
    @JoinColumn(name="id_estudiante", nullable=false)
    @JsonBackReference(value = "estudiante_examenes")
    Estudiante estudiante;

    public Examen(LocalDate fecha, Integer puntaje, Estudiante estudiante, int revisado){
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.estudiante = estudiante;
        this.revision = revisado;
    }
}
