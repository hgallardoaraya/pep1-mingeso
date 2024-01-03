package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_colegio")
public class TipoColegio {
    @Id
    Integer id;
    @Column(name = "tipo")
    String tipo;
    @OneToOne(mappedBy = "tipoColegio")
    @JsonBackReference(value = "estudiante_tipoColegio")
    Estudiante estudiante;
    @OneToOne(mappedBy = "tipoColegio")
    MaxCuotasTipoColegio maxCuotasTipoColegio;
    @OneToOne(mappedBy = "tipoColegio")
    @JsonBackReference
    DescuentoTipoColegio descuentoTipoColegio;
}
