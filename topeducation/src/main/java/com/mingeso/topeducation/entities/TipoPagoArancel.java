package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_pago_arancel")
public class TipoPagoArancel {
    @Id
    Integer id;
    @Column(name = "tipo")
    String tipo;
    @OneToOne(mappedBy = "tipoPagoArancel")
    @JsonBackReference(value = "estudiante_tipoPagoArancel")
    Estudiante estudiante;
    @OneToOne(mappedBy = "tipoPagoArancel")
    @JsonBackReference
    DescuentoTipoPagoArancel descuentoTipoPagoArancel;
}
