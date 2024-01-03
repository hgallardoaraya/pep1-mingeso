package com.mingeso.topeducation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "descuento_puntaje_prueba")
@Data
@NoArgsConstructor
public class DescuentoPuntajePrueba {
    @Id
    Integer id;
    @Column(name = "puntaje_inferior")
    Double puntajeInferior;
    @Column(name = "puntaje_superior")
    Double puntajeSuperior;
    @Column(name = "porcentaje_descuento")
    Integer porcentajeDescuento;
}
