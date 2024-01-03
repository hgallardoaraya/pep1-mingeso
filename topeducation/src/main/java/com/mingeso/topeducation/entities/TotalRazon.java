package com.mingeso.topeducation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "total_razon")
public class TotalRazon {
    @Id
    Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_razon", referencedColumnName = "id")
//    @JsonManagedReference
    @JsonIgnore
    TipoRazon tipo;

    @Column(name = "total")
    Integer total;
}
