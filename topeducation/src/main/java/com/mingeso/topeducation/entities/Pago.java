package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "total")
    Integer total;
    @Column(name = "fecha")
    LocalDate fecha;
    @ManyToOne
    @JoinColumn(name="id_estudiante", nullable=false)
    @JsonIgnore
    @JsonBackReference(value = "estudiante_pagos")
    Estudiante estudiante;
    @ManyToMany
    @JoinTable(
            name = "pago_razon",
            joinColumns = @JoinColumn(name = "id_pago"),
            inverseJoinColumns = @JoinColumn(name = "id_razon"))
    @JsonManagedReference
    List<Razon> razones;

}
