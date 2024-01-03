package com.mingeso.topeducation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "descuento_tipo_pago_arancel")
public class DescuentoTipoPagoArancel {
    @Id
    Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_pago_arancel", referencedColumnName = "id")
    TipoPagoArancel tipoPagoArancel;
    @Column(name = "porcentaje_descuento")
    Integer porcentajeDescuento;
}
