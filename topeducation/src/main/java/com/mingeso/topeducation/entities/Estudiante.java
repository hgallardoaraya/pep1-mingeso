package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "estudiante")
@Data
@NoArgsConstructor
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;
    @Column(name = "rut")
    String rut;
    @Column(name = "nombre1")
    String nombre1;
    @Column(name = "nombre2")
    String nombre2;
    @Column(name = "apellido1")
    String apellido1;
    @Column(name = "apellido2")
    String apellido2;
    @Column(name = "fecha_nacimiento")
    LocalDate fechaNacimiento;
    @Column(name = "anio_egreso")
    Integer anioEgreso;
    @Column(name = "nombre_colegio")
    String nombreColegio;
    @Column(name = "cuotas_pactadas")
    Integer cuotasPactadas;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_colegio", referencedColumnName = "id")
    @JsonManagedReference(value = "estudiante_tipoColegio")
    @JsonIgnore
    TipoColegio tipoColegio;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_pago_arancel", referencedColumnName = "id")
    @JsonManagedReference(value = "estudiante_tipoPagoArancel")
    @JsonIgnore
    TipoPagoArancel tipoPagoArancel;
    @OneToMany(mappedBy="estudiante")
    @JsonManagedReference(value = "estudiante_razones")
    @JsonIgnore
    List<Razon> razones;
    @OneToMany(mappedBy="estudiante")
    @JsonManagedReference(value = "estudiante_examenes")
    @JsonIgnore
    List<Examen> examenes;
    @OneToMany(mappedBy="estudiante")
    @JsonManagedReference(value = "estudiante_pagos")
    @JsonIgnore
    List<Pago> pagos;
}
