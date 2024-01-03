package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "descuento_tipo_colegio")
public class DescuentoTipoColegio {
    @Id
    Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_colegio", referencedColumnName = "id")
    @JsonManagedReference
    TipoColegio tipoColegio;
    @Column(name = "porcentaje_descuento")
    Integer porcentajeDescuento;
}
