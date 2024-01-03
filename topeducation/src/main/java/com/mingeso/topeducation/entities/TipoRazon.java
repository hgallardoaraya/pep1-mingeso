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
@Table(name = "tipo_razon")
public class TipoRazon {
    @Id
    Integer id;
    @Column(name = "tipo")
    String tipo;
    @OneToOne(mappedBy = "tipo")
    @JsonBackReference
    Razon razon;
    @OneToOne(mappedBy = "tipo")
    @JsonBackReference
    TotalRazon totalRazon;
}
