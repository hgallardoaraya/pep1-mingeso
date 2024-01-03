package com.mingeso.topeducation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "descuento_anio_egreso")
@Entity
public class DescuentoAnioEgreso {
    @Id
    Integer id;
    @Column(name = "cantidad_anios_egreso_inferior")
    Integer cantidadAniosEgresoInferior;
    @Column(name = "cantidad_anios_egreso_superior")
    Integer cantidadAniosEgresoSuperior;
    @Column(name = "porcentaje_descuento")
    Integer porcentajeDescuento;
}
