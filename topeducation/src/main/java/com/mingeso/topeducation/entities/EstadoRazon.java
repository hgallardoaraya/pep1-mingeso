package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estado_razon")
public class EstadoRazon {
    @Id
    Integer id;
    @Column(name = "estado")
    String estado;
    @OneToOne(mappedBy = "estado")
    @JsonBackReference
    Razon razon;
}
