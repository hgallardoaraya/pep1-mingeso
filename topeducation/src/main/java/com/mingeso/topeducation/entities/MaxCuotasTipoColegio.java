package com.mingeso.topeducation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "max_cuotas_tipo_colegio")
public class MaxCuotasTipoColegio {
    @Id
    Integer id;
    @Column(name = "max_cuotas")
    Integer maxCuotas;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_colegio", referencedColumnName = "id")
    TipoColegio tipoColegio;
}
